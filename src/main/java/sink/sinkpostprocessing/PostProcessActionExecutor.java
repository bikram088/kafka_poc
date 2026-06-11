package sink.sinkpostprocessing;

import sink.eventprocessing.FileMergeContext;

import java.util.List;

public class PostProcessActionExecutor {
    List<PostProcessAction> actions;

    public PostProcessActionExecutor(List<PostProcessAction> actions){
        this.actions = actions;
    }

    public void executeAll(FileMergeContext context){
        actions.forEach(actions -> {
            actions.execute(context);
        });
    }
}
