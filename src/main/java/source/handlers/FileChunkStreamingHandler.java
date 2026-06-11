package POC.handlers;

import POC.context.FileProcessingContext;
import POC.context.FileStreamingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.IOException;

public class FileChunkStreamingHandler extends FileProcessingHandler {
    private static final Logger log = LogManager.getLogger(FileChunkStreamingHandler.class);

    @Override
    public boolean process(FileProcessingContext context){
        FileChunkProducer producer = null;
        try{
            producer = new FileChunkProducer();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        for(FileStreamingContext streamingContext : context.getStreamingContexts()){
            try{
                log("streaming File: ");
                FileChunkReader reader = new FileChunkReader(streamingContext.getFile(), 1024*1024 - 10*1024);

                do{
                    byte[] payload = reader.nextChunk();
                    streamingContext.setHeaders(ChunklHeader.fromFileChunkReader(reader));

                    producer.sendChunk(streamingContext.getTopic(),
                            ThreadContext.get("streamUUID"),
                            payload,
                            streamingContext.getHeaders()
                    );
                }while (reader.hasNextChunk());
                reader.close();
                streamingContext.setStreamed(true);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
