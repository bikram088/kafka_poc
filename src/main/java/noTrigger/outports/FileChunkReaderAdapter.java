package noTrigger.outports;

import noTrigger.core.ScannedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.datetime.standard.InstantFormatter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FileChunkReaderAdapter implements FileChunkReaderPort{
        private final Logger log = LogManager.getLogger(FileChunkReaderAdapter.class);

        @Override
        public void readInChunk(ScannedFile file, int chunkSizeBytes, ChunkConsumer chunkConsumer) throws IOException {
            Path path = file.getPath();

            Map<String, String> headers = new HashMap<>();
            headers.put("file.path", path.toString());
            headers.put("file.name", path.getFileName().toString());
            headers.put("file.last.modified", InstantFormatter.write(file.getLastModifiedTime()));

            try(InputStream is = Files.newInputStream(path);
                BufferedInputStream bis = new BufferedInputStream(is, chunkSizeBytes)){
                byte[] buffer = new byte[chunkSizeBytes];
                int chunkNumber = 0;
                long totalBytesRead = 0;
                int totalChunks = (int) Math.ceil((double) file.getSize() / chunkSizeBytes);

                int bytesRead;
                while((bytesRead = bis.read(buffer)) != -1){
                    chunkNumber++;
                    long startOffset = totalBytesRead;
                    int actualSize = bytesRead;
                    boolean isLast = (bytesRead < chunkSizeBytes) || (totalBytesRead + bytesRead == file.getSize());

                    byte[] chunk = (bytesRead == buffer.length) ? buffer : Arrays.copyOf(buffer, bytesRead);

                    headers.put("chunk.number", String.valueOf(chunkNumber));
                    headers.put("chunk.total", String.valueOf(totalChunks));
                    headers.put("chunk.startOffset", String.valueOf(startOffset));
                    headers.put("chunk.actualSize", String.valueOf(actualSize));
                    headers.put("chunk.last", String.valueOf(isLast));

                    if(isLast){
                        headers.put("file.checksum", calculateSHA256Checksum(file.getPath().toString()));
                        headers.put("file.size", String.valueOf(chunkNumber));
                    }
                    chunkConsumer.accept(chunk, headers);
                    totalBytesRead += bytesRead;
                }
            }catch (IOException e){
                log.error("Failed to read file: {}", file.getPath(), e);
                throw new RuntimeException( e);
            }catch (ExecutionException | InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
