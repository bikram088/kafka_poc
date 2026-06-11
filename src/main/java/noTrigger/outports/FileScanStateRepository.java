package noTrigger.outports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.datetime.standard.InstantFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FileScanStateRepository implements ScanStateRepository{
    private final Logger log = LogManager.getLogger(FileScanStateRepository.class);

    private static final String STATE_FILE = "scan_state.properties";
    private final ConcurrentHashMap<String, Instant> timeStampMap = new ConcurrentHashMap<>();
    private final Object fileLock = new Object();

    public FileScanStateRepository(){
        loadFromFile();
    }

    @Override
    public Optional<Instant> loadLastTimeStamp(String directoryId){
        return Optional.ofNullable(timeStampMap.get(directoryId));
    }

    @Override
    public void saveLastTimeStamp(String directoryId, Instant timestamp){
        timestamp.put(directoryId, timestamp);
        persistToFile();
    }

    private void loadFromFile(){
        log.info("Loading state from file...");
        Path path = Paths.get(STATE_FILE);
        if(!Files.exists(path)){
            log.info("No state file exists");
            return;
        }
        Properties props = new Properties();
        try(InputStream is = Files.newInputStream(path)){
            props.load(is);
            for(String key : props.stringPropertyNames()){
                Instant ts = Instant.parse(props.getProperty(key));
                timeStampMap.put(key, ts);
            }
        }catch (IOException e){
            log.error("Error Loading state", e);
        }
    }

    private void persistToFile(){
        synchronized (fileLock){
            log.info("Saving state file...");
            Properties props = new Properties();
            timeStampMap.forEach((key, ts) ->
                    props.setProperty(key, InstantFormatter.write(ts)));

            try(OutputStream os = Files.newOutputStream(Paths.get(STATE_FILE))){
                props.store(os, "last scan timestamps");
                log.info("Saved state file");
            }catch (IOException){
                log.error("Error saving the state", e);
            }
        }
    }
}
