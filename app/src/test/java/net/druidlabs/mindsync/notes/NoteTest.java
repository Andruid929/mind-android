package net.druidlabs.mindsync.notes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for the {@link Note} class
 *
 * @since 0.6.0
 * @author Andrew Jones
 * */

public class NoteTest {

    private final Note TEST_NOTE = new Note(Note.TEST_HEADING, Note.TEST_BODY);

    @Test
    public void getHeadingTest() {
        assertEquals("Testing header", TEST_NOTE.getHeading());
    }

    @Test
    public void setHeadingTest() {
        TEST_NOTE.setHeading("A test");
        assertEquals("A test", TEST_NOTE.getHeading());
    }

    @Test
    public void getBodyTest() {
        assertEquals("Testing body", TEST_NOTE.getBody());
    }

    @Test
    public void setBodyTest() {
        TEST_NOTE.setBody("Body the test");
        assertEquals("Body the test", TEST_NOTE.getBody());
    }

}