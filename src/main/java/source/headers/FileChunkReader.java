package POC.headers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class FileChunkReader {
    private final Path filePath;
    private final SeekableByteChannel channel;
    private final long fileSize;
    private final long maxChunkSize;
    private int chunkIndex = 0;
    private long chunkStartOffset = 0;
    private int actualChunkSize = 0;
    private int totalChunks;


    public FileChunkReader(Path filePath, SeekableByteChannel channel, long fileSize, long maxChunkSize) throws IOException {
        this.filePath = filePath;
        this.channel = Files.newByteChannel(filePath, StandardOpenOption.READ);
        this.fileSize = Files.size(filePath);
        this.maxChunkSize = maxChunkSize;
        this.totalChunks = (int) Math.ceil((double) fileSize / maxChunkSize);
    }

    public boolean hadNextChunk() throws IOException{
        return channel.position() < fileSize;
    }
    public byte[] nextChunk() throws IOException{
        chunkStartOffset = channel.position();

        long remaining = fileSize - channel.position();
        int size = (int) Math.min(maxChunkSize, remaining);
        actualChunkSize = size;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        int bytesRead = channel.read(buffer);

        if(bytesRead == -1)
            return new byte[0];

        chunkIndex++;
        return buffer.array();
    }
    public boolean isLastchunk() throws IOException{
        return !hadNextChunk();
    }

    public Path getFilePath() {
        return filePath;
    }

    public SeekableByteChannel getChannel() {
        return channel;
    }

    public long getFileSize() throws IOException {
        return channel.size();
    }

    public long getMaxChunkSize() {
        return maxChunkSize;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public long getChunkStartOffset() {
        return chunkStartOffset;
    }

    public void setChunkStartOffset(long chunkStartOffset) {
        this.chunkStartOffset = chunkStartOffset;
    }

    public int getActualChunkSize() {
        return actualChunkSize;
    }

    public void setActualChunkSize(int actualChunkSize) {
        this.actualChunkSize = actualChunkSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    public void close() throws IOException{
        if(channel != null && channel.isOpen()){
            channel.close();
        }
    }

}
