package POC.handlers;

import POC.context.FileProcessingContext;

public abstract class FileProcessingHandler {
    private FileProcessingHandler next;
    public FileProcessingHandler setNext(FileProcessingHandler next){
        this.next = next;
        return next;
    }

    public void handle(FileProcessingContext context){
        boolean shouldContinue = process(context);
        if(shouldContinue && next != null){
            next.handle(context);
        }
    }
    public abstract boolean process(FileProcessingContext context);
}
