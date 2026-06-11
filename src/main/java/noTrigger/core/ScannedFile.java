package noTrigger.core;

import org.springframework.format.datetime.standard.InstantFormatter;

import java.nio.file.Path;
import java.time.Instant;

public class ScannedFile {
    private final Path path;
    private final Instant lastModifiedTime;
    private final long size;

    public ScannedFile(Path path, Instant lastModifiedTime, long size){
        this.path = path;
        this.size= size;
        this.lastModifiedTime=lastModifiedTime;
    }

    public Path getPath() {
        return path;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString(){
        return "ScannedFile{" +
                "path=" + path +
                ", lastModifiedTime=" + InstantFormatter.write(lastModifiedTime)+
                ", size=" + size +
                '}';
    }

}
