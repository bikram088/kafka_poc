    package POC.handlers;

    import POC.context.FileProcessingContext;
    import POC.context.FileStreamingContext;
    import org.apache.kafka.common.header.internals.RecordHeaders;

    import java.io.IOException;
    import java.nio.file.Path;
    import java.util.ArrayList;
    import java.util.List;

    public class FileStreamingContextBuilderHandler extends FileProcessingHandler{
        @Override
        public boolean process(FileProcessingContext context) {
            List<FileStreamingContext> streamingContexts = new ArrayList<>();

            for(Path file : context.getActualFiles()){
                FileStreamingContext streamingContext = new FileStreamingContext();

                streamingContext.setFile(file);
                streamingContext.setTopic(context.getRules().get(context.getMatchingRuleIndex()).getKafkaTopic());

                try{
                    streamingContext.setHeaders(FileMetadataHeader.fromFilePath(file));
                }catch (IOException e){
                    return false;
                }
                if(file.equals(context.getTriggerFile())){
                    streamingContext.setHeaders(new RecordHeaders().add("file.trigger", String.valueOf(true).getBytes()));
                }else{
                    streamingContext.setHeaders(new RecordHeaders().add("file.trigger", String.valueOf(false).getBytes()));
                }
                streamingContexts.add(streamingContext);
            }
            context.setStreamingContexts(streamingContexts);
            return true;
        }
    }
