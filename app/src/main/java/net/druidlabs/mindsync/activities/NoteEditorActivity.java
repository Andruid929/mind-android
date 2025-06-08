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

    private MaterialButton goBackBtn;

    private TextView bodyCharCountTextView;
    private TextView noteCreationTimeTextView;

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

        goBackBtn = findViewById(R.id.editor_toolbar_back_btn);

        bodyCharCountTextView = findViewById(R.id.editor_note_character_num_textview);
        noteCreationTimeTextView = findViewById(R.id.editor_note_time_created_textview);

        goBackBtn.setOnClickListener(v -> finish());

        //The clicked note's index
        int currentNoteIndex = getIntent().getIntExtra(Note.INTENT_NOTE_POSITION, -1);

        currentNote = Notes.getNotes().get(currentNoteIndex);

        String noteHeading = currentNote.getHeading();
        String noteBody = currentNote.getBody();

        int numOfCharsInBody = noteBody.length();

        String charText;

        if (numOfCharsInBody == 1) {
            charText = "1 " + getStringResource(R.string.editor_note_info_character);
        } else {
            charText = numOfCharsInBody + " " + getStringResource(R.string.editor_note_info_character_num);
        }

        bodyCharCountTextView.setText(charText);

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