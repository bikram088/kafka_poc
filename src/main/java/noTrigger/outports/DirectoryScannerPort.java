package noTrigger.outports;

import noTrigger.config.DirectoryScanConfig;
import noTrigger.core.ScannedFile;

import java.time.Instant;
import java.util.List;

public interface DirectoryScannerPort {
    List<ScannedFile> scan(DirectoryScanConfig directoryScanConfig, Instant fromTime, Instant toTime);
}
