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

package com.pitchedapps.material.glass.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.dialogs.ISDialogs;
import com.pitchedapps.material.glass.tasks.ApplyWallpaper;
import com.pitchedapps.material.glass.tasks.WallpaperToCrop;
import com.pitchedapps.material.glass.utilities.PermissionUtils;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;
import com.pitchedapps.material.glass.utilities.color.ColorUtils;
import com.pitchedapps.material.glass.utilities.color.ToolbarColorizer;
import com.pitchedapps.material.glass.views.TouchImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ViewerActivity extends AppCompatActivity {

    private boolean mLastTheme, mLastNavBar;

    public static final String EXTRA_CURRENT_ITEM_POSITION = "extra_current_item_position";

    private String transitionName, wallUrl, wallName, wallAuthor, wallDimensions, wallCopyright;
    private TouchImageView mPhoto;

    private RelativeLayout layout;
    private static Preferences mPrefs;
    private static File downloadsFolder;
    public static MaterialDialog dialogApply;
    private Toolbar toolbar;

    public static LinearLayout toHide1, toHide2;

    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThemeUtils.onActivityCreateSetNavBar(this);
            Window window = getWindow();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);

        context = this;

        mPrefs = new Preferences(context);

        Intent intent = getIntent();
        transitionName = intent.getStringExtra("transitionName");
        wallUrl = intent.getStringExtra("wallUrl");
        wallName = intent.getStringExtra("wallName");
        wallAuthor = intent.getStringExtra("authorName");
        wallDimensions = intent.getStringExtra("wallDimensions");
        wallCopyright = intent.getStringExtra("wallCopyright");

        setContentView(R.layout.wall_viewer_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(context, R.color.toolbar_text_dark) :
                ContextCompat.getColor(context, R.color.toolbar_text_light);

        ToolbarColorizer.colorizeToolbar(toolbar, iconsColor);

        toHide1 = (LinearLayout) findViewById(R.id.iconsA);
        toHide2 = (LinearLayout) findViewById(R.id.iconsB);

        int tintLightLighter = ContextCompat.getColor(context, R.color.drawable_base_tint);
        int tintLight = ContextCompat.getColor(context, R.color.drawable_tint_light);
        int tintDark = ContextCompat.getColor(context, R.color.drawable_tint_dark);

        Drawable save = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_save)
                .color(ThemeUtils.darkTheme ? tintDark : tintLightLighter)
                .sizeDp(24);

        Drawable apply = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_format_paint)
                .color(ThemeUtils.darkTheme ? tintDark : tintLightLighter)
                .sizeDp(24);

        Drawable info = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_info_outline)
                .color(ThemeUtils.darkTheme ? tintDark : tintLightLighter)
                .sizeDp(24);

        ImageView saveIV = (ImageView) findViewById(R.id.download);
        saveIV.setImageDrawable(save);
        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.canAccessStorage(context)) {
                    PermissionUtils.setViewerActivityAction("save");
                    PermissionUtils.requestStoragePermission(context);
                } else {
                    showDialogs("save");
                }
            }
        });

        ImageView applyIV = (ImageView) findViewById(R.id.apply);
        applyIV.setImageDrawable(apply);
        applyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.canAccessStorage(context)) {
                    PermissionUtils.setViewerActivityAction("apply");
                    PermissionUtils.requestStoragePermission(context);
                } else {
                    showDialogs("apply");
                }
            }
        });

        ImageView infoIV = (ImageView) findViewById(R.id.info);
        infoIV.setImageDrawable(info);
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showWallpaperDetailsDialog(context, wallName, wallAuthor,
                        wallDimensions, wallCopyright);
            }
        });

        mPhoto = (TouchImageView) findViewById(R.id.big_wallpaper);
        ViewCompat.setTransitionName(mPhoto, transitionName);

        layout = (RelativeLayout) findViewById(R.id.viewerLayout);

        TextView wallNameText = (TextView) findViewById(R.id.wallName);
        wallNameText.setText(wallName);

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = context.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress);
        spinner.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable d = new GlideBitmapDrawable(getResources(), bmp);

        Drawable errorIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_alert_triangle)
                .color(ThemeUtils.darkTheme ? tintDark : tintLight)
                .sizeDp(192);

        if (mPrefs.getAnimationsEnabled()) {
            Glide.with(context)
                    .load(wallUrl)
                    .placeholder(d)
                    .error(errorIcon)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap picture = ((GlideBitmapDrawable) resource).getBitmap();
                            colorizeToolbar(picture, context.getResources().getBoolean(R.bool.use_palette_api_in_viewer));
                            spinner.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mPhoto);
        } else {
            Glide.with(context)
                    .load(wallUrl)
                    .placeholder(d)
                    .error(errorIcon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap picture = ((GlideBitmapDrawable) resource).getBitmap();
                            colorizeToolbar(picture, context.getResources().getBoolean(R.bool.use_palette_api_in_viewer));
                            spinner.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mPhoto);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLastTheme = ThemeUtils.darkTheme;
        mLastNavBar = ThemeUtils.coloredNavBar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLastTheme != ThemeUtils.darkTheme
                || mLastNavBar != ThemeUtils.coloredNavBar) {
            ThemeUtils.restartActivity(context);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialogApply != null) {
            dialogApply.dismiss();
            dialogApply = null;
        }
    }

    @Override
    public void onBackPressed() {
        closeViewer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == PermissionUtils.PERMISSION_REQUEST_CODE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogs(PermissionUtils.getViewerActivityAction());
            } else {
                ISDialogs.showPermissionNotGrantedDialog(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //Crop request
            if (toHide1 != null && toHide2 != null) {
                toHide1.setVisibility(View.VISIBLE);
                toHide2.setVisibility(View.VISIBLE);
            }
        }
    }

    private void colorizeToolbar(Bitmap picture, boolean usePalette) {
        int paletteIconsColor = 0;
        if (usePalette) {
            paletteIconsColor = Utils.getIconsColorForViewer(picture, context);
            if (paletteIconsColor == 0) {
                int light = Color.parseColor("#80000000");
                int dark = Color.parseColor("#80ffffff");
                if (ColorUtils.isDark(picture, 0, picture.getHeight() / 2, true)) {
                    ToolbarColorizer.colorizeToolbar(toolbar, dark);
                } else {
                    ToolbarColorizer.colorizeToolbar(toolbar, light);
                }
            } else {
                ToolbarColorizer.colorizeToolbar(toolbar, paletteIconsColor);
            }
        } else {
            paletteIconsColor = Color.parseColor("b3ffffff");
            ToolbarColorizer.colorizeToolbar(toolbar, paletteIconsColor);
        }
    }

    private void closeViewer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }

    private void saveWallpaperAction(final String name, String url) {
        final MaterialDialog downloadDialog = ISDialogs.showDownloadDialog(context);
        downloadDialog.show();
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            saveWallpaper(context, name, downloadDialog, resource);
                        }
                    }
                });
    }

    private void saveWallpaper(final Activity context, final String wallName,
                               final MaterialDialog downloadDialog, final Bitmap result) {
        downloadDialog.setContent(context.getString(R.string.saving_wallpaper));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mPrefs.getDownloadsFolder() != null) {
                    downloadsFolder = new File(mPrefs.getDownloadsFolder());
                } else {
                    downloadsFolder = new File(context.getString(R.string.walls_save_location,
                            Environment.getExternalStorageDirectory().getAbsolutePath()));
                }
                downloadsFolder.mkdirs();
                final File destFile = new File(downloadsFolder, wallName + ".png");
                String snackbarText;
                if (!destFile.exists()) {
                    try {
                        result.compress(Bitmap.CompressFormat.PNG, 100,
                                new FileOutputStream(destFile));
                        snackbarText = context.getString(R.string.wallpaper_downloaded,
                                destFile.getAbsolutePath());
                    } catch (final Exception e) {
                        snackbarText = context.getString(R.string.error);
                    }
                } else {
                    snackbarText = context.getString(R.string.wallpaper_downloaded,
                            destFile.getAbsolutePath());
                }
                final String finalSnackbarText = snackbarText;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDialog.dismiss();

                        if (toHide1 != null && toHide2 != null) {
                            toHide1.setVisibility(View.GONE);
                            toHide2.setVisibility(View.GONE);
                        }

                        Snackbar longSnackbar = Snackbar.make(layout, finalSnackbarText,
                                Snackbar.LENGTH_LONG);
                        final int snackbarLight = ContextCompat.getColor(context, R.color.snackbar_light);
                        final int snackbarDark = ContextCompat.getColor(context, R.color.snackbar_dark);
                        ViewGroup snackbarView = (ViewGroup) longSnackbar.getView();
                        snackbarView.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
                        longSnackbar.show();
                        longSnackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                if (toHide1 != null && toHide2 != null) {
                                    toHide1.setVisibility(View.VISIBLE);
                                    toHide2.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void showApplyWallpaperDialog(final Activity context, final String wallUrl) {
        ISDialogs.showApplyWallpaperDialog(context,
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        dialogApply = new MaterialDialog.Builder(context)
                                .content(R.string.downloading_wallpaper)
                                .progress(true, 0)
                                .cancelable(false)
                                .show();
                        Glide.with(context)
                                .load(wallUrl)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        if (resource != null) {
                                            dialogApply.setContent(context.getString(R.string.setting_wall_title));
                                            new ApplyWallpaper(context, dialogApply, resource,
                                                    false, layout, null, toHide1, toHide2).execute();
                                        }
                                    }
                                });
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (dialogApply != null) {
                            dialogApply.dismiss();
                        }
                        dialogApply = new MaterialDialog.Builder(context)
                                .content(R.string.downloading_wallpaper)
                                .progress(true, 0)
                                .cancelable(false)
                                .show();
                        Glide.with(context)
                                .load(wallUrl)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        if (resource != null) {
                                            new WallpaperToCrop(context, dialogApply, resource,
                                                    layout, null, wallName, toHide1, toHide2).execute();
                                        }
                                    }
                                });
                    }
                });
    }

    private void showNotConnectedSnackBar(final FloatingActionMenu fab) {
        if (fab != null) {
            if (fab.isOpened()) {
                fab.close(mPrefs.getAnimationsEnabled());
            }
            fab.hideMenuButton(mPrefs.getAnimationsEnabled());
            fab.hideMenu(mPrefs.getAnimationsEnabled());
        }

        Snackbar notConnectedSnackBar = Snackbar.make(layout, R.string.no_conn_title,
                Snackbar.LENGTH_LONG);

        final int snackbarLight = ContextCompat.getColor(context, R.color.snackbar_light);
        final int snackbarDark = ContextCompat.getColor(context, R.color.snackbar_dark);
        ViewGroup snackbarView = (ViewGroup) notConnectedSnackBar.getView();
        snackbarView.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
        notConnectedSnackBar.show();
        notConnectedSnackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (fab != null) {
                    fab.showMenuButton(mPrefs.getAnimationsEnabled());
                }
            }
        });
        if (!notConnectedSnackBar.isShown()) {
            if (fab != null) {
                fab.showMenuButton(mPrefs.getAnimationsEnabled());
            }
        }
    }

    private void showDialogs(String action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            new MaterialDialog.Builder(context)
                    .title(R.string.md_error_label)
                    .content(context.getResources().getString(R.string.md_storage_perm_error,
                            context.getResources().getString(R.string.app_name)))
                    .positiveText(android.R.string.ok)
                    .show();
        } else {
            if (Utils.hasNetwork(context)) {
                switch (action) {
                    case "save":
                        saveWallpaperAction(wallName, wallUrl);
                        break;

                    case "apply":
                        showApplyWallpaperDialog(context, wallUrl);
                        break;
                }
            } else {
                showNotConnectedSnackBar(null);
            }
        }
    }
}