package POC.monitoring;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class DynamicAgeFilter implements IOFileFilter {
    private final FilterConfig config;
    private final long pollIntervalsMs;
    private final FilterState filterState;

    public DynamicAgeFilter(FilterConfig config, long pollIntervalsMs, FilterState filterState){
        this.config = config;
        this.filterState = filterState;
        this.pollIntervalsMs = pollIntervalsMs;
    }

    @Override
    public boolean accept(File file) {
        long cutOff;
        if(config.isLatest()){
            cutOff = System.currentTimeMillis() - pollIntervalsMs;
        } else {
            if(filterState.getTriggerFileLastModified() == null){
                cutOff = 0L;
            } else {
                cutOff = filterState.getTriggerFileLastModified().toInstant().toEpochMilli();
            }
        }
        return file.lastModified() > cutOff;
    }

    @Override
    public boolean accept(File file, String s) {
        return false;
    }
}
