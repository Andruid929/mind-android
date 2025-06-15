package net.druidlabs.mindsync.notes;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.activities.NoteEditorActivity;

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

    private final Context context;

    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull List<N> notes) {
        super(context, resource, notes);

        this.notes = notes;
        this.resource = resource;
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            context.startActivity(noteEditorIntent);
        });

        viewHolder.noteCardView.setOnLongClickListener(v -> {
            View dialogView = inflater.inflate(R.layout.note_options_dialog, null, false);

            AlertDialog noteOptionsDialog = createOptionsDialog(position, dialogView).create();

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
     * @param position the position/index of the note held.
     * @param dialogView the inflated dialog layout resource
     * @return a new dialog builder with two buttons.
     * @since 0.10.0
     * */

    private @NonNull AlertDialog.Builder createOptionsDialog(int position, View dialogView) {
        return new AlertDialog.Builder(context, R.style.NoteDialogTheme)

                .setView(dialogView)
                //Open the note editor to edit the note
                .setPositiveButton(R.string.note_options_btn_pos, (dialog, which) -> {
                    Intent noteEditorIntent = new Intent(getContext(), NoteEditorActivity.class);
                    noteEditorIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position); //Send the clicked note's index to the NoteEditorActivity

                    context.startActivity(noteEditorIntent);
                })
                //Delete the note
                .setNegativeButton(R.string.note_options_btn_neg, ((dialog, which) -> {
                    dialog.dismiss();

                    notes.remove(position);

                    notifyDataSetChanged();
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
