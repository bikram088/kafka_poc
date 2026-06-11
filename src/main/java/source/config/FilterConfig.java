package POC.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FilterConfig {
    private static final Logger log = LogManager.getLogger(FilterConfig.class);

    private String trigger;
    private List<String> dataFiles;
    private boolean streamTriggerFile = false;
    private String kafkaTopic;
    private boolean latest = true;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public List<String> getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(List<String> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public boolean isStreamTriggerFile() {
        return streamTriggerFile;
    }

    public void setStreamTriggerFile(boolean streamTriggerFile) {
        this.streamTriggerFile = streamTriggerFile;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }
}
