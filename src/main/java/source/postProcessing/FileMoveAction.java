package POC.postProcessing;

import POC.context.FileProcessingContext;
import POC.context.FileStreamingContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileMoveAction implements PostProcessAction{

    private final Path finished;
    private final Path error;

    public FileMoveAction(Path finished, Path error) {
        this.finished = finished;
        this.error = error;
    }

    @Override
    public void execute(FileProcessingContext context) {
        Path targetDir;

        if(context.getExpectedFiles().size() != context.getActualFiles().size()){
            targetDir = error;
        } else{
            targetDir = context.getStreamingContexts().stream()
                    .allMatch(FileStreamingContext::isStreamed) ? finished : error;
        }

        context.getActualFiles().forEach(file -> {
            try{
                Files.move(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                log.error(e);
            }
        });

        if(Files.exists(context.getTriggerFile())){
            try{
                Files.move(context.getTriggerFile(), targetDir.resolve(context.getTriggerFile().getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
