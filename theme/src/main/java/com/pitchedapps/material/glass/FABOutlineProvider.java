package com.pitchedapps.material.glass;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by ivon on 1/18/15.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FABOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        int size = view.getContext().getResources().getDimensionPixelSize(R.dimen.floating_button_size);
        outline.setOval(0, 0, size, size);
    }
}
