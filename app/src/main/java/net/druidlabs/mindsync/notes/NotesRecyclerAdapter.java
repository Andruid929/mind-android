package net.druidlabs.mindsync.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.activities.NoteEditorActivity;
import net.druidlabs.mindsync.notesio.NotesIO;
import net.druidlabs.mindsync.util.AppResources;

import java.util.List;

/**
 * The adapter for the notes {@code RecyclerView}.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-beta.2
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Note> notesList;

    private final Context uiContext;

    private final LayoutInflater viewInflater;

    public NotesRecyclerAdapter(List<Note> notesList, Context uiContext) {
        this.notesList = notesList;
        this.uiContext = uiContext;

        viewInflater = (LayoutInflater) uiContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notes_tiles, parent, false);
        return new ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Note note = notesList.get(position);

        TextView noteHeadingTextView = holder.itemView.findViewById(R.id.note_heading_textview);
        TextView noteBodyTextView = holder.itemView.findViewById(R.id.note_body_textview);

        CardView noteCardView = holder.itemView.findViewById(R.id.note_cardview);

        noteHeadingTextView.setText(note.getHeading());
        noteBodyTextView.setText(note.getBody());

        noteCardView.setOnClickListener(v -> {
            Intent noteEditorIntent = new Intent(uiContext, NoteEditorActivity.class);
            noteEditorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position); //Send the clicked note's index to the NoteEditorActivity

            uiContext.startActivity(noteEditorIntent);
        });

        noteCardView.setOnLongClickListener(v -> {
            View dialogView = viewInflater.inflate(R.layout.note_options_dialog, null, false);

            View confirmDialogView = viewInflater.inflate(R.layout.delete_confirmation_dialog, null, false);

            AlertDialog noteOptionsDialog = createOptionsDialog(position, dialogView, confirmDialogView, note.getHeading()).create();

            TextView dialogNoteHeader = dialogView.findViewById(R.id.note_options_preview_header);
            TextView dialogNoteBody = dialogView.findViewById(R.id.note_options_preview_body);

            dialogNoteHeader.setText(note.getHeading());
            dialogNoteBody.setText(note.getBody());

            noteOptionsDialog.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
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
     */

    private @NonNull AlertDialog.Builder createOptionsDialog(int position, View dialogView, View confirmDialog, String noteHeading) {
        return new AlertDialog.Builder(uiContext, R.style.NoteDialogTheme)

                .setView(dialogView)
                //Open the note editor to edit the note
                .setPositiveButton(R.string.note_options_btn_pos, (dialog, which) -> {
                    Intent noteEditorIntent = new Intent(uiContext, NoteEditorActivity.class);
                    noteEditorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    notesList.remove(position);
                    notifyItemRemoved(position);

                    NotesIO.saveNotesToStorage(uiContext);

                    String noteDeletionConfirmationText = noteHeading + " " +
                            AppResources.getStringResource(R.string.note_deleted_toast, uiContext);

                    Toast.makeText(uiContext, noteDeletionConfirmationText, Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * ViewHolder for the {@code NotesRecyclerAdapter}.
     *
     * @author Andrew Jones
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder RecyclerView.ViweHolder
     * @since 1.1.0-beta.2
     */

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView noteHeaderTextView;
        private TextView noteBodyTextView;

        private CardView noteCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteHeaderTextView = itemView.findViewById(R.id.note_heading_textview);
            noteBodyTextView = itemView.findViewById(R.id.note_body_textview);
            noteCardView = itemView.findViewById(R.id.note_cardview);
        }

    }
}
