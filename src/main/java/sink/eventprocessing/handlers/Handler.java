package sink.eventprocessing.handlers;

import sink.eventprocessing.FileMergeContext;

import java.io.FileNotFoundException;

public abstract class Handler {
    private Handler next;
    public Handler setNext(Handler next){
        this.next = next;
        return next;
    }
    public void handle(FileMergeContext context) {
        boolean shouldContinue = process(context);
        if(shouldContinue && next != null){
            next.handle(context);
        }
    }

    public abstract boolean process(FileMergeContext context) throws FileNotFoundException;
}
