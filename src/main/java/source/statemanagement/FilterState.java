package POC.statemanagement;

import org.apache.logging.log4j.LogManager;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class FilterState implements AppStateObserver{
    private static final Logger log = LogManager.getLogger(FilterState.class);
    private final List<AppStateStoreListener> appStateStoreListenerList = new ArrayList<>();

    private String trigger;
    private String triggerFile;
    private OffsetDateTime triggerFileLastModified;
    private String dataFile;
    private OffsetDateTime dataFileLastModified;

    public List<AppStateStoreListener> getAppStateStoreListenerList() {
        return appStateStoreListenerList;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTriggerFile() {
        return triggerFile;
    }

    public void setTriggerFile(String triggerFile) {
        this.triggerFile = triggerFile;
    }

    public OffsetDateTime getTriggerFileLastModified() {
        return triggerFileLastModified;
    }

    public void setTriggerFileLastModified(OffsetDateTime triggerFileLastModified) {
        this.triggerFileLastModified = triggerFileLastModified;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public OffsetDateTime getDataFileLastModified() {
        return dataFileLastModified;
    }

    public void setDataFileLastModified(OffsetDateTime dataFileLastModified) {
        this.dataFileLastModified = dataFileLastModified;
    }
}
