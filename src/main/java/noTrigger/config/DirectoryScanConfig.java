package noTrigger.config;

import java.nio.file.Path;
import java.time.Instant;

public class DirectoryScanConfig {
    private Path directoryPath;
    private String fileNamePattern;
    private long initialDelaySeconds;
    private long frequencyMinutes;
    private long minAgeSeconds;
    private String kafkaTopic;
    private Instant fromTime;
    private Instant toTime;
    private String timeUnitAsString = "MINUTES";

    public DirectoryScanConfig(){}

    public DirectoryScanConfig(Path directoryPath, String fileNamePattern, long initialDelaySeconds, long frequencyMinutes, long minAgeSeconds, String kafkaTopic, Instant fromTime, Instant toTime, String timeUnitAsString) {
        this.directoryPath = directoryPath;
        this.fileNamePattern = fileNamePattern;
        this.frequencyMinutes = frequencyMinutes;
        this.minAgeSeconds = minAgeSeconds;
        this.kafkaTopic = kafkaTopic;
    }

    public Path getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public long getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public void setInitialDelaySeconds(long initialDelaySeconds) {
        this.initialDelaySeconds = initialDelaySeconds;
    }

    public long getFrequencyMinutes() {
        return frequencyMinutes;
    }

    public void setFrequencyMinutes(long frequencyMinutes) {
        this.frequencyMinutes = frequencyMinutes;
    }

    public long getMinAgeSeconds() {
        return minAgeSeconds;
    }

    public void setMinAgeSeconds(long minAgeSeconds) {
        this.minAgeSeconds = minAgeSeconds;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public Instant getFromTime() {
        return fromTime;
    }

    public void setFromTime(Instant fromTime) {
        this.fromTime = fromTime;
    }

    public Instant getToTime() {
        return toTime;
    }

    public void setToTime(Instant toTime) {
        this.toTime = toTime;
    }

    public String getTimeUnitAsString() {
        return timeUnitAsString;
    }

    public void setTimeUnitAsString(String timeUnitAsString) {
        this.timeUnitAsString = timeUnitAsString;
    }
}
