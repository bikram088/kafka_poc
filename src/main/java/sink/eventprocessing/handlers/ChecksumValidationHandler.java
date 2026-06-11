package sink.eventprocessing.handlers;

import org.apache.commons.io.FileUtils;
import org.springframework.util.DigestUtils;
import sink.eventprocessing.FileMergeContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.LogManager;

public class ChecksumValidationHandler {
    private static final Logger log = LogManager.getLogger(ChecksumValidationHandler.class);

    @Override
    public boolean process(FileMergeContext context){
        try{
            String mergerFileChecksum = calculateSHA256Checksum(context.getMergedFilePath());
            if(context.getChecksum().equals(mergerFileChecksum)){
                log.info("Checksum valisdation : passed, checkum {}", mergerFileChecksum);
                context.setMerged(true);
            }else{
                return false;
            }
        }catch (IOException w){
            return false;
        }
    }

    public static String calculateSHA256Checksum(Path filePath) throws IOException{
        FileInputStream file = FileUtils.openInputStream(filePath.toFile());
        String checksum = DigestUtils.sha256Hex(file);
        file.close();
        return checksum;
    }
}
