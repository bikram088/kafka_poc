package POC.monitoring;

import jakarta.servlet.FilterConfig;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class FileFilter {
    public static IOFileFilter fromConfig(List<FilterConfig> cfgs, long pollIntervalMs, DirectoryState dirState){
        OrFileFilter composite = new OrFileFilter();
        for(FilterConfig cfg: cfgs){
            AndFileFilter and = new AndFileFilter();
            and.addFileFilter(new RegexFileFilter(cfg.getTrigger()));

            FilterState filterState = dirState.getFilters().stream
                    .filter(xyz -> xyz.getTrigger().equals(cfg.getTrigger()))
                    .findFirst()
                    .orElse(null);
            and.addFileFilter(new DynamicAgeFilter(cfg, pollIntervalMs, filterState));
            composite.addFileFilter(and);
        }
        return composite;
    }
}
