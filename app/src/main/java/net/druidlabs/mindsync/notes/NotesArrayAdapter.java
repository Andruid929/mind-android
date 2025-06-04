package net.druidlabs.mindsync.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import net.druidlabs.mindsync.R;
import net.druidlabs.mindsync.activities.NoteEditorActivity;

import java.util.List;

/**
 * This class is what allows the user to interact with the {@code List<Note>}.
 *
 * @author Andrew Jones
 * @see android.widget.ArrayAdapter ArrayAdapter
 * @since 0.2.0
 * @version 1.0
 * */

public class NotesArrayAdapter<N extends Note> extends ArrayAdapter<N> {

    private final List<N> notes;
    private final LayoutInflater inflater;
    private final int resource;

    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull List<N> notes) {
        super(context, resource, notes);

        this.notes = notes;
        this.resource = resource;

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
            noteEditorIntent.putExtra(Note.INTENT_HEADING, note.getHeading());
            noteEditorIntent.putExtra(Note.INTENT_BODY, note.getBody());
            noteEditorIntent.putExtra(Note.INTENT_NOTE_POSITION, position);

            getContext().startActivity(noteEditorIntent);
        });

        return convertView;
    }

    /**
     * This inner class allows the ArrayAdapter to implement the
     * view holder pattern to increase scrolling performance in the view.
     *
     * @author Andrew Jones
     * @since 0.2.0
     * @version 1.0
     * */

    private static class ViewHolder {
        private CardView noteCardView;
        private TextView noteHeader;
        private TextView noteBody;
    }
}
