package net.druidlabs.mindsync.util;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class for generating a timestamp which includes the date and time.
 *
 * @author Andrew Jones
 * @version 2.0
 * @since 0.10.0
 */

public final class TimeStamp {

    public static final String DEFAULT_TIMESTAMP_FORMAT = "MMM dd yyyy, HH:mm";
    public static final String DEFAULT_NUMERICAL_TIMESTAMP_FORMAT = "yyyyMMdd_HH:mm";

    /**
     * The first ever commit of this Android app project was pushed at this time.
     */

    public static final String DEFAULT_TIMESTAMP = "May 31, 2025 | 00:31";
    public static final String DEFAULT_NUMERICAL_TIMESTAMP = "20250531_00:31";

    private TimeStamp() {
    }

    public static TimeStamp createTimestamp() {
        return new TimeStamp();
    }

    /**
     * Get a timestamp of the exact moment {@link #createTimestamp()} was called.
     *
     * <p>Devices using API level 26 or higher use {@link LocalDateTime} to generate the time and
     * {@link Calendar} for anything lower than API level 26.
     * The returned timestamp follows {@link #DEFAULT_TIMESTAMP_FORMAT this format}.
     * An example is of timestamp returned in this format is {@code Jun 11 2025, 18:32}.
     *
     * @return the date and time this method was called.
     */

    public String getTimeStamp() { //Jun 11 2025, 18:32 is when this method was documented :)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIMESTAMP_FORMAT);

            return timeStampFormatter.format(now);
        }

        Date now = Calendar.getInstance().getTime();

        SimpleDateFormat timeStampFormatter = new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT, Locale.US);

        return timeStampFormatter.format(now);
    }

    /**
     * Get a timestamp of the exact moment {@link #createTimestamp()} was called.
     *
     * <p>Devices using API level 26 or higher use {@link LocalDateTime} to generate the time and
     * {@link Calendar} for anything lower than API level 26.
     * The returned timestamp follows {@link #DEFAULT_NUMERICAL_TIMESTAMP_FORMAT this format}.
     * An example is of timestamp returned in this format is {@code 20250611_18:32}.
     *
     * @return the date and time this method was called in all numbers and without spaces.
     * @since 1.1.0-beta.2
     */

    public String getNumericalTimeStamp() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();

            return DateTimeFormatter.ofPattern(DEFAULT_NUMERICAL_TIMESTAMP_FORMAT).format(now);
        }

        Date now = Calendar.getInstance().getTime();

        return new SimpleDateFormat(DEFAULT_NUMERICAL_TIMESTAMP_FORMAT, Locale.US).format(now);
    }

}
