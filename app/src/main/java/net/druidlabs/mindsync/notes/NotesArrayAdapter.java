package net.druidlabs.mindsync.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import net.druidlabs.mindsync.R;

import java.util.List;

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
        viewHolder.noteCardView.setOnLongClickListener(v -> {
            Toast.makeText(getContext(), note.getHeading(), Toast.LENGTH_SHORT).show();
            return true;
        });

        return convertView;
    }

    private static class ViewHolder {
        private CardView noteCardView;
        private TextView noteHeader;
        private TextView noteBody;
    }
}
