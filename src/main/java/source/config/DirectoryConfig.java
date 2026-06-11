package POC.config;

import java.util.List;

public class DirectoryConfig {

    private String monitor;
    private int poll = 100;
    private List<FilterConfig> filters;
    private List<PostProcessActionConfig> postProcessActions;

    public DirectoryConfig(){}

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public int getPoll() {
        return poll;
    }

    public void setPoll(int poll) {
        this.poll = poll;
    }

    public List<FilterConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterConfig> filters) {
        this.filters = filters;
    }

    public List<PostProcessActionConfig> getPostProcessActions() {
        return postProcessActions;
    }

    public void setPostProcessActions(List<PostProcessActionConfig> postProcessActions) {
        this.postProcessActions = postProcessActions;
    }
}
