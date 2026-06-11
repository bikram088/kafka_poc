package POC.context;

import jakarta.servlet.FilterConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileProcessingContext {
    private final Path triggerFile;
    private final List<FilterConfig> rules;
    private int matchingRuleIndex;
    private final List<Path> expectedFiles;
    private final List<Path> actualFiles;
    private List<FileStreamingContext> streamingContexts;

    public FileProcessingContext(Path triggerFile, List<FilterConfig> rules) {
        this.triggerFile = triggerFile;
        this.rules = rules;
        this.expectedFiles = new ArrayList<>();
        this.actualFiles = new ArrayList<>();
    }

    public Path getTriggerFile() {
        return triggerFile;
    }

    public List<FilterConfig> getRules() {
        return rules;
    }

    public int getMatchingRuleIndex() {
        return matchingRuleIndex;
    }

    public void setMatchingRuleIndex(int matchingRuleIndex) {
        this.matchingRuleIndex = matchingRuleIndex;
    }

    public List<Path> getExpectedFiles() {
        return expectedFiles;
    }

    public List<Path> getActualFiles() {
        return actualFiles;
    }

    public List<FileStreamingContext> getStreamingContexts() {
        return streamingContexts;
    }

    public void setStreamingContexts(List<FileStreamingContext> streamingContexts) {
        this.streamingContexts = streamingContexts;
    }
}
