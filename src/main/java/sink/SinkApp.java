package sink;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.logging.log4j.ThreadContext;
import sink.config.SinkConfig;
import sink.config.SinkRules;
import sink.config.YamlSinkConfig;
import sink.eventprocessing.FileMergeContext;
import sink.eventprocessing.handlers.*;
import sink.sinkpostprocessing.DefaultFileMoveAction;
import sink.sinkpostprocessing.PostProcessActionExecutor;
import sink.sinkpostprocessing.PostProcessActionProvider;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SinkApp {
    private static final Logger log = LogManager.getLogger(DefaultFileMoveAction.class);

    public static void main(String[] args) throws IOException {
        AtomicBoolean running = new AtomicBoolean(true);
        KafkaConsumer<String, byte[]> consumer;

        SinkRules rules = new YamlSinkConfig();
        rules.getAll().forEach(SinkConfig::applyDefaults);

        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            Validator validator = (Validator) factory.getValidator();

            Set<ConstraintViolation<SinkRules>> violations = validator.validate(rules);
            if(!violations.isEmpty()){
                for(ConstraintViolation<SinkRules> violation : violations){
                    log.error("Sink Config validation error: {}", violation.getMessage());
                }
                throw new IllegalStateException("Sink configuration is invalid. see logs for details");
            }
        }

        List<String> topics = rules.getAll()
                .stream()
                .map(SinkConfig::getTopic)
                .collect(Collectors.toList());

        try{
            consumer = getOrCreate();
            consumer.subscribe(Pattern.compile(String.join("|", topics)));
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        Handler chain = new EventFilePatternHandler();
        chain
                .setNext(new MetaDataExtractionHandler())
                .setNext(new TempDirectoryHandler())
                .setNext(new ChunkMergeHandler())
                .setNext(new ChunkWriteHandler())
                .setNext(new LastChunkCheckHandler())
                .setNext(new ChecksumValidationHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            running.set(false);
            try{
                consumer.wakeup();
            }catch (Exception e){
                log.error("Error closing consumer", e);
            }
        }));

        try{
            while(running.get()){
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis());
                for(ConsumerRecords<String, byte[]> record : records){
                    String eventUUID = record.key();
                    ThreadContext.put("eventUUID", eventUUID);
                    FileMergeContext fileMergeContext = new FileMergeContext(record, rules);
                    chain.handle(fileMergeContext);
                    log.debug("context: {}", fileMergeContext);
                    if(fileMergeContext.isMerged()){
                        PostProcessActionExecutor actionExecutor = new PostProcessActionExecutor(
                                PostProcessActionProvider.fromConfig(rules.getAll().get(fileMergeContext.getMatchingRuleIndex()))
                        );
                        actionExecutor.executeAll(fileMergeContext);
                    }
                    consumer.commitAsync();
                    ThreadContext.clearAll();
                }
            }
        }catch (WakeupException e){
            log.info("kafka consumer closed");
        }catch (Exception e){
            log.error("Unexpected error processing messages", e);
        }
        finally {
            try{
                consumer.close();
            }catch (Exception e){
                log.error("Error closing consumer", e);
            }
        }

    }
}
