package file.reader.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import file.reader.model.BitCoinData;

import java.io.IOException;

public class ObjectUtils {
    public static BitCoinData mapToBitCoinData(String[] tokens) {
        BitCoinData data = new BitCoinData();
        data.setTimestamp(tokens[0].trim());
        data.setOpen(tokens[1].trim());
        data.setHigh(tokens[2].trim());
        data.setLow(tokens[3].trim());
        data.setClose(tokens[4].trim());
        data.setVolume(tokens[5].trim());
        return data;
    }

    public static String toString(Object objectToConvert) throws IOException {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(objectToConvert);
    }
}
