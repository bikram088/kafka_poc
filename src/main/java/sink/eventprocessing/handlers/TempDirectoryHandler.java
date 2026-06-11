package sink.eventprocessing.handlers;

import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TempDirectoryHandler extends Handler{
    private static final Logger log = LogManager.getLogger(MetaDataExtractionHandler.class);

    @Override
    public boolean process(FileMergeContext context) {
        Path chunkDirPath = SinkBootstrap.sinkAppDataDir.resolve("temp");
        chunkDirPath = chunkDirPath.resolve(context.getUuid() + "_" + context.getFileNameWithoutExtension());
        chunkDirPath = chunkDirPath.resolve("chunks");

        try{
            Files.createDirectories(chunkDirPath);
        }catch (IOException e){
            log.error("Unable to create temporary chunk directory: {}", chunkDirPath, e);
            return false;
        }
        context.setChunkDirPath(chunkDirPath);
        return false;
    }
}
