package source.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import source.config.api.HostNameIpAddressResolver;
import source.config.api.SourceConfigApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final Logger log = LogManager.getLogger(ConfigLoader.class);
    private static ConfigLoader configLoader;

    private static final Path appConfigFile = Paths.get(SystemProperty.APP_CONFIG_FILE.getValue());
    private static final Path appDataDir = Paths.get(SystemProperty.APP_DATA_DIR.getValue());
    private static final Path producerKafkaConfigFile = Paths.get(SystemProperty.PRODUCER_KAFKA_CONFIG_FILE.getValue());
    private static final Path apiResponseJsonPath = appDataDir.resolve("api-response.json");
    private static final Path apiResponseYmlPath = appDataDir.resolve("api-response.yml");

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(
            YAMLFactory.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build()
    );

    private SourceConfig sourceConfig;
    private String apiResponseJson;

    public static ConfigLoader getOrSetConfig() throws Exception {
        ConfigLoader loader = new ConfigLoader();
        String hostname = HostNameIpAddressResolver.resolveHostName(null);
        String ipAddress = HostNameIpAddressResolver.resolveIpAddress(null);

        log.info("Resolved hostname: {}, IP address: {}", hostname, ipAddress);

        Map<String, String> configs = SourceConfigApiClient.loadSourceConfig(hostname, ipAddress);
        loader.apiResponseJson = configs.get("json");
        loader.sourceConfig = YAML_MAPPER.readValue(configs.get("yaml"), SourceConfig.class);
        return loader;
    }

    public void persistConfig() throws IOException {
        String yaml = YAML_MAPPER.writeValueAsString(sourceConfig);
        Files.write(apiResponseJsonPath, apiResponseJson.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(apiResponseYmlPath, yaml.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("Persisted validated config to {}", appDataDir);
    }

    private ConfigLoader(Path configFile) throws IOException {
        InputStream configFileStream = Files.newInputStream(configFile);
        this.sourceConfig = loadConfig(configFileStream);
    }

    public SourceConfig loadConfig(InputStream configAsStream) {
        Constructor constructor = new Constructor(SourceConfig.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        return yaml.load(configAsStream);
    }

    public void dumbConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
    }

    public static void setConfigLoader(ConfigLoader configLoader) {
        ConfigLoader.configLoader = configLoader;
    }

    public SourceConfig getSourceConfig() {
        return sourceConfig;
    }

    public static Path getAppConfigFile() {
        return appConfigFile;
    }

    public static Path getProducerKafkaConfigFile() {
        return producerKafkaConfigFile;
    }

    public static Path getAppDataDir() {
        return appDataDir;
    }
}
