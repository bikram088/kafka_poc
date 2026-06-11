package sink.config;

import jakarta.validation.Payload;

public @interface ValidRegex {
    String message() default "Invalid regex pattern";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
