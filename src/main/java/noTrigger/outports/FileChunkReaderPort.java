package noTrigger.outports;

import noTrigger.core.ScannedFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FileChunkReaderPort {
    void readInChunk(
            ScannedFile file,
            int chunkSize,
            ChunkConsumer chunkConsumer
    ) throws IOException;

    @FunctionalInterface
    interface ChunkConsumer{
        void accept(byte[] chunk, Map<String, String> metadataHeaders) throws ExecutionException, InterruptedException;
    }
}
