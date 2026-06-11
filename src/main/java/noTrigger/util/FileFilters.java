package noTrigger.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FileFilters {
    private FileFilters(){}

    public static Predicate<Path> onlyFiles(){
        return Files::isRegularFile;
    }

    public static Predicate<Path> createdOrModifiedBetween(Instant from, Instant to){
        return path -> {
            try {
                Instant mtime = Files.getLastModifiedTime(path).toInstant();
                return mtime.compareTo(from) >= 0 && mtime.compareTo(to) < 0;
            } catch (IOException e) {
                return false;
            }
        };
    }

    public static Predicate<Path> olderThan(Duration duration){
        Instant threshold = Instant.now().minus(duration);
        return path -> {
            try{
                return Files.getLastModifiedTime(path).toInstant().isBefore(threshold);
            }catch (IOException e){
                return false;
            }
        };
    }

    public static Predicate<Path> filenameMatches(String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return path -> pattern.matcher(path.getFileName().toString()).matches();
    }

    public static Predicate<Path> filenameMatches(Pattern pattern){
        return path -> pattern.matcher(path.getFileName().toString()).matches();
    }
}
