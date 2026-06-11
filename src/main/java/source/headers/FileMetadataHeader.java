package POC.headers;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileMetadataHeader {
    private FileMetadataHeader(){}

    public static Headers fromFilePath(Path filePath) throws IOException {
        Headers headers = new RecordHeaders();

        BasicFileAttributes fileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);

        headers.add("file.path", filePath.toString().getBytes());
        headers.add("file.name", filePath.getFileName().toString().getBytes());
        return headers;
    }
}
