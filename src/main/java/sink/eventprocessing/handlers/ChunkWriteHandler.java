package sink.eventprocessing.handlers;

import sink.eventprocessing.FileChunk;
import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChunkWriteHandler extends Handler{
    private static final Logger log = LogManager.getLogger(ChunkWriteHandler.class);

    @Override
    public boolean process(FileMergeContext context) throws FileNotFoundException {
        FileChunk fileChunk = new FileChunk(
                context.getChunkStartOffset(),
                context.getChunkActualSize(),
                context.getRecord().value()
        );
        Path chunkFilePath = context.getChunkDirPath();
        chunkFilePath = chunkFilePath.resolve(String.valueOf(context.getChunkNumber()));
        try(FileOutputStream chunkOut = new FileOutputStream(chunkFilePath.toFile())){
            ObjectOutputStream oos = new ObjectOutputStream(chunkOut);
            oos.writeObject(fileChunk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
