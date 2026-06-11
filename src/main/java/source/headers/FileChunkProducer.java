//package POC.headers;
//
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.header.Header;
//
//import java.io.IOException;
//import java.util.List;
//
//public class FileChunkProducer {
//    private final Producer<String, byte[]> binaryProducer;
//
//    public FileChunkProducer() throws IOException{
//        this.binaryProducer = ProducerCache.getORCreate();
//    }
//
//    public void sendChunk(String kafkaTopic, String key, byte[] data, List<Header> headers)throws Exception{
//        ProducerRecord<String, byte[]> binaryRecord = new ProducerRecord<String, byte[]>(
//                kafkaTopic,
//                null,
//                key,data,headers
//        );
//        binaryProducer.send(binaryRecord).get();
//    }
//    public sendChunk(String kafkaTopic, String key, byte[] data, Header headers)throws IOException{
//        ProducerRecord<String, byte[]> binaryRecord = new ProducerRecord<String, byte[]>(
//                kafkaTopic,
//                null,
//                key,
//                data,
//                headers
//        );
//        binaryProducer.send(binaryRecord).get();
//    }
//}
