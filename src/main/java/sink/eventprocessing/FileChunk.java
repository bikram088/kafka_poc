package sink.eventprocessing;

import java.io.Serializable;

public class FileChunk implements Serializable {
    private final int chunkOffset;
    private final int chunkActualSize;
    private final byte[] data;

    public FileChunk(int chunkOffset, int chunkActualSize, byte[] data) {
        this.chunkOffset = chunkOffset;
        this.chunkActualSize = chunkActualSize;
        this.data = data;
    }

    public int getChunkOffset() {
        return chunkOffset;
    }

    public int getChunkActualSize() {
        return chunkActualSize;
    }

    public byte[] getData() {
        return data;
    }
}
