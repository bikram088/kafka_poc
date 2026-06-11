package POC.statemanagement;

import POC.config.ConfigLoader;
import POC.config.SourceConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class YamlStateStore implements AppStateStoreListener{

    private final SourceConfig sourceConfig;
    private final AppState appState;

    private final Path yamlAppStatePath = ConfigLoader.getAppDataDir().resolve("state.yml");

    public YamlStateStore(SourceConfig sourceConfig, AppState appState) {
        this.sourceConfig = sourceConfig;
        this.appState = appState;
    }

    public List<DirectoryState> initializeStateFromSourceConfig(){
        return sourceConfig.getDirectories()
                .stream()
                .map(directoryConfig -> {
                    DirectoryState dirState = new DirectoryState();
                    dirState.setMonitor(directoryConfig.getMonitor());

                    List<FilterState> filterStates = directoryConfig.getFilters()
                            .stream()
                            .map(filterConfig -> {
                                FilterState filterState = new FilterState();
                                filterState.setTrigger(filterConfig.getTrigger());
                                return filterState;
                            }).collect(Collectors.toList());
                    dirState.setFilters(filterStates);
                    return dirState;
                }).collect(Collectors.toList());
    }

    public AppState loadStateFromFile() throws IOException{
        AppState appStateFromFile = AppState();

        try(InputStream stateStream = Files.newInputStream(yamlAppStatePath)){
            LoaderOptions loaderOptions = new LoaderOptions();
            Yaml yaml = new Yaml(new OffsetDateTimeConstructor(AppState.class, loaderOptions));
            appStateFromFile = yaml.loadAs(stateStream, AppState.class);
        }catch (IOException e){
            throw new IOException("Unable to read state file...");
        }
        return appStateFromFile;
    }

    public void copyAppState(AppState target, AppState source){
        target.getDirectories().forEach(
                targetDir -> source.getDirectories()
                        .stream()
                        .filter(sourceDir -> sourceDir.getMonitor().equals(targetDir.getMonitor()))
                        .findFirst()
                        .ifPresent(
                                sourceDir -> targetDir.getFilters().forEach(
                                        targetDir -> sourceDir.getFilters()
                                                .stream()
                                                .filter(sourceFilter -> sourceFilter.getTrigger().equals(targetFilter.getTrigger()))
                                                .findFirst()
                                                .ifPresent(
                                                        sourceFilter -> {
                                                            try{
                                                                targetFilter.setTriggerFileLastModified(sourceFilter.getTriggerFileLastModified());
                                                            }catch (IOException e){
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                )
                                )
                        )
        );
    }

    @Override
    public void loadState() throws IOException {
        appState.setDirectories(initializeStateFromSourceConfig());
        if(Files.exists(yamlAppStatePath)){
            AppState appStateFromFile = loadStateFromFile();
            copyAppState(appState, appStateFromFile);
        }
    }

    @Override
    public void saveState() throws IOException {
        try(Writer yamlWriter = new FileWriter(yamlAppStatePath.toString())){
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Representer representer = new OffsetDateTimeRepresenter(dumperOptions);
            representer.addClassTag(AppState.class, Tag.MAP);
            Yaml yamlAppState = new Yaml(new Constructor(new LoaderOptions()), representer, dumperOptions);
            yamlAppState.dump(appState, yamlWriter);
        }catch (IOException e){
            log.error("Error while writing yaml state", e);
        }
    }

    @Override
    public void dumpState() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new OffsetDateTimeRepresenter(dumperOptions);
        representer.addClassTag(AppState.class, Tag.MAP);
        Yaml yamlAppState = new Yaml(new Constructor(new LoaderOptions()), representer, dumperOptions);
        log.info(yamlAppState.dump(appState));
    }
}
