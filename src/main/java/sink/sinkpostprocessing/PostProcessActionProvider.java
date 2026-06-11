package sink.sinkpostprocessing;

import sink.config.SinkConfig;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PostProcessActionProvider {
    public static List<PostProcessAction> fromConfig(SinkConfig config){
        List<PostProcessAction> postProcessActions = new ArrayList<>();

        if(config == null){
            return postProcessActions;
        }
        postProcessActions.add(new DefaultFileMoveAction(Paths.get(config.getTargetDir())));
        return postProcessActions;
    }
}
