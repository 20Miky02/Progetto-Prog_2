package mole;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

/**
 * Funzione che estrae l'ora e i minuti (LocalTime) da un LocalDateTime.
 */
public class TimeExtractor implements Function<LocalDateTime, LocalTime> {
    /**
     * Costruttore di default per TimeExtractor.
     */
    public TimeExtractor() {
    }

    @Override
    public LocalTime apply(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalTime();
    }
}