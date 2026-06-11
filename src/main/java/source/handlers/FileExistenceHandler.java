package POC.handlers;

import POC.context.FileProcessingContext;

import java.nio.file.Files;

public class FileExistenceHandler extends FileProcessingHandler{
    @Override
    public boolean process(FileProcessingContext context){
        context.getExpectedFiles().forEach(file -> {
            if(Files.exists(file)){
                context.setActualFiles(Files);
            }else{
                log.error("path doesn;t exist");
            }
        });
        if(context.getActualFiles().size() != context.getExpectedFiles().size()){
            log.infor("Partial/No files found");
            return false;
        }
        log("found all actual files");
        return true;
    }
}
