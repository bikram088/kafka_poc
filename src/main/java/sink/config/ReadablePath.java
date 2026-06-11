package sink.config;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

@Documented
@Constraint(validatedBy =  ReadablePathValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
    public @interface ReadablePath {
        String message() default "Path must exist and be readable";
        Class<?>[] groups() default {};
        Class<? extends payload>[] payload() default {};
    }
