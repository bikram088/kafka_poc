package sink.sinkpostprocessing;

import sink.eventprocessing.FileMergeContext;
import sink.eventprocessing.handlers.ChunkMergeHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DefaultFileMoveAction {
    private static final Logger log = LogManager.getLogger(DefaultFileMoveAction.class);

    private final Path targetDir;

    public DefaultFileMoveAction(Path targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public void execute(FileMergeContext context){
        try{
            Files.move(context.getMergedFilePath(), targetDir.resolve(context.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            log.info("Moved file");
        }catch (IOException e){
            log.error("Failed to move file");
        }

        Path tempChunkDir = context.getMergedFilePath().getParent();
        try{
            if(Files.exists(tempChunkDir)){
                Files.walk(tempChunkDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try{
                                Files.delete(path);
                            }catch (IOException e){
                                log.error("Failed to delete: {}", path, e);
                            }
                        });
                log.info("Deleted temporary chunk directory: {}", tempChunkDir);
            }else {
                log.info("Directory doesn't exist: {}", tempChunkDir);
            }
            log.info("Clean temp chunk directory: {}", tempChunkDir);
        }catch (IOException e){
            log.error("Failed to delete tmp chunk directory", tempChunkDir, e);
        }
    }
}
