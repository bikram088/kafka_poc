package POC.postProcessing;

import POC.context.FileProcessingContext;

public class PostProcessActionExecutor {
    List<PostProcessAction> actions;
    public PostProcessActionExecutor(List<PostProcessAction> actions){
        this.actions = actions;
    }

    public void executeAll(FileProcessingContext context){
        actions.forEach(action -> action.execute(context));
    }
}
