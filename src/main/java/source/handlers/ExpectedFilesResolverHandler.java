package POC.handlers;

import POC.context.FileProcessingContext;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpectedFilesResolverHandler extends FileProcessingHandler {
    @Override
    public boolean process(FileProcessingContext context){
        String triggerFileName = context.getTriggerFile().getFileName().toString();
        String triggerFileRegex = context.getRules().get(context.getMatchingRuleIndex()).getTrigger();
        Pattern triggerFilePattern = Pattern.compile(triggerFileRegex);
        Matcher triggerMatcher = triggerFilePattern.matcher(triggerFileName);

        if(!triggerMatcher.matches()){
            log.info("Unable to resolve expected data files");
            return false;
        }
        Path triggerParentPath = context.getTriggerFile().getParent();

        for (String template: context.getRules().get(context.getMatchingRuleIndex()).getDataFiles()){
            Path dataFile = triggerParentPath.resolve(triggerMatcher.replaceAll(template));
            context.setExpectedFiles(dataFile);
        }
        if(context.getRules().get(context.getMatchingRuleIndex()).isStreamTriggerFile()){
            context.setExpectedFiles(context.getTriggerFile());
        }
        return true;
    }
}
