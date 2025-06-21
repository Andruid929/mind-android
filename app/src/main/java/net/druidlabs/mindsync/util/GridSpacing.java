package net.druidlabs.mindsync.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Customise the spacing between notes.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.1.0-beta.2
 */

public final class GridSpacing extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing;

    /**
     * Get a new instance of the class and specify the span count and spacing in pixels.
     *
     * @param spanCount the number of columns the grid should have.
     * @param spacing   the number of pixels between each note.
     */

    public GridSpacing(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); //Note position/index
        int column = position % spanCount; //Note column

        outRect.left = spacing - column / spanCount;

        outRect.right = (column + 1) * spacing / spanCount;

        if (position < spanCount) { //Top edge
            outRect.top = spacing;
        }

        outRect.bottom = spacing;
    }
}
