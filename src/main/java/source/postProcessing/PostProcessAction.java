package POC.postProcessing;

import POC.context.FileProcessingContext;

public interface PostProcessAction {
    void execute(FileProcessingContext context);
}
