package bth.ui.utils;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtils {

    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("HH:mm");

    public String toDateTime(Date date) {
        return DATE_TIME.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
    }
}
