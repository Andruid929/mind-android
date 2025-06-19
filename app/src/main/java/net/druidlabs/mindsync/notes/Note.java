package net.druidlabs.mindsync.notes;

import androidx.annotation.NonNull;

import net.druidlabs.mindsync.MainActivity;
import net.druidlabs.mindsync.util.TimeStamp;

import java.util.Objects;

/**
 * This class represents a note and contains the note's data.
 * <p>The moment the parameters are valid, any new object of this class
 * is automatically added to {@link MainActivity#notesList}.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.0.1
 */

public final class Note {

    public static final String TEST_HEADING = "Testing header";
    public static final String TEST_BODY = "Testing body";
    public static final String INTENT_NOTE_POSITION = "Clicked note position";

    /**
     * The note's heading.
     */

    private String heading;

    /**
     * The note's body
     */

    private String body;

    private final String timeStamp;
    private final String numericalTimestamp;

    /**
     * Get a new note instance.
     * <p>Passing in {@link #TEST_HEADING} for the heading and
     * {@link #TEST_BODY} for the body will set this note as a test note and
     * will not add automatically add it to {@link MainActivity#notesList}.
     *
     * @param heading the heading/title of the note.
     * @param body    the body of the note.
     */

    public Note(String heading, String body) {
        this.heading = heading;
        this.body = body;

        TimeStamp time = TimeStamp.createTimestamp();

        timeStamp = time.getTimeStamp();
        numericalTimestamp = time.getNumericalTimeStamp();

        if (heading.isBlank()) {
            return;
        }

        if (heading.equals(TEST_HEADING) && body.equals(TEST_BODY)) {
            return;
        }

        MainActivity.notesList.add(this);
    }

    /**
     * Set a new heading for this note.
     * Since note headings cannot be blank,
     * passing a blank heading will cancel the operation.
     *
     * @param heading the new heading of this note.
     * @since 0.6.0
     */

    public void setHeading(String heading) {
        if (heading.isBlank()) return;

        this.heading = heading;
    }

    /**
     * Set a new body to this note.
     *
     * @param body the new heading of this note.
     * @since 0.6.0
     */

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get the name of this note.
     *
     * @return the heading of the current note.
     */

    public String getHeading() {
        return heading;
    }

    /**
     * Get the body.
     *
     * @return the body of this note.
     */

    public String getBody() {
        return body;
    }

    /**
     * This note's timestamp.
     *
     * @return the date and time this note was created.
     * @since 0.10.0
     */
    @NonNull
    public String getTimeStamp() {
        return Objects.requireNonNullElse(timeStamp, TimeStamp.DEFAULT_TIMESTAMP);

    }

    /**
     * This note's timestamp in all numbers and without spaces.
     *
     * @return the date and time this note was created.
     * @since 1.1.0-beta.3
     */

    @NonNull
    public String getNumericalTimeStamp() {
        return Objects.requireNonNullElse(numericalTimestamp, TimeStamp.DEFAULT_NUMERICAL_TIMESTAMP);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(heading, note.heading) && Objects.equals(body, note.body)
                && Objects.equals(timeStamp, note.timeStamp) && Objects.equals(numericalTimestamp, note.numericalTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, body, timeStamp, numericalTimestamp);
    }
}
