package noTrigger.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class InstantFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    public static String write(Instant instant){
        return FORMATTER.format(instant.truncatedTo(ChronoUnit.SECONDS));
    }

    public static Instant read(String value){
        return Instant.from(FORMATTER.parse(value));
    }
}
