package file.reader.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import file.reader.model.BitCoinData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaBatchProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaBatchProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;
    private final int batchSize;
    private final List<BitCoinData> buffer = new ArrayList<>();

    public KafkaBatchProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${app.kafka.topic}") String topic, @Value("${app.kafka.batch-size}") int batchSize) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.batchSize = batchSize;
        this.objectMapper = new ObjectMapper();
    }

    public synchronized void add(BitCoinData data) throws Exception {
        buffer.add(data);
        if (buffer.size() >= batchSize) {
            flush();
            Thread.sleep(1000);
        }
    }

    public synchronized void flush() throws Exception {
        if (buffer.isEmpty()) return;
        for (BitCoinData data : buffer) {
            String json = objectMapper.writeValueAsString(data);
            kafkaTemplate.send(topic, json);
        }
        logger.info("✅ Published batch of " + buffer.size() + " records to topic " + topic);
        buffer.clear();
    }
}