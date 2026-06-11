package noTrigger.outports;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface KafkaProducerPort{
    void sendChunk(String topic, String key, byte[] chunk, Map<String, String> headers) throws ExecutionException, InterruptedException;
}
