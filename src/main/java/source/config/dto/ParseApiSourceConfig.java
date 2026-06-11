package source.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParseApiSourceConfig {
    private int id;
    private String ipAddress;
    private String appUser;
    private String absoluteSourceDirPath;
    private String hostname;
    private String filePattern;
    private String streamType;
    private String active;
    private String department;
    private String segment;
    private String triggerDataFilePattern;
    private String triggerPoll;
    private String triggerFileStream;
    private String triggerAbsoluteSuccessDirPath;
    private String triggerAbsoluteFailurePath;
    private ParseApiTopicConfig topic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public String getAbsoluteSourceDirPath() {
        return absoluteSourceDirPath;
    }

    public void setAbsoluteSourceDirPath(String absoluteSourceDirPath) {
        this.absoluteSourceDirPath = absoluteSourceDirPath;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getTriggerDataFilePattern() {
        return triggerDataFilePattern;
    }

    public void setTriggerDataFilePattern(String triggerDataFilePattern) {
        this.triggerDataFilePattern = triggerDataFilePattern;
    }

    public String getTriggerPoll() {
        return triggerPoll;
    }

    public void setTriggerPoll(String triggerPoll) {
        this.triggerPoll = triggerPoll;
    }

    public String getTriggerFileStream() {
        return triggerFileStream;
    }

    public void setTriggerFileStream(String triggerFileStream) {
        this.triggerFileStream = triggerFileStream;
    }

    public String getTriggerAbsoluteSuccessDirPath() {
        return triggerAbsoluteSuccessDirPath;
    }

    public void setTriggerAbsoluteSuccessDirPath(String triggerAbsoluteSuccessDirPath) {
        this.triggerAbsoluteSuccessDirPath = triggerAbsoluteSuccessDirPath;
    }

    public String getTriggerAbsoluteFailurePath() {
        return triggerAbsoluteFailurePath;
    }

    public void setTriggerAbsoluteFailurePath(String triggerAbsoluteFailurePath) {
        this.triggerAbsoluteFailurePath = triggerAbsoluteFailurePath;
    }

    public ParseApiTopicConfig getTopic() {
        return topic;
    }

    public void setTopic(ParseApiTopicConfig topic) {
        this.topic = topic;
    }
}
