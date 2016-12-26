/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package jahirfiquitiva.iconshowcase.utilities.color;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

import jahirfiquitiva.iconshowcase.R;

public class ToolbarColorizer {

    /**
     * Use this method to colorize toolbar icons to the desired target color
     *
     * @param toolbar           toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     */
    public static void colorizeToolbar(Toolbar toolbar, int toolbarIconsColor) {
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.SRC_IN);

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }

            if (v instanceof ActionMenuView) {
                for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {
                    //Step 2: Changing the color of any ActionMenuViews - icons that are not back button, nor text, nor overflow menu icon.
                    //Colorize the ActionViews -> all icons that are NOT: back button | overflow menu
                    final View innerView = ((ActionMenuView) v).getChildAt(j);
                    if (innerView instanceof ActionMenuItemView) {
                        for (int k = 0; k < ((ActionMenuItemView) innerView).getCompoundDrawables().length; k++) {
                            if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) {
                                final int finalK = k;

                                //Important to set the color filter in separate thread, by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbar.setTitleTextColor(toolbarIconsColor);
            toolbar.setSubtitleTextColor(toolbarIconsColor);
        }
    }

    public static void tintSaveIcon(MenuItem item, Context context, int color) {
        item.setIcon(
                getTintedIcon(
                        ContextCompat.getDrawable(context, R.drawable.ic_save),
                        color));
    }

    /**
     * This code was created by Aidan Follestad. Complete credits to him.
     */
    @SuppressWarnings("PrivateResource")
    public static void tintSearchView(Context context, @NonNull Toolbar toolbar, MenuItem item,
                                      @NonNull SearchView searchView, @ColorInt int color) {
        item.setIcon(getTintedIcon(
                ContextCompat.getDrawable(context, R.drawable.ic_search),
                color));
        final Class<?> cls = searchView.getClass();
        try {
            final Field mCollapseIconField = toolbar.getClass().getDeclaredField("mCollapseIcon");
            mCollapseIconField.setAccessible(true);
            final Drawable drawable = (Drawable) mCollapseIconField.get(toolbar);
            if (drawable != null)
                mCollapseIconField.set(toolbar, getTintedIcon(drawable, color));

            final Field mSearchSrcTextViewField = cls.getDeclaredField("mSearchSrcTextView");
            mSearchSrcTextViewField.setAccessible(true);
            final EditText mSearchSrcTextView = (EditText) mSearchSrcTextViewField.get(searchView);
            mSearchSrcTextView.setTextColor(color);
            mSearchSrcTextView.setHintTextColor(ColorUtils.adjustAlpha(color, 0.5f));
            setCursorTint(mSearchSrcTextView, color);

            Field field = cls.getDeclaredField("mSearchButton");
            tintImageView(searchView, field, color);
            field = cls.getDeclaredField("mGoButton");
            tintImageView(searchView, field, color);
            field = cls.getDeclaredField("mCloseButton");
            tintImageView(searchView, field, color);
            field = cls.getDeclaredField("mVoiceButton");
            tintImageView(searchView, field, color);
            field = cls.getDeclaredField("mCollapsedIcon");
            tintImageView(searchView, field, color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setCursorTint(@NonNull EditText editText, @ColorInt int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[0] = getTintedIcon(drawables[0], color);
            drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[1] = getTintedIcon(drawables[1], color);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tintImageView(Object target, Field field, int tintColor) throws Exception {
        field.setAccessible(true);
        final ImageView imageView = (ImageView) field.get(target);
        if (imageView == null) return;
        if (imageView.getDrawable() != null)
            imageView.setImageDrawable(getTintedIcon(imageView.getDrawable(), tintColor));
    }

    public static Drawable getTintedIcon(Drawable drawable, int color) {
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            DrawableCompat.setTint(drawable, color);
            return drawable;
        } else {
            return null;
        }
    }

}