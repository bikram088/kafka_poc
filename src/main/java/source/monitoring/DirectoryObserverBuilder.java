package POC.monitoring;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import java.io.IOException;

public class DirectoryObserverBuilder {
    private static final Logger log = (Logger) LogManager.getLogger(DirectoryObserverBuilder.class);
    private FileAlterationObserver observer;
    private IOFileFilter triggerFileFilters;
    private DirectoryEventListener directoryEventListener;

    private DirectoryObserverBuilder() {
    }

    public static DirectoryObserverBuilder withConfig(
            FileEntry dirToMonitor,
            List<FilterConfig> fileFilters,
            long pollIntervalsMs,
            List<PostPRocessActionConfig> postActionConfigs,
            DirectoryState dirState) throws IOException {
        return new DirectoryObserverBuilder()
                .createDirectoryEventListener(fileFilters, postActionConfigs, dirState)
                .createTriggerFileFilters(fileFilters, pollIntervalsMs, dirState)
                .createObserverAndAttachListener(dirToMonitor);
    }

    privateDirectoryObserverBuilder createTriggerFileFilters(List<FilterConfigs> fileFilters, long pollIntervalsMs, DirectoryState dirState) {
        this.triggerFileFilters = FileFilters.fromConfig(fileFilters, pollIntervalsMs, dirState);
        return this;
    }

    private DirectoryObserverBuilder createObserverAndAttachListener(FileEntry dirToMonitor) throws IOException {
        this.observer = FileAlterationObserver.build()
                .setFileFilter(triggerFileFilters)
                .setRootEntry(dirToMonitor)
                .get();

        this.observer.addListener(this.directoryEventListener);
        return this;
    }

    private DirectoryObserverBuilder createDirectoryEventListener(
            List<FilterConfig> fileFilters,
            List<PostProcessActionConfig> postActionConfigs,
            DirectoryState dirState) {
        this.directoryEventListener = new DirectoryEventListener(fileFilters, PostProcessActionProvider.fromConfig(postActionConfigs, dirState));
        return this;
    }

    public FileAlterationObserver build(){
        return this.observer;

}

}
