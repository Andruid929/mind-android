package net.druidlabs.mindsync.activities;

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

import com.google.android.material.button.MaterialButton;

import net.druidlabs.mindsync.MainActivity;
import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notesio.NotesIO;

/**
 * This is the activity where the note editing itself takes place.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.3.0
 */

public class NoteEditorActivity extends AppCompatActivity {

    private Note currentNote;

    /**
     * The {@code View} that displays the number of characters the note has.
     * */

    private TextView bodyCharCountTextView;

    private EditText noteHeadingEditText;
    private EditText noteBodyEditText;

    /**
     * Check if this activity was created for adding a new note
     * or for editing an existing one.
     * */

    private boolean isForNoteAdding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        MaterialButton goBackBtn = findViewById(R.id.editor_toolbar_back_btn);

        bodyCharCountTextView = findViewById(R.id.editor_note_character_num_textview);

        TextView noteCreationTimeTextView = findViewById(R.id.editor_note_time_created_textview);

        goBackBtn.setOnClickListener(v -> finish());

        //The clicked note's index
        int currentNoteIndex = getIntent().getIntExtra(Note.INTENT_NOTE_POSITION, -1);

        isForNoteAdding = (currentNoteIndex == -1);

        String noteHeading;
        String noteBody;

        if (isForNoteAdding) {
            //currentNoteIndex is -1, user is creating a new note
            currentNote = new Note(Note.TEST_HEADING, Note.TEST_BODY);

            noteHeading = currentNote.getTimeStamp();
            noteBody = "";

            currentNote.setHeading(noteHeading);
            currentNote.setBody("");
        } else {
            //currentNoteIndex is greater than -1, user is editing an existing note
            currentNote = MainActivity.notesList.get(currentNoteIndex);

            noteHeading = currentNote.getHeading();
            noteBody = currentNote.getBody();
        }

        String noteTimeStamp = currentNote.getTimeStamp();

        int numOfCharsInBody = noteBody.length();

        String charText;

        if (numOfCharsInBody == 1) {
            charText = "1 " + getStringResource(R.string.editor_note_info_character);
        } else {
            charText = numOfCharsInBody + " " + getStringResource(R.string.editor_note_info_character_num);
        }

        bodyCharCountTextView.setText(charText);
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
                if (s.toString().isBlank()) { //Check if a note is blank and if it is, undo the heading change
                    Toast.makeText(NoteEditorActivity.this, R.string.blank_note_warning, Toast.LENGTH_LONG).show();
                    currentNote.setHeading(noteHeading);
                }
            }
        });

        noteBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNote.setBody(s.toString());

                String charText;

                int numOfCharsInBody = s.length();

                if (numOfCharsInBody == 1) {
                    charText = "1 " + getStringResource(R.string.editor_note_info_character);
                } else {
                    charText = numOfCharsInBody + " " + getStringResource(R.string.editor_note_info_character_num);
                }

                bodyCharCountTextView.setText(charText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void finish() {
        super.finish();

        if (!isForNoteAdding) {
            //Not a new note that needs to be checked for validity
            return;
        }

        String timestamp = currentNote.getTimeStamp();

        String presetHeading = noteHeadingEditText.getText().toString();
        String presetBody = noteBodyEditText.getText().toString();

        if (presetHeading.equals(timestamp) && !presetBody.isBlank()) {
            //The body has been modified, save as new note
            MainActivity.notesList.add(currentNote);

            NotesIO.saveNotesToStorage(getApplicationContext());
        } else if (!presetHeading.isBlank() && !presetHeading.equals(timestamp)) {
            //The heading has been modified and is not blank, save as new note
            MainActivity.notesList.add(currentNote);

            NotesIO.saveNotesToStorage(getApplicationContext());
        }
    }

    /**
     * Get the string resource from the specific resource ID.
     *
     * @param resId the string resource ID.
     * @return the String represented by the {@code resID}.
     * @since 0.7.0
     */

    private String getStringResource(int resId) {
        return getResources().getString(resId);
    }
}