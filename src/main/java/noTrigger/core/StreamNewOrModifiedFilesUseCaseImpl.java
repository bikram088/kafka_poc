package noTrigger.core;

import noTrigger.config.DirectoryScanConfig;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.format.datetime.standard.InstantFormatter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StreamNewOrModifiedFilesUseCaseImpl implements StreamNewOrModifiedFilesUseCase {
    private final Logger log = LogManager.getLogger(StreamNewOrModifiedFilesUseCaseImpl);

    private final ScanStateRepository stateRepo;
    private final DirectoryScannerPort directoryScanner;
    private final FileChunkReaderPort fileChunkReader;
    private final KafkaProducerPort kafkaProducer;

    public StreamNewOrModifiedFilesUseCaseImpl(ScanStateRepository stateRepo, DirectoryScannerPort directoryScanner,FileChunkReaderPort fileChunkReader, KafkaProducerPort kafkaProducer
    ){this.stateRepo = stateRepo;
        this.directoryScanner = directoryScanner;
        this.fileChunkReader = fileChunkReader;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void streamFromDirectory(DirectoryScanConfig directoryScanConfig) {
        String dirId = String.format("%s = %s", directoryScanConfig.getDirectoryPath(), directoryScanConfig.getFileNamePattern());
        String streamUUID = UUID.randomUUID().toString();
        ThreadContext.put("streamUUID", streamUUID);

        Instant effectiveFrom, effectiveTo;

        Optional<Instant> persistedFrom = stateRepo.loadLadtTimeStamp(dirId);
        if(persistedFrom.isPresent()){
            effectiveFrom = persistedFrom.get();
        } else if(directoryScanConfig.getFromTime() != null){
            effectiveFrom = directoryScanConfig.getFromTimeInstant();
        } else {
            effectiveFrom = Instant.now().minus(
                    Duration.of(
                            directoryScanConfig.getFrequencyMinutes(),
                            ChronoUnit.valueOf(directoryScanConfig.getTimeUnitAsString())
                    )
            );
        }

        if(directoryScanConfig.getToTime() != null){
            effectiveTo = directoryScanConfig.getToTimeInstant();
        } else {
            effectiveTo = Instant.now();
        }

        log.info("Scanning log dir : {}/{} -> from: {}, to:{}",
                directoryScanConfig.getDirectoryPath(),
                directoryScanConfig.getFileNamePattern(),
                InstantFormatter.write(effectiveFrom),
                InstantFormatter.write(effectiveTo)
                );

        List<ScannedFile> eligible = directoryScanner.scan(directoryScanConfig, effectiveFrom, Instant.now());
        log.info("Files to Stream: {}", eligible);

        if(!eligible.isEmpty()){
            Instant maxLastModified = effectiveFrom;
            for(ScannedFile file : eligible){
                fileChunkReader.readInChunk(
                        file,
                        1024 * 1024 - 10*1024,
                        (chunk, headers) -> {
                            kafkaProducer.sendChunk(directoryScanConfig.getKafkaTopic(),streamUUID, chunk, headers);
                        });
                if (file.getLastModifiedTime().isAfter(maxLastModified)){
                    maxLastModified = file.getLastModifiedTime();
                }
            }
            log.info("File Streamed...");
            stateRepo.saveLastTimeStamp(dirId, effectiveTo);
            ThreadContext.clearAll();
        }
    }
}
