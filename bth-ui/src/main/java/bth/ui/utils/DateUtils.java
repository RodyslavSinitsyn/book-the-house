package bth.ui.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {

    public static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("HH:mm");

    public String toDateTime(Date date) {
        return DATE_TIME.format(date);
    }
}
