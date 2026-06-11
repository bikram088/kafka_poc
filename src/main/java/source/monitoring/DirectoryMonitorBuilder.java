package source.monitoring;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DirectoryMonitorBuilder {
    private static final Logger log = (Logger) LogManager.getLogger(DirectoryMonitorBuilder.class);

    private FileAlterationMonitor monitor;
    private FileAlterationObserver observer;
    private DirectoryMonitorBuilder(){}

    public static DirectoryMonitorBuilder withConfig(POC.config.DirectoryConfig dirConfig, POC.statemanagement.DirectoryState dirState) throws IOException {
        FileEntry rootDirToMonitor = new FileEntry(new File(dirConfig.getMonitor()));

        return new DirectoryMonitorBuilder()
                .createObserver(rootDirToMonitor, dirConfig.getFilters(), dirConfig.getPoll(), dirConfig.getPostProcessActions, dirState)
                .createDirectoryMonitor(dirConfig.getPoll());
    }

    private DirectoryMonitorBuilder createDirectoryMonitor(long interval){
        this.monitor = new FileAlterationMonitor(interval, this.observer);
        return this;
    }

    private DirectoryMonitorBuilder createObserver(
            FileEntry dirToMonitor, List<POC.config.FilterConfig> fileFilters, long pollIntervalsMs,
            List<POC.config.PostProcessActionConfig> postActionConfigs, POC.statemanagement.DirectoryState dirState) throw IOException{
        this.observer  = POC.monitoring.DirectoryObserverBuilder.withConfig(dirToMonitor, fileFilters, pollIntervalMs, postActionConfigs, dirState).build();
    }
    public FileAlterationMonitor build(){
        return monitor;
    }
}
