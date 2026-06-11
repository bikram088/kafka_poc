package sink.eventprocessing.handlers;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class EventFilePatternHandler extends Handler{
    private static final Logger log = LogManager.getLogger(EventFilePatternHandler.class);

    @Override
    public boolean process(FileMergeContext context) {
        boolean matched = false;

        List<SinkConfig> rules = context.getRules().getAll();
        for(int i = 0; i < rules.size(); i++){
            String regex = rules.get(i).getFilePattern();
            String fileName = extractHeaderString(context.getRecord().headers(), "file.name");
            if(Pattern.compile(regex).matcher(fileName).matches()){
                context.setMatchingRuleIndex(i);
                matched = true;
                log.info("[MATCHED] event file name");
                break;
            }
        }
        return matched;
    }

    public String extractHeaderString(Headers headers, String headerKey){
        Header header = headers.lastHeader(headerKey);
        if(header == null){
            return null;
        }
        return new String(header.value());
    }

}
