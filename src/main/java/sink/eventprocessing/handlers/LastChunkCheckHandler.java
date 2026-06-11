package sink.eventprocessing.handlers;

import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LastChunkCheckHandler extends Handler{
    private static final Logger log = LogManager.getLogger(LastChunkCheckHandler.class);

    @Override
    public boolean process(FileMergeContext context) throws FileNotFoundException {
       if(context.isLastChunk())
           log.info("last chunk");
        return context.isLastChunk();
    }
}
