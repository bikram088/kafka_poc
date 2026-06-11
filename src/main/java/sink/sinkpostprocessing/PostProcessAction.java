package sink.sinkpostprocessing;

import sink.eventprocessing.FileMergeContext;

public interface PostProcessAction {
    void execute (FileMergeContext context);
}
