package sink.eventprocessing.handlers;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.protocol.types.Field;
import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MetaDataExtractionHandler extends Handler{
    private static final Logger log = LogManager.getLogger(MetaDataExtractionHandler.class);

    @Override
    public boolean process(FileMergeContext context) {
        context.setUuid(context.getUuid());
        Headers headers = context.getRecord().headers();
        context.setFileName(extractHeaderString(headers, "file.name"));

        int dotIndex = context.getFileName().lastIndexOf('.');
        if(dotIndex == -1){
            context.setFileNameWithoutExtension(context.getFileName());
        }else {
            context.setFileNameWithoutExtension(context.getFileName().substring(0, dotIndex));
        }

        context.setFileSize(extractHeaderLong(headers, "file.size"));
        context.setChecksum(extractHeaderString(headers, "file.checksum"));
        context.setChunkNumber(extractHeaderInt(headers, "chunk.number"));
        context.setChunkStartOffset(extractHeaderInt(headers, "chunk.startOffset"));
        context.setChunkActualSize(extractHeaderInt(headers, "chunk.actualSize"));
        context.setLastChunk(extractHeaderBoolean(headers, "chunk.last"));
        return true;
    }

    public String extractHeaderString(Headers headers, String headerKey){
        Header header = headers.lastHeader(headerKey);
        if(header == null){
            return null;
        }
        return new String(header.value());
    }

    public long extractHeaderLong (Headers headers, String headerkey){
        String value = extractHeaderString(headers, headerkey);
        try{
            return Long.parseLong(value);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public int extractHeaderInt(Headers headers, String headerKey){
        String value = extractHeaderString(headers, headerKey);
        try{
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public boolean extractHeaderBoolean(Headers headers, String headerKey){
        String value = extractHeaderString(headers, headerKey);
        return Boolean.parseBoolean(value);
    }

}
