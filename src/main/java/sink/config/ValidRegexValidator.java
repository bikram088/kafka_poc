package sink.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValidRegexValidator implements ConstraintValidator<ValidRegex, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()){
            return false;
        }
        try{
            Pattern.compile(value);
            return true;
        }catch (PatternSyntaxException e){
            return false;
        }
    }
}
