package POC;

import POC.config.ConfigLoader;
import jakarta.validation.*;
import POC.monitoring.DirectoryRegistry;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import source.config.ConfigLoader;

public class SourceApp {
    private static final Logger log = (Logger) LogManager.getLogger(SourceApp.class);
    public static void main(String[] args) throws Exception {
        
        ConfigLoader configLoader = ConfigLoader.getOrSetConfig();
        configLoader.getSourceConfig().applyDefaults();
        configLoader.dumpConfig();

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<ConfigLoader>> violations = validator.validate(configLoader);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<ConfigLoader> violation : violations) {
                    log.error("");
                }
                throw new IllegalStateException("");
            }
        }

        configLoader.persistConfig();

        POC.statemanagement.AppState appState = new AppState();
        AppStateStoreListener yamlAppStateStore = new YamlStateStore(
                configLoader.getSourceConfig(),
                appState
        );

        yamlAppStateStore.loadState();

        appState.getDirectories().forEach(dirState -> {
            dirState.getFilters().forEach(filterState -> {
                filterState.addListener(yamlAppStateStore);
            });
        });

        ProducerCache.getOrCreate();

        DirectoryRegistry directoryRegistry = DirectoryRegistry.initialize(configLoader.getSourceConfig().getDirectories(),
                appState);
        directoryRegistry.scanDirectory();
        directoryRegistry.startPolling();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                directoryRegistry.stopPolling();
            } catch (Exception e) {
                log.error(e);
            }
        }));
    }
}
