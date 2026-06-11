package noTrigger.core;

import noTrigger.config.DirectoryScanConfig;

public interface StreamNewOrModifiedFilesUseCase {
    void streamFromDirectory(DirectoryScanConfig directoryScanConfig);
}
