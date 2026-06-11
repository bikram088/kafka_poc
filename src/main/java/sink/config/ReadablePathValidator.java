package sink.config;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sink.eventprocessing.handlers.MetaDataExtractionHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ReadablePathValidator implements ConstraintValidator<ReadablePath, String> {
    private static final Logger log = LogManager.getLogger(ReadablePathValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()){
            return false;
        }
        try {
            return Files.exists(Paths.get(value)) && Files.isReadable(Paths.get(value));
        }catch (Exception e){
            return false;
        }
    }
}
