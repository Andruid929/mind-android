package net.druidlabs.mindsync.notes;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.activities.NoteEditorActivity;
import net.druidlabs.mindsync.notesio.NotesIO;
import net.druidlabs.mindsync.util.AppResources;

import java.util.List;

/**
 * This class is what allows the user to interact with the {@code List<Note>}.
 *
 * @author Andrew Jones
 * @version 1.0
 * @see android.widget.ArrayAdapter ArrayAdapter
 * @since 0.2.0
 */

public class NotesArrayAdapter<N extends Note> extends ArrayAdapter<N> {

    private final List<N> notes;
    private final LayoutInflater inflater;
    private final int resource;

    private final Context uiContext;

    public NotesArrayAdapter(@NonNull Context uiContext, int resource, @NonNull List<N> notes) {
        super(uiContext, resource, notes);

        this.notes = notes;
        this.resource = resource;
        this.uiContext = uiContext;

        inflater = (LayoutInflater) uiContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public N getItem(int position) {
        return notes.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.noteCardView = convertView.findViewById(R.id.note_cardview);
            viewHolder.noteHeader = convertView.findViewById(R.id.note_heading_textview);
            viewHolder.noteBody = convertView.findViewById(R.id.note_body_textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);

        viewHolder.noteHeader.setText(note.getHeading());
        viewHolder.noteBody.setText(note.getBody());

        viewHolder.noteCardView.setOnClickListener(v -> {
            Intent noteEditorIntent = new Intent(getContext(), NoteEditorActivity.class);
            noteEditorIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position); //Send the clicked note's index to the NoteEditorActivity

            uiContext.startActivity(noteEditorIntent);
        });

        viewHolder.noteCardView.setOnLongClickListener(v -> {
            View dialogView = inflater.inflate(R.layout.note_options_dialog, null, false);

            View confirmDialogView = inflater.inflate(R.layout.delete_confirmation_dialog, null, false);

            AlertDialog noteOptionsDialog = createOptionsDialog(position, dialogView, confirmDialogView, note.getHeading()).create();

            TextView dialogNoteHeader = dialogView.findViewById(R.id.note_options_preview_header);
            TextView dialogNoteBody = dialogView.findViewById(R.id.note_options_preview_body);

            dialogNoteHeader.setText(note.getHeading());
            dialogNoteBody.setText(note.getBody());

            noteOptionsDialog.show();

            return true;
        });

        return convertView;
    }

    /**
     * Create a new note options dialog when a note is held.
     *
     * @param position      the position/index of the note held.
     * @param dialogView    the inflated dialog layout resource
     * @param confirmDialog the inflated confirmation dialog.
     * @param noteHeading   the heading the note held.
     * @return a new dialog builder with two buttons.
     * @since 0.10.0
     * */

    private @NonNull AlertDialog.Builder createOptionsDialog(int position, View dialogView, View confirmDialog, String noteHeading) {
        return new AlertDialog.Builder(uiContext, R.style.NoteDialogTheme)

                .setView(dialogView)
                //Open the note editor to edit the note
                .setPositiveButton(R.string.note_options_btn_pos, (dialog, which) -> {
                    Intent noteEditorIntent = new Intent(getContext(), NoteEditorActivity.class);
                    noteEditorIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position); //Send the clicked note's index to the NoteEditorActivity

                    uiContext.startActivity(noteEditorIntent);
                })
                //Delete the note
                .setNegativeButton(R.string.note_options_btn_neg, ((dialog, which) -> {
                    TextView confirmDeleteTextView = confirmDialog.findViewById(R.id.note_delete_confirm_dialog_textview);

                    //Note heading in quotes before the delete confirmation text
                    String confirmText = "\"" + noteHeading + "\" " +
                            AppResources.getStringResource(R.string.note_del_confirm_text, uiContext);

                    confirmDeleteTextView.setText(confirmText);

                    AlertDialog deleteConfirmDialog = confirmDeleteDialog(position, confirmDialog, noteHeading).create();

                    deleteConfirmDialog.show();
                }));

    }

    /**
     * Create a confirmation dialog when a note is being deleted.
     *
     * @param position    the position/index of the note held.
     * @param dialogView  the inflated dialog layout resource
     * @param noteHeading the heading of the note being deleted.
     * @return a new confirmation dialog builder with two buttons.
     * @since 1.1.0-beta.2
     */

    private @NonNull AlertDialog.Builder confirmDeleteDialog(int position, View dialogView, String noteHeading) {
        return new AlertDialog.Builder(uiContext, R.style.NoteDialogTheme)
                .setView(dialogView)
                //If the user cancels
                .setPositiveButton(R.string.note_del_confirm_pos_btn, ((dialog, which) -> dialog.dismiss()))
                //If the user clicks delete
                .setNegativeButton(R.string.note_del_confirm_neg_btn, ((dialog, which) -> {
                    notes.remove(position);
                    notifyDataSetChanged();

                    NotesIO.saveNotesToStorage(uiContext);

                    String noteDeletionConfirmationText = noteHeading + " " +
                            AppResources.getStringResource(R.string.note_deleted_toast, uiContext);

                    Toast.makeText(uiContext, noteDeletionConfirmationText, Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * This inner class allows the ArrayAdapter to implement the
     * view holder pattern to increase scrolling performance in the view.
     *
     * @author Andrew Jones
     * @version 1.0
     * @since 0.2.0
     */

    private static class ViewHolder {
        private CardView noteCardView;
        private TextView noteHeader;
        private TextView noteBody;
    }
}
