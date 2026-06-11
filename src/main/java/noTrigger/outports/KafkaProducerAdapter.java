package noTrigger.outports;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class KafkaProducerAdapter implements KafkaProducerPort{
    private final Logger log = LogManager.getLogger(KafkaProducerAdapter.class);

    public kafkaProducerAdapter() throws IOException{
    }

    @Override
    public void sendChunk(String topic, String key, byte[] chunk, Map<String, String> headers) throws ExecutionException, InterruptedException {
        Headers kafkaHeaders = new RecordHeaders();
        headers.forEach((k, v) -> kafkaHeaders.add(k, v.getBytes()));

        ProducerRecord<String, byte[]> binaryRecord = new ProducerRecord<String, byte[]>(
                topic,
                null,
                key,
                chunk,
                kafkaHeaders
        );
        binaryRecord.send(binaryRecord).get();
    }
}
