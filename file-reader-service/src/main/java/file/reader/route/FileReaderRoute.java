package file.reader.route;

import org.apache.camel.builder.RouteBuilder;
import file.reader.utils.FileConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileReaderRoute extends RouteBuilder {

    @Autowired
    private CsvToBitCoinKafkaProcessor csvToBitCoinKafkaProcessor;


    @Override
    public void configure() throws Exception {
        onException(Exception.class).handled(true).log("Failed to process file").to("file:" + FileConstants.ERROR_PATH + "?fileName=${file:name.noext}-error.${file:ext}");

        from("direct:bitcoin-file-route").routeId("bitcoin-file-route-direct").process(exchange -> {
            File file = new File(FileConstants.BASE_PATH, FileConstants.FILE_NAME);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + file.getAbsolutePath());
            }
            exchange.getIn().setBody(file);
        }).process(csvToBitCoinKafkaProcessor).process(exchange -> moveFileToProcessedFolder()).log("✅ CSV file processed and moved to 'processed' folder successfully.");
    }

    private void moveFileToProcessedFolder() {
        File file = new File(FileConstants.BASE_PATH, FileConstants.FILE_NAME);
        File processedDir = new File(FileConstants.PROCESSED_PATH);
        if (!processedDir.exists() && !processedDir.mkdirs()) {
            throw new RuntimeException("Failed to create processed folder");
        }
        File processedFile = new File(processedDir, FileConstants.FILE_NAME.replace(".csv", "-done.csv"));
        boolean moved = file.renameTo(processedFile);
        if (!moved) {
            throw new RuntimeException("Failed to move file after processing");
        }
    }
}
