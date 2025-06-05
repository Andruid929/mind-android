package net.druidlabs.mindsync.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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
 * @since 0.3.0
 * @version 1.0
 * */

public class NoteEditorActivity extends AppCompatActivity {

    private final String NOTE_SYNC_STATE = "Note synchronisation: ";

    private EditText noteHeadingEditText;

    private EditText noteBodyEditText;

    private Intent fromMainActivityIntent;
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

        noteHeadingEditText = findViewById(R.id.editor_heading_edittext);
        noteBodyEditText = findViewById(R.id.editor_body_edittext);

        fromMainActivityIntent = getIntent();

        int currentNotePosition = fromMainActivityIntent.getIntExtra(Note.INTENT_NOTE_POSITION, -1);

        currentNote = Notes.getNotes().get(currentNotePosition);

        String noteHeading = fromMainActivityIntent.getStringExtra(Note.INTENT_HEADING);
        String noteBody = fromMainActivityIntent.getStringExtra(Note.INTENT_BODY);

        boolean isNoteSynced = currentNote.getHeading().equals(noteHeading) && currentNote.getBody().equals(noteBody);

        Log.d(NOTE_SYNC_STATE, isNoteSynced ? "Passed" : "Failed"); //Check to see if the intent note details passed and the note's data sync

        noteHeadingEditText.setText(noteHeading);
        noteBodyEditText.setText(noteBody);
    }
}