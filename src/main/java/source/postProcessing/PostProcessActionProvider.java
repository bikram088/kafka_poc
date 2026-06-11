package POC.postProcessing;

import POC.config.PostProcessActionConfig;
import POC.statemanagement.DirectoryState;

import java.util.ArrayList;
import java.util.List;

public class PostProcessActionProvider {
    public static List<PostProcessAction> fromConfig(List<PostProcessActionConfig> config, DirectoryState dirState){
        List<PostProcessAction> postProcessingActions = new ArrayList<>();
        postProcessingActions.add(new AppStateSaveAction(dirState));
        if(config == null){
            return postProcessingActions;
        }
        config.forEach(cfg -> {
            switch(cfg.getType()){
                case: "moveFile":
                    postProcessingActions.add(new FileMoveAction(
                            (String)cfg.getParams().get("finished"),
                            (String)cfg.getParams().get("error")
                            ));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action type: "+ cfg.getType());
            }
        });
        return  postProcessingActions;
    }
}
