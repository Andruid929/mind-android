package net.druidlabs.mindsync.util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class for generating a timestamp which includes the date and time.
 *
 * @author Andrew Jones
 * @version 3.0
 * @since 0.10.0
 */

public final class TimeStamp {

    /**
     * Output: Jun 11 2025 | 18:32
     */

    public static final String DEFAULT_TIMESTAMP_FORMAT = "MMM dd, yyyy @ HH:mm";

    /**
     * Output: 20250611_18:32
     */

    public static final String DEFAULT_NUMERICAL_TIMESTAMP_FORMAT = "yyyyMMdd_HH:mm";

    /**
     * Output: 11 June 2025 | 18:32
     *
     * @since 1.2.0-beta.1
     */

    public static final String TIMESTAMP_FORMAT_A = "dd MMMM yyyy @ HH:mm";

    /**
     * Output: June 11 2025 | 18:32
     *
     * @since 1.2.0-beta.1
     */

    public static final String TIMESTAMP_FORMAT_B = "MMMM dd, yyyy @ HH:mm";

    /**
     * Output: 2025 11 June | 18:32
     */

    public static final String TIMESTAMP_FORMAT_C = "yyyy dd MMMM @ HH:mm";

    public static final String[] TIMESTAMP_FORMATS = {DEFAULT_TIMESTAMP_FORMAT, TIMESTAMP_FORMAT_A,
            TIMESTAMP_FORMAT_B, TIMESTAMP_FORMAT_C};

    /**
     * The first ever commit of this Android app project was pushed at this time.
     */

    public static final String DEFAULT_TIMESTAMP = "May 31, 2025 | 00:31";

    private TimeStamp() {
    }

    /**
     * Get the epoch time as a {@link TemporalAccessor} object that can be formatted.
     *
     * @param epochMillis date and time in epoch milliseconds.
     * @return time as a LocalDateTime object.
     * @apiNote API level < 26 cannot use this method.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime getEpochAsTemporal(long epochMillis) {
        Instant instant = Instant.ofEpochMilli(epochMillis);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Get a timestamp of the exact moment this method was called.
     *
     * <p>Devices using API level 26 or higher use {@link LocalDateTime} to generate the time and
     * {@link Calendar} for anything lower than API level 26.
     * The returned timestamp follows {@link #DEFAULT_TIMESTAMP_FORMAT this format}.
     * An example is of timestamp returned in this format is {@code Jun 11 2025, 18:32}.
     *
     * @param epochMillis     the time a note was created in epoch milliseconds.
     * @param timestampFormat the format to use when formatting the timestamp.
     * @return the date and time this method was called.
     */

    public static String getTimeStamp(@NonNull String timestampFormat, long epochMillis) { //Jun 11 2025, 18:32 is when this method was documented :)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            LocalDateTime creationTime = getEpochAsTemporal(epochMillis);

            DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern(timestampFormat);

            return timeStampFormatter.format(creationTime);
        }

        Date creationTime = new Date(epochMillis);

        SimpleDateFormat timeStampFormatter = new SimpleDateFormat(timestampFormat, Locale.US);

        return timeStampFormatter.format(creationTime);
    }

    /**
     * Get a timestamp of the exact moment this method} was called.
     *
     * <p>Devices using API level 26 or higher use {@link LocalDateTime} to generate the time and
     * {@link Calendar} for anything lower than API level 26.
     * The returned timestamp follows {@link #DEFAULT_NUMERICAL_TIMESTAMP_FORMAT this format}.
     * An example is of timestamp returned in this format is {@code 20250611_18:32}.
     *
     * @return the date and time this method was called in all numbers and without spaces.
     * @since 1.1.0-beta.2
     */

    public static String getNumericalTimeStamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();

            return DateTimeFormatter.ofPattern(DEFAULT_NUMERICAL_TIMESTAMP_FORMAT).format(now);
        }

        Date now = Calendar.getInstance().getTime();

        return new SimpleDateFormat(DEFAULT_NUMERICAL_TIMESTAMP_FORMAT, Locale.US).format(now);
    }

}
