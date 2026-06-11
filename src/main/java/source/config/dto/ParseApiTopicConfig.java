package source.config.dto;

import java.util.List;

public class ParseApiTopicConfig {
    private int id;
    private String topicName;
    private String active;
    private String kafkaStatus;
    private List<ParseApiSinkConfig> sinks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getKafkaStatus() {
        return kafkaStatus;
    }

    public void setKafkaStatus(String kafkaStatus) {
        this.kafkaStatus = kafkaStatus;
    }

    public List<ParseApiSinkConfig> getSinks() {
        return sinks;
    }

    public void setSinks(List<ParseApiSinkConfig> sinks) {
        this.sinks = sinks;
    }
}
