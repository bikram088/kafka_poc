package POC.headers;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.io.IOException;

public class ChunkHeader {
    private ChunkHeader(){
    }

    public static Headers fromFileChunkReader(FileChunkReader reader) throws IOException {
        Headers chunkHeaders = new RecordHeaders();
        chunkHeaders.add("chunk.number", String.valueOf(reader.getCurrentChunkIndex()).getBytes());

        if(reader.isLastChunk()){
            chunkHeaders.add("file.checksum", calculateSHA256Checksum(reader.getFilePath().toString()).getBytes());
        }
        return chunkHeaders;
    }
}