package noTrigger.outports;

import noTrigger.config.DirectoryScanConfig;
import noTrigger.core.ScannedFile;
import noTrigger.util.FileFilters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DirectoryScannerAdaptor implements DirectoryScannerPort{
    private final Logger log = LogManager.getLogger(DirectoryScannerAdaptor.class);


    @Override
    public List<ScannedFile> scan(DirectoryScanConfig directoryScanConfig, Instant fromTime, Instant toTime) {
        List<ScannedFile> result = new ArrayList<>();

        Predicate<Path> filterPredicates = FileFilters.onlyFiles()
                .and(FileFilters.createdOrModifiedBetween(fromTime,toTime))
                .and(FileFilters.olderThan(Duration.ofSeconds(directoryScanConfig.getMinAgeSeconds())))
                .and(FileFilters.filenameMatches(directoryScanConfig.getFileNamePattern()));

        try(Stream<Path> stream = Files.walk(directoryScanConfig.getDirectoryPath())){
            stream.filter(filterPredicates)
                    .forEach(
                            path -> {
                                try{
                                    Instant mtime = Files.getLastModifiedTime(path).toInstant();
                                    long size = Files.size(path);
                                    result.add(new ScannedFile(path, mtime, size));
                                }catch (IOException e){
                                    log.error("Error reading last modified or size of the file. {}", path, e);
                                }
                            }
                    );
        }catch (IOException e){
            log.error("Failed to scan directory: {}", directoryScanConfig.getDirectoryPath(), e);
            throw new RuntimeException("Failed to scan directory: " + directoryScanConfig.getDirectoryPath(), e);
        }
        result.sort(Comparator.comparing(ScannedFile::getLastModifiedTime));
        return result;
    }
}
