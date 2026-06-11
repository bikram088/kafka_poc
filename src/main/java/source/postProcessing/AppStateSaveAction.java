package POC.postProcessing;

import POC.context.FileProcessingContext;
import POC.statemanagement.DirectoryState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class AppStateSaveAction implements PostProcessAction{

    private final DirectoryState directoryState;

    public AppStateSaveAction(DirectoryState directoryState) {
        this.directoryState = directoryState;
    }

    @Override
    public void execute(FileProcessingContext context) {
        try{
            BasicFileAttributes fileAttributes = Files.readAttributes(context.getTriggerFile(), BasicFileAttributes.class);
            OffsetDateTime fileLastModified = fileAttributes.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
            directoryState.getFilters()
                    .get(context.getMatchingRuleIndex())
                    .setTriggerFileLastModified(fileLastModified);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
