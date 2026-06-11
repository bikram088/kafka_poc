package POC.context;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.nio.file.Path;

public class FileStreamingContext {
    private Path file;
    private String topic;
    private Headers headers = new RecordHeaders();
    private boolean streamed;

    public void setHeaders(Headers newHeaders){
        newHeaders.forEach(header -> {
            headers.remove(header.key());
            headers.add(header);
        });
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Headers getHeaders() {
        return headers;
    }

    public boolean isStreamed() {
        return streamed;
    }

    public void setStreamed(boolean streamed) {
        this.streamed = streamed;
    }
}
