package noTrigger.outports;

import java.time.Instant;
import java.util.Optional;

public interface ScanStateRepository {
    Optional<Instant> loadLastTimestamp(String directoryId);
    void saveLastTimestamp(String directoryId, Instant timestamp);
}
