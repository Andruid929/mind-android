package net.druidlabs.mindsync.notes;

import java.util.Objects;

/**
 * This class represents a note and contains the note's data.
 * <p>The moment an object of this clas and the parameters are valid,
 * this note is automatically added to {@code Notes.notes}.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.0.1
 */

public final class Note {

    public static final String TEST_HEADING = "Testing header";
    public static final String TEST_BODY = "Testing body";

    public static final String INTENT_HEADING = "Note heading";
    public static final String INTENT_BODY = "Note body";

    public static final String INTENT_NOTE_POSITION = "Clicked note position";

    /**
     * The note's heading.
     */

    private final String heading;

    /**
     * The note's body
     */

    private final String body;

    /**
     * Get a new note instance.
     *
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

        Notes.notes.add(this);
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
