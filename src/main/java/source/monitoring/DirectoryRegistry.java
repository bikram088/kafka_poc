package POC.monitoring;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryRegistry {
    private static final Logger log = (Logger) LogManager.getLogger(DirectoryRegistry.class);

    private final List<FileAlterationMonitor> directoryMonitors = new ArrayList<>();
    private DirectoryRegistry(){}

    public static DirectoryRegistry initialize(List<DirectoryConfig> directoryConfigs, AppState appState) throws IOException {
        return new DirectoryRegistory().initializeDirectoryMonitors(directoryConfigs, appState);
    }

    private DirectoryRegistry initializeDirectoryMonitors(List<DirectoryConfig> directoryConfigs,AppState appState) thows IOException{
        for(DirectoryConfig dirConfig : directoryConfigs){
            DirectoryState dirState = appState.getDirectories()
                    .stream()
                    .filter(monitor -> monitor.getMonitor().equals(dirConfigs.getMonitor()))
                    .findFirst()
                    .orElse(null);
            FileAlterationMonitor initializedDirectoryMonitor = DirectoryMonitorBuilder.withConfig(dirConfig, dirState).build();
            this.directoryMonitors.add(initializedDirectoryMonitor);
            log.info();
        }
        return this;
    }

    public void scanDirectory() throws Exception{
        for(FileAlterationMonitor dirMon: this.directoryMonitors){
            dirMon.getObservers().forEach(FileAlterationObserver::checkAndNotify);
            log.info();
        }
    }
    public void startPolling() throws Exception{
        for(FileAlterationMonitor dirMon: this.directoryMonitors){
            dirMon.start();
            log.info();
        }
    }
    public void stopPolling() throws Exception{
        for(FileAlterationMonitor dirMon: this.directoryMonitors){
            dirMon.stop();
            log.info();
        }
    }

    public List<FileAlterationMonitor> getDirectoryMonitors(){
        return directoryMonitors;
    }
}
