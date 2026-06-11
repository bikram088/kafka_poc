package POC.statemanagement;

import java.util.List;

public class DirectoryState {
    private String monitor;
    private List<FilterState> filters;

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public List<FilterState> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterState> filters) {
        this.filters = filters;
    }
}
