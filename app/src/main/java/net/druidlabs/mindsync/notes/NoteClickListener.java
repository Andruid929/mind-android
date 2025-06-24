package net.druidlabs.mindsync.notes;

/**
 * This interface outlines the contract of what should happen when the
 * user interacts with a note.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-beta.3
 */

public interface NoteClickListener {

    /**
     * This method gets called when the user clicks on a note.
     *
     * @param position the index of the note clicked.
     */

    void onNoteClick(int position);

    /**
     * This method gets called when the user holds down on a note.
     *
     * @param position the index of the note held.
     */

    void onNoteLongClick(int position);

}
