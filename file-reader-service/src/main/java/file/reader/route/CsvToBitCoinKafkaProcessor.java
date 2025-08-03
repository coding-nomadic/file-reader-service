package file.reader.route;

import file.reader.utils.ObjectUtils;
import file.reader.kafka.KafkaBatchProducer;
import file.reader.model.BitCoinData;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.File;
import java.io.FileReader;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsvToBitCoinKafkaProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(CsvToBitCoinKafkaProcessor.class);
    private static final int EXPECTED_COLUMNS = 6;

    @Autowired
    private KafkaBatchProducer kafkaBatchProducer;

    @Override
    public void process(Exchange exchange) throws Exception {
        List<BitCoinData> bitCoinData = new ArrayList<>();
        File file = exchange.getIn().getBody(File.class);
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Invalid file received in exchange || No file present in the path !");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] tokens = line.split(",", -1);
                if (tokens.length < EXPECTED_COLUMNS) {
                    logger.error("Skipping malformed line: " + line);
                    continue;
                }
                BitCoinData data = ObjectUtils.mapToBitCoinData(tokens);
                kafkaBatchProducer.add(data);
            }
        }
    }
}
