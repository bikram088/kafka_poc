package sink.config;

import java.util.List;

public class SinkConfigs {
    private List<SinkConfig> rules;
    private AppConsumerConfig consumer;

    public List<SinkConfig> getRules() {
        return rules;
    }

    public void setRules(List<SinkConfig> rules) {
        this.rules = rules;
    }

    public AppConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(AppConsumerConfig consumer) {
        this.consumer = consumer;
    }
}
