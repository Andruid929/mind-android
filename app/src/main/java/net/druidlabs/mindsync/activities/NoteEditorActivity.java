package net.druidlabs.mindsync.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.notes.Note;
import net.druidlabs.mindsync.notes.Notes;

/**
 * This is the activity where the note editing itself takes place.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 0.3.0
 */

public class NoteEditorActivity extends AppCompatActivity {

    private Note currentNote;

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

        EditText noteHeadingEditText = findViewById(R.id.editor_heading_edittext);
        EditText noteBodyEditText = findViewById(R.id.editor_body_edittext);

        //The clicked note's index
        int currentNoteIndex = getIntent().getIntExtra(Note.INTENT_NOTE_POSITION, -1);

        currentNote = Notes.getNotes().get(currentNoteIndex);

        String noteHeading = currentNote.getHeading();
        String noteBody = currentNote.getBody();

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
                    Toast.makeText(NoteEditorActivity.this,
                            "Note heading cannot be blank, heading will not be saved", Toast.LENGTH_LONG).show();
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}