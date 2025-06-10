package net.druidlabs.mindsync.notes;

import net.druidlabs.mindsync.MainActivity;

import java.util.Objects;

/**
 * This class represents a note and contains the note's data.
 * <p>The moment an object of this class and the parameters are valid,
 * this note is automatically added to {@link MainActivity#notesList}.
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

    /**
     * Get a new note instance.
     * <p>Passing in {@link #TEST_HEADING} for the heading and
     * {@link #TEST_BODY} for the body will set this note as a test note and
     * will not add automatically add it to {@link MainActivity#notesList}.
     * @param heading the heading/title of the note.
     * @param body    the body of the note.
     */

    public Note(String heading, String body) {
        this.heading = heading;
        this.body = body;

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
     * @since 0.6.0
     * @param heading the new heading of this note.
     * */

    public void setHeading(String heading) {
        if (heading.isBlank()) return;

        this.heading = heading;
    }

    /**
     * Set a new body to this note.
     *
     * @since 0.6.0
     * @param body the new heading of this note.
     * */

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(heading, note.heading) && Objects.equals(body, note.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, body);
    }
}
