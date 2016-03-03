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

package com.pitchedapps.material.glass.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

public class WallpaperToCrop extends AsyncTask<Void, String, Boolean> {

    private Activity activity;
    private MaterialDialog dialog;
    private Bitmap resource;
    private Uri wallUri;
    private Context context;
    private View layout;
    private FloatingActionMenu fab;
    private String wallName;
    private WeakReference<Activity> wrActivity;
    private LinearLayout toHide1, toHide2;

    public WallpaperToCrop(Activity activity, MaterialDialog dialog, Bitmap resource,
                           View layout, FloatingActionMenu fab, String wallName) {
        this.wrActivity = new WeakReference<>(activity);
        this.dialog = dialog;
        this.resource = resource;
        this.layout = layout;
        this.fab = fab;
        this.wallName = wallName;
    }

    public WallpaperToCrop(Activity activity, MaterialDialog dialog, Bitmap resource,
                           View layout, FloatingActionMenu fab, String wallName, LinearLayout toHide1, LinearLayout toHide2) {
        this.wrActivity = new WeakReference<>(activity);
        this.dialog = dialog;
        this.resource = resource;
        this.layout = layout;
        this.fab = fab;
        this.wallName = wallName;
        this.toHide1 = toHide1;
        this.toHide2 = toHide2;
    }

    @Override
    protected void onPreExecute() {
        final Activity a = wrActivity.get();
        if (a != null) {
            this.context = a.getApplicationContext();
            this.activity = a;
        }

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean worked;
        if (wallUri != null) {
            wallUri = null;
        }
        try {
            wallUri = getImageUri(context, resource);
            worked = wallUri != null;
        } catch (Exception e) {
            worked = false;
        }
        return worked;
    }

    @Override
    protected void onPostExecute(Boolean worked) {
        final Preferences mPrefs = new Preferences(context);
        if (toHide1 != null && toHide2 != null) {
            toHide1.setVisibility(View.GONE);
            toHide2.setVisibility(View.GONE);
        }
        if (worked) {
            dialog.dismiss();
            Intent setWall = new Intent(Intent.ACTION_ATTACH_DATA);
            setWall.setDataAndType(wallUri, "image/*");
            setWall.putExtra("png", "image/*");
            activity.startActivityForResult(Intent.createChooser(setWall,
                    context.getResources().getString(R.string.set_as)), 1);
        } else {
            dialog.dismiss();
            Snackbar snackbar = Snackbar.make(layout,
                    context.getResources().getString(R.string.error), Snackbar.LENGTH_SHORT);
            final int snackbarLight = ContextCompat.getColor(context, R.color.snackbar_light);
            final int snackbarDark = ContextCompat.getColor(context, R.color.snackbar_dark);
            ViewGroup snackbarView = (ViewGroup) snackbar.getView();
            snackbarView.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
            snackbar.show();
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    if (toHide1 != null && toHide2 != null) {
                        toHide1.setVisibility(View.VISIBLE);
                        toHide2.setVisibility(View.VISIBLE);
                    }
                    if (fab != null) {
                        fab.showMenuButton(mPrefs.getAnimationsEnabled());
                    }
                }
            });
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        Preferences mPrefs = new Preferences(inContext);
        File downloadsFolder;

        if (inImage.isRecycled()) {
            inImage = inImage.copy(Bitmap.Config.ARGB_8888, false);
        }

        if (mPrefs.getDownloadsFolder() != null) {
            downloadsFolder = new File(mPrefs.getDownloadsFolder());
        } else {
            downloadsFolder = new File(context.getString(R.string.walls_save_location,
                    Environment.getExternalStorageDirectory().getAbsolutePath()));
        }

        downloadsFolder.mkdirs();

        File destFile = new File(downloadsFolder, wallName + ".png");

        if (!destFile.exists()) {
            try {
                inImage.compress(Bitmap.CompressFormat.PNG, 100,
                        new FileOutputStream(destFile));
            } catch (final Exception e) {
                Utils.showLog(context, e.getLocalizedMessage());
            }
        }

        return Uri.fromFile(destFile);
    }

}
