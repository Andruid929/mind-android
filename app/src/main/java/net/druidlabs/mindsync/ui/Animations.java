package net.druidlabs.mindsync.ui;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.druidlabs.mindsync.R;

/**
 *  Enum for various animations to be used in the UI.
 *
 * @author Andrew Jones
 * @since 0.2.0
 * @version 1.0
 * */

public enum Animations {

    /**
     * Rotate the view 90 degrees clockwise.
     * */

    ROTATE_90_CLOCKWISE(R.anim.rotate_add_btn_open_anim),

    /**
     * Rotate the view 90 degrees anti-clockwise.
     * */

    ROTATE_90_ANTI_CLOCKWISE(R.anim.rotate_add_btn_close_anim),

    /**
     * Make the view pop up a few dp the bottom.
     * */

    FROM_BOTTOM(R.anim.from_bottom_anim),

    /**
     * Make the view move downwards a few dp.
     * */

    TO_BOTTOM(R.anim.to_bottom_anim);

    private final int animResId;

    Animations(int animResId) {
        this.animResId = animResId;
    }

    /**
     * Get this animation passing in the context to be used.
     *
     * @return {@link Animation} object for this animation.
     * @param context activity to which this animation is to be played.
     * */

    public Animation getAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, animResId);
    }

}
