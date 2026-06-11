package source.config;

import java.util.List;

public class SourceConfig {
    private String kafkaTopic;
    private List<POC.config.DirectoryConfig> directories;

    public SourceConfig(){
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public List<POC.config.DirectoryConfig> getDirectories() {
        return directories;
    }

    public void setDirectories(List<POC.config.DirectoryConfig> directories) {
        this.directories = directories;
    }
}
