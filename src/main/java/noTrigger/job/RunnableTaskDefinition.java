package noTrigger.job;

import noTrigger.config.DirectoryScanConfig;
import noTrigger.core.StreamNewOrModifiedFilesUseCase;

import java.util.logging.LogManager;

public class RunnableTaskDefinition {
    private final Logger log = LogManager.getLogger(RunnableTaskDefinition.class);
    private final StreamNewOrModifiedFilesUseCase useCase;

    public RunnableTaskDefinition(StreamNewOrModifiedFilesUseCase useCase) {
        this.useCase = useCase;
    }

    public Runnable createTask(DirectoryScanConfig scanDefinition){
        return new Runnable(){
            public void run(){
                try{
                    log.info("Runnable for log dir: {}/{}", scanDefinition.getDirectoryPath(), scanDefinition.getFileNamePattern());
                    useCae.streamFromDirectory(scanDefinition);
                }catch (Exception e){
                    log.error("Runnable error for: {}/{} --> {}",
                    scanDefinition.getDirectoryPath(),
                    scanDefinition.getFileNamePattern(),
                    e.getMessage(),
                    e
                    );
                }
            }
        };
    }
}
