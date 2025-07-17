package net.druidlabs.mindsync.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import net.druidlabs.mindsync.MainActivity;
import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notesio.NotesIO;
import net.druidlabs.mindsync.preferences.AppPreferences;
import net.druidlabs.mindsync.util.AppResources;
import net.druidlabs.mindsync.util.StringUtils;
import net.druidlabs.mindsync.util.UiUtil;

/**
 * This is the activity where the note editing itself takes place.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.3.0
 */

public class NoteEditorActivity extends AppCompatActivity {

    /**
     * The key for the result extra when a new note is added.
     *
     * @since 1.1.0-beta.3
     */

    public static final String NEW_NOTE_ADDED_EXTRA = "New note added";

    /**
     * The key for the result extra when a note is edited.
     *
     * @since 1.1.0-beta.3
     */

    public static final String EDITED_NOTE_INDEX_EXTRA = "Note edited";

    /**
     * The note being edited in this activity.
     */

    private Note currentNote;

    /**
     * The notes list index of the note being edited.
     */

    private int currentNoteIndex;

    /**
     * The {@code View} that displays the number of characters the note has.
     */

    private TextView bodyCharCountTextView;

    /**
     * The {@code View} where the user edits the note heading.
     */

    private EditText noteHeadingEditText;

    /**
     * The {@code View} where the user edits the note body.
     */

    private EditText noteBodyEditText;

    /**
     * The saved note heading before the note is edited.
     */

    private String originalHeading;

    /**
     * The saved note body before editing.
     */

    private String originalBody;

    /**
     * Check if this activity was created for adding a new note
     * or for editing an existing one.
     */

    private boolean isForNoteAdding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UiUtil.setBlackMode(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteHeadingEditText = findViewById(R.id.editor_heading_edittext);
        noteBodyEditText = findViewById(R.id.editor_body_edittext);

        MaterialToolbar noteEditorToolbar = findViewById(R.id.editor_toolbar);
        noteEditorToolbar.setNavigationOnClickListener(v -> finish());

        bodyCharCountTextView = findViewById(R.id.editor_note_character_num_textview);

        TextView noteCreationTimeTextView = findViewById(R.id.editor_note_time_created_textview);

        //The clicked note's index
        currentNoteIndex = getIntent().getIntExtra(Note.INTENT_NOTE_POSITION, -1);

        isForNoteAdding = (currentNoteIndex == -1);

        String noteHeading;
        String noteBody;

        if (isForNoteAdding) {
            //currentNoteIndex is -1, user is creating a new note
            currentNote = new Note(Note.TEST_HEADING, Note.TEST_BODY);

            noteHeading = "";
            noteBody = "";

            currentNote.setHeading(noteHeading);
            currentNote.setBody(noteBody);

            noteHeadingEditText.setHint(currentNote.getNumericalTimeStamp());

        } else {
            //currentNoteIndex is greater than -1, user is editing an existing note
            currentNote = MainActivity.notesList.get(currentNoteIndex);

            noteHeading = currentNote.getHeading();
            noteBody = currentNote.getBody();

            originalHeading = noteHeading;
            originalBody = noteBody;
        }

        String timestampFormat = AppPreferences.noteTimeStampFormat(this);

        String noteTimeStamp = currentNote.getTimeStamp(timestampFormat);

        int numOfCharsInBody = noteBody.length();

        bodyCharCountTextView.setText(getCharText(numOfCharsInBody));
        noteCreationTimeTextView.setText(noteTimeStamp);

        noteHeadingEditText.setText(noteHeading);
        noteBodyEditText.setText(noteBody);

        noteHeadingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setHeading(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        noteBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setBody(s.toString());

                int numOfCharsInBody = s.length();

                bodyCharCountTextView.setText(getCharText(numOfCharsInBody));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void finish() {
        if (isForNoteAdding) {
            //New note that needs be checked for validity before adding

            String timestamp = currentNote.getNumericalTimeStamp();

            String currentHeading = noteHeadingEditText.getText().toString();
            String currentBody = noteBodyEditText.getText().toString();

            boolean headingModified = StringUtils.isInputBlank(currentHeading);

            boolean bodyModified = StringUtils.isInputBlank(currentBody);

            if (!headingModified) {
                currentNote.setHeading(currentHeading);
                currentNote.setBody(currentBody);

                MainActivity.notesList.add(currentNote);

                NotesIO.saveNotesToStorage(getApplicationContext());

                setNewNoteAddedResult();

            } else if (!bodyModified) {
                currentNote.setHeading(timestamp);
                currentNote.setBody(currentBody);

                MainActivity.notesList.add(currentNote);

                NotesIO.saveNotesToStorage(getApplicationContext());

                setNewNoteAddedResult();

            }

            super.finish();

        } else {
            //The note was being edited, check if changes are valid

            String currentHeader = noteHeadingEditText.getText().toString();
            String currentBody = noteBodyEditText.getText().toString();

            boolean isHeadingBlank = StringUtils.isInputBlank(currentHeader);

            if (!isHeadingBlank && isNoteModified(currentHeader, currentBody)) {
                //Note has changes which are valid and can be saved

                setNoteEditedResult();

            } else if (isHeadingBlank) {
                //Header is blank, invalidate changes

                currentNote.setHeading(originalHeading);
                currentNote.setBody(originalBody);

                //Notify the user that the changes made have not been saved.
                boolean blankHeadingToastEnabled = AppPreferences.isBlankHeadingToastEnabled(this);

                if (blankHeadingToastEnabled) {
                    Toast.makeText(NoteEditorActivity.this, R.string.editor_blank_heading, Toast.LENGTH_LONG).show();
                }
            }

            super.finish();
        }

    }

    /**
     * Check to see if the note being edited has been changed.
     * <p>This method compares the saved note's heading and title to
     * the current state.
     * If the two states are identical, the note has not been changed.
     *
     * @param currentHeading the text in the {@link #noteHeadingEditText heading}.
     * @param currentBody    the text in the {@link #noteBodyEditText body}.
     * @return {@code true} - if either the heading or body have been changed
     * <p>    {@code false} - if both heading and body are unchanged.
     * @since 1.1.0-beta.3
     */

    private boolean isNoteModified(String currentHeading, String currentBody) {
        //False if the current and original headings are identical
        if (!currentHeading.equals(originalHeading)) return true;

        //False if the current and original bodies are identical
        return !currentBody.equals(originalBody);
    }

    /**
     * Get text showing how many characters are in the note's body.
     *
     * @param numOfCharsInBody number of characters in note body. Should be {@link Note#getBody() noteBody}{@code .length()}.
     * @return {@code 1 character} if there's a single character
     * and {@code # characters} with # being the number of characters specified by {@code numOfCharsInBody}.
     * @since 1.1.0-beta.2
     */

    private String getCharText(int numOfCharsInBody) {
        if (numOfCharsInBody == 1) {
            return "1 " + AppResources.getStringResource(R.string.editor_note_info_character, NoteEditorActivity.this);
        } else {
            return numOfCharsInBody + " " + AppResources.getStringResource(R.string.editor_note_info_character_num, NoteEditorActivity.this);
        }
    }

    /**
     * Set the result of this activity when a new note has been added.
     * <p>When the user is in this activity to add a new note and the
     * conditions for adding one are met, set a "new note added" result.
     *
     * @since 1.1.0-beta.3
     */

    private void setNewNoteAddedResult() {
        Intent newNoteIntent = new Intent();
        newNoteIntent.putExtra(NEW_NOTE_ADDED_EXTRA, true);

        setResult(RESULT_OK, newNoteIntent);
    }

    /**
     * Set the result of this activity when {@link #currentNote this note} has been edited.
     *
     * @since 1.1.0-beta.3
     */

    private void setNoteEditedResult() {
        Intent noteEditedIntent = new Intent();
        noteEditedIntent.putExtra(EDITED_NOTE_INDEX_EXTRA, currentNoteIndex);

        setResult(RESULT_OK, noteEditedIntent);
    }
}
