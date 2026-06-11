package sink;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SinkBootstrap {
    public static final Path sinkAppConfigFile = Paths.get(SinkSystemProperty.SINK_APP_CONFIG_FILE.getValue());
    public static final Path consumerKafkaConfigFile = Paths.get(SinkSystemProperty.CPONSUMER_KAFKA_CONFIG_FILE.getValue());
    public static final Path sinkAppDataDir = Paths.get(SinkSystemProperty.SINK_APP_DATA_DIR.getValue());
}
