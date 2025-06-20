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
import net.druidlabs.mindsync.util.AppResources;

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
     */

    private TextView bodyCharCountTextView;

    private EditText noteHeadingEditText;
    private EditText noteBodyEditText;

    /**
     * Check if this activity was created for adding a new note
     * or for editing an existing one.
     */

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

            noteHeading = currentNote.getNumericalTimeStamp();
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
        super.finish();

        if (!isForNoteAdding) {
            //Not a new note that needs to be checked for validity
            return;
        }

        String timestamp = currentNote.getNumericalTimeStamp();

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
}