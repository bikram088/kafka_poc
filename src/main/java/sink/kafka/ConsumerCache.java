package sink.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.tomcat.util.net.SendfileState;
import sink.SinkBootstrap;
import sink.config.AppConsumerConfig;
import sink.config.YamlSinkConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Logger;

public class ConsumerCache {
    public static final Logger log = LogManager.getLogger(ConsumerCache.class.);

    private static Consumer<String,byte[]> consumer;
    private static final AppConsumerConfig consumerConfig = YamlSinkConfig.getConsumerConfig();

    private ConsumerCache(){}

    public static KafkaConsumer<String, byte[]> getOrCreate() throws IOException{
        if(consumer == null){
            synchronized (ConsumerCache.class){
                if(consumer == null){
                    if(consumerConfig == null || consumerConfig.getGroupId() == null || consumerConfig.getGroupId().isEmpty()){
                        throw new IllegalStateException("Consumer groupId missing or blank in configuration.");
                    }
                    consumer = createNewConsumer();
                    log.info("Kafka Consumer :: created");
                }
            }
        }
        return (KafkaConsumer<String, byte[]>) consumer;
    }

    public static KafkaConsumer<String, byte[]> createNewConsumer() throws IOException{
        Properties props = new Properties();
        props.load(Files.newInputStream(SinkBootstrap.consumerKafkaConfigFile));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getGroupId());
        return new KafkaConsumer<>(props);
    }
}
