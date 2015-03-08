package com.pitchedapps.material.glass;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ivon on 07/03/15.
 */
public class Util {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setUpFab(View fabContainer, int iconRes, String tag, View.OnClickListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabContainer.setOutlineProvider(new FABOutlineProvider());
            fabContainer.setTag(tag);
            fabContainer.setOnClickListener(listener);
        } else {
            View fabCircle = fabContainer.findViewById(R.id.fab_circle);
            fabCircle.setTag(tag);
            fabCircle.setOnClickListener(listener);
        }
        ImageView fabIcon = (ImageView) fabContainer.findViewById(R.id.fab_icon);
        fabIcon.setImageResource(iconRes);
    }

    /**
     * Get bottom of circular part of fab, relative to the top of the fabContainer.
     * @param fabContainer
     *          The fab container to calculate bottom of circle for
     * @return  Position of the bottom of the circle, in pixels
     */
    public static float getBottomOfFabCircle(Context context, View fabContainer) {
        float bottom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottom = fabContainer.getHeight();
        } else {
            bottom = fabContainer.getHeight() - Util.convertDpToPixel(8 /* in dp, "leftover" space for shadow */, context);
        }
        return bottom;
    }

    public static ImageView getFabIconView(View fabContainer) {
        return (ImageView) fabContainer.findViewById(R.id.fab_icon);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);

        return px;
    }

}
