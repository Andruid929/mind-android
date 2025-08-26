package net.druidlabs.mindsync.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.druidlabs.mindsync.R;

import java.util.List;

/**
 * The adapter for the notes {@code RecyclerView}.
 * <p>Prior to this, a {@link GridView} with {@link ArrayAdapter} was used
 * for notes display.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-beta.2
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private final List<Note> notesList;

    private final NoteClickListener noteClickListener;

    /**
     * Create a new adapter for the notes RecyclerView.
     *
     * @param notesList         main notes list.
     * @param noteClickListener a class that implements {@link NoteClickListener}.
     * @since 1.1.0-beta.2
     */

    public NotesRecyclerAdapter(List<Note> notesList, NoteClickListener noteClickListener) {
        this.notesList = notesList;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NotesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notes_tiles, parent, false);
        return new ViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerAdapter.ViewHolder holder, int position) {

        Note note = notesList.get(position);

        TextView noteHeadingTextView = holder.noteHeaderTextView;
        TextView noteBodyTextView = holder.noteBodyTextView;

        CardView noteCardView = holder.noteCardView;

        noteHeadingTextView.setText(note.getHeading());
        noteBodyTextView.setText(note.getBody());

        int pos = holder.getAdapterPosition();

        noteCardView.setOnClickListener(v -> {
            if (pos == RecyclerView.NO_POSITION) {
                return;
            }

            noteClickListener.onNoteClick(pos);
        });

        noteCardView.setOnLongClickListener(v -> {
            if (pos == RecyclerView.NO_POSITION) {
                return true;
            }

            noteClickListener.onNoteLongClick(pos);

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * ViewHolder for the {@code NotesRecyclerAdapter}.
     *
     * @author Andrew Jones
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder RecyclerView.ViweHolder
     * @since 1.1.0-beta.2
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteHeaderTextView;
        private final TextView noteBodyTextView;

        private final CardView noteCardView;

        /**
         * Get a new instance of this ViewHolder.
         *
         * @param itemView inflated layout resource for each note.
         * @since 1.1.0-beta.2
         */

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteHeaderTextView = itemView.findViewById(R.id.note_heading_textview);
            noteBodyTextView = itemView.findViewById(R.id.note_body_textview);
            noteCardView = itemView.findViewById(R.id.note_cardview);
        }

    }
}
