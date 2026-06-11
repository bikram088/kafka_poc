package sink.config;

import jakarta.validation.constraints.NotBlank;

public class SinkConfig {

    @NotBlank(message = "Topic must not be blank")
    private String topic;

    @NotBlank(message = "At least one trigger must be provided")
    private String filePattern;
    private String targetDir;

    public SinkConfig(){

    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
