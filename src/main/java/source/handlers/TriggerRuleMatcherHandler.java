package POC.handlers;

import POC.context.FileProcessingContext;

import java.util.regex.Pattern;

public class TriggerRuleMatcherHandler {
    public boolean process(FileProcessingContext context){
        boolean matched = false;
        for(int i = 0; i < context.getRules().size(); i++){
            String regex = context.getRules().get(i).getTrigger();
            String fileName = context.getTriggerFile().getFileName().toString();
            if(Pattern.compile(regex).matcher(fileName).matches()){
                context.setMatchingRuleIndex(i);
                matched = true;
                break;
            }
        }
        return matched;
    }
}
