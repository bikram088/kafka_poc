package sink.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.List;

public class YamlSinkConfig implements SinkRules{
    private static SinkConfigs sinkConfigs;

    public YamlSinkConfig() throws IOException {
        this.sinkConfigs = loadConfig();
    }

    private SinkConfigs loadConfig(){
        InputStream configFileStream = Files.newInputStream(sinkAppConfigFile);
        Constructor constructor = new Constructor(SinkConfigs.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        return yaml.load(configFileStream);
    }

    @Override
    public List<SinkConfig> getAll() {
        return null;
    }

    @Override
    public String toString() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        return yaml.dump(sinkConfigs);
    }

    public static AppConsumerConfig getConsumerConfig(){
        return sinkConfigs.getConsumer();
    }
}
