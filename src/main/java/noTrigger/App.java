package noTrigger;

import noTrigger.core.StreamNewOrModifiedFilesUseCaseImpl;
import noTrigger.outports.*;

public class App {
    public static void main(String[] args) throws Exception{

        NoTriggerConfigLoader configLoader = NoTriggerConfigLoader.getOrSetConfig();
        configLoader.dumpConfig();

        ScanStateRepository stateRepo = new FileScanStateRepository();
        DirectoryScannerPort directoryScannerAdapter = new FileChunkReaderAdapter();
        KafkaProducerPort kafkaProducer = new KafkaProducerAdapter();
        FileChunkReaderPort fileChunkReader = new FileChunkReaderAdapter();

        StreamNewOrModifiedFilesUseCaseImpl useCase = new StreamNewOrModifiedFilesUseCaseImpl(
                stateRepo,
                directoryScannerAdapter,
                fileChunkReader,
                kafkaProducer
        );

        RunnableTaskDefinition runnableTaskDefinition = new RunnableTaskDefinition(useCase);
        ScheduledTaskManager scheduledTaskManager = new ScheduledTaskManager(2, useCase, runnableTaskDefinition);
        scheduledTaskManager.registerTasks(configLoader.getNoTriggerConfig().getDirectories());
        scheduledTaskManager.startAll();

        Runtime.getRunTime().addShutdownHook(new Thread(()->{
            log.info("Shutdown hook started...");
            scheduledTaskManager.awaitTermination();
            LogManager.shutdown();
        }));

        try{
            Thread.currentThread().join();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

}
