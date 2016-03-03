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

package com.pitchedapps.material.glass.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.florent37.glidepalette.GlidePalette;
import com.mikepenz.materialize.util.UIUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.concurrent.Callable;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.adapters.WallpapersAdapter;
import com.pitchedapps.material.glass.utilities.color.ColorUtils;
import com.pitchedapps.material.glass.utilities.color.ToolbarColorizer;
import com.pitchedapps.material.glass.views.CustomCoordinatorLayout;

/**
 * With a little help from Aidan Follestad (afollestad)
 */
public class Utils {

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // this should never happen
            return "Unknown";
        }
    }

    public static void log(String s) {
        Log.e("MGX", s);
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void showSimpleSnackbar(Context context, View location, String text, int duration) {
        final int snackbarLight = ContextCompat.getColor(context, R.color.snackbar_light);
        final int snackbarDark = ContextCompat.getColor(context, R.color.snackbar_dark);

        switch (duration) {
            case 1:
                Snackbar shortSnackbar = Snackbar.make(location, text,
                        Snackbar.LENGTH_SHORT);
                ViewGroup shortGroup = (ViewGroup) shortSnackbar.getView();
                shortGroup.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
                shortSnackbar.show();
                break;
            case 2:
                Snackbar longSnackbar = Snackbar.make(location, text,
                        Snackbar.LENGTH_LONG);
                ViewGroup longGroup = (ViewGroup) longSnackbar.getView();
                longGroup.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
                longSnackbar.show();
                break;
            case 3:
                Snackbar indefiniteSnackbar = Snackbar.make(location, text,
                        Snackbar.LENGTH_INDEFINITE);
                ViewGroup indefiniteGroup = (ViewGroup) indefiniteSnackbar.getView();
                indefiniteGroup.setBackgroundColor(ThemeUtils.darkTheme ? snackbarDark : snackbarLight);
                indefiniteSnackbar.show();
                break;
        }
    }

    public static void openLink(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openLinkInChromeCustomTab(Context context, String link) {
        final CustomTabsClient[] mClient = new CustomTabsClient[1];
        final CustomTabsSession[] mCustomTabsSession = new CustomTabsSession[1];
        CustomTabsServiceConnection mCustomTabsServiceConnection;
        CustomTabsIntent customTabsIntent;

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient[0] = customTabsClient;
                mClient[0].warmup(0L);
                mCustomTabsSession[0] = mClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient[0] = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession[0])
                .setToolbarColor(ThemeUtils.darkTheme ?
                        ContextCompat.getColor(context, R.color.dark_theme_primary_dark) :
                        ContextCompat.getColor(context, R.color.light_theme_primary_dark))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl((Activity) context, Uri.parse(link));
    }

    public static void showLog(Context context, String s) {
        String tag = "IconShowcase + " + context.getResources().getString(R.string.app_name);
        Log.d(tag, s);
    }

    public static void showAppFilterLog(Context context, String s) {
        String tag = context.getResources().getString(R.string.app_name) + " AppFilter";
        Log.d(tag, s);
    }

    public static void showLog(String s) {
        Log.d("IconShowcase ", s);
    }

    public static String getStringFromResources(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String makeTextReadable(String name) {
        String partialConvertedText = name.replaceAll("_", " ");
        String[] text = partialConvertedText.split("\\s+");
        StringBuilder sb = new StringBuilder();
        if (text[0].length() > 0) {
            sb.append(Character.toUpperCase(text[0].charAt(0))).append(text[0].subSequence(1, text[0].length()).toString().toLowerCase());
            for (int i = 1; i < text.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(text[i].charAt(0))).append(text[i].subSequence(1, text[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String capitalizeText(String text) {
        return text.substring(0, 1).toUpperCase(Locale.getDefault()) + text.substring(1);
    }

    public static void forceCrash() {
        throw new RuntimeException("This is a crash");
    }

    public static void sendEmailWithDeviceInfo(Context context) {
        StringBuilder emailBuilder = new StringBuilder();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + context.getResources().getString(R.string.email_id)));
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.email_subject));
        emailBuilder.append("\n \n \nOS Version: ").append(System.getProperty("os.version")).append("(").append(Build.VERSION.INCREMENTAL).append(")");
        emailBuilder.append("\nOS API Level: ").append(Build.VERSION.SDK_INT);
        emailBuilder.append("\nDevice: ").append(Build.DEVICE);
        emailBuilder.append("\nManufacturer: ").append(Build.MANUFACTURER);
        emailBuilder.append("\nModel (and Product): ").append(Build.MODEL).append(" (").append(Build.PRODUCT).append(")");
        PackageInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert appInfo != null;
        emailBuilder.append("\nApp Version Name: ").append(appInfo.versionName);
        emailBuilder.append("\nApp Version Code: ").append(appInfo.versionCode);
        intent.putExtra(Intent.EXTRA_TEXT, emailBuilder.toString());
        context.startActivity(Intent.createChooser(intent, (context.getResources().getString(R.string.send_title))));
    }

    public static GlidePalette getGlidePalette(String profile, boolean withTintedTexts, Preferences mPrefs,
                                               String wallUrl, WallpapersAdapter.WallsHolder holder) {

        GlidePalette palette;

        int colorSwatch = GlidePalette.Profile.VIBRANT;

        switch (profile) {
            case "VIBRANT":
                colorSwatch = GlidePalette.Profile.VIBRANT;
                break;
            case "VIBRANT_LIGHT":
                colorSwatch = GlidePalette.Profile.VIBRANT_LIGHT;
                break;
            case "VIBRANT_DARK":
                colorSwatch = GlidePalette.Profile.VIBRANT_LIGHT;
                break;
            case "MUTED":
                colorSwatch = GlidePalette.Profile.MUTED;
                break;
            case "MUTED_LIGHT":
                colorSwatch = GlidePalette.Profile.MUTED_LIGHT;
                break;
            case "MUTED_DARK":
                colorSwatch = GlidePalette.Profile.MUTED_LIGHT;
                break;
        }

        if (withTintedTexts) {
            palette = GlidePalette.with(wallUrl)
                    .use(colorSwatch)
                    .intoBackground(holder.titleBg, GlidePalette.Swatch.RGB)
                    .intoTextColor(holder.name, GlidePalette.Swatch.TITLE_TEXT_COLOR)
                    .intoTextColor(holder.authorName, GlidePalette.Swatch.BODY_TEXT_COLOR)
                    .crossfade(mPrefs.getAnimationsEnabled());
        } else {
            palette = GlidePalette.with(wallUrl)
                    .use(colorSwatch)
                    .intoBackground(holder.titleBg, GlidePalette.Swatch.RGB)
                    .crossfade(mPrefs.getAnimationsEnabled());
        }

        return palette;
    }

    /***
     * Method gets executed once the view is displayed
     *
     * @param view   A view Object
     * @param method A callable method implementation
     */
    public static void triggerMethodOnceViewIsDisplayed(final View view, final Callable<Void> method) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void collapseToolbar(Context context) {
        Preferences mPrefs = new Preferences(context);
        AppBarLayout appbar = (AppBarLayout) ((Activity) context).findViewById(R.id.appbar);
        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) ((Activity) context).findViewById(R.id.mainCoordinatorLayout);
        appbar.setExpanded(false, mPrefs.getAnimationsEnabled());
        coordinatorLayout.setScrollAllowed(false);
    }

    public static void expandToolbar(Context context) {
        Preferences mPrefs = new Preferences(context);
        AppBarLayout appbar = (AppBarLayout) ((Activity) context).findViewById(R.id.appbar);
        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) ((Activity) context).findViewById(R.id.mainCoordinatorLayout);
        appbar.setExpanded(true, mPrefs.getAnimationsEnabled());
        coordinatorLayout.setScrollAllowed(true);
    }

    public static void setupToolbarIconsAndTextsColors(Context context, AppBarLayout appbar,
                                                       final Toolbar toolbar, final Bitmap bitmap) {

        final int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(context, R.color.toolbar_text_dark) :
                ContextCompat.getColor(context, R.color.toolbar_text_light);

        int paletteGeneratedColor = 0;

        if(context.getResources().getBoolean(R.bool.use_palette_api_in_toolbar)){
            paletteGeneratedColor = getIconsColorFromBitmap(bitmap, context);
            if (paletteGeneratedColor == 0) {
                if (ColorUtils.isDark(bitmap, 0, bitmap.getHeight() / 2, true)) {
                    paletteGeneratedColor = Color.parseColor("#80ffffff");
                } else {
                    paletteGeneratedColor = Color.parseColor("#80000000");
                }
            }
        } else {
            paletteGeneratedColor = Color.parseColor("#b3ffffff");
        }

        final int finalPaletteGeneratedColor = paletteGeneratedColor;

        if (appbar != null) {
            appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    double alpha = round(((double) (verticalOffset * -1) / 288.0), 1);
                    int paletteColor = ColorUtils.blendColors(
                            finalPaletteGeneratedColor != 0 ? finalPaletteGeneratedColor : iconsColor,
                            iconsColor, alpha > 1.0 ? 1.0f : (float) alpha);
                    if (toolbar != null) {
                        ToolbarColorizer.colorizeToolbar(toolbar, paletteColor);
                    }
                }
            });
        }
    }

    public static void setupCollapsingToolbarTextColors(Context context,
                                                        CollapsingToolbarLayout collapsingToolbarLayout) {
        int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(context, R.color.toolbar_text_dark) :
                ContextCompat.getColor(context, R.color.toolbar_text_light);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(context, android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(iconsColor);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @SuppressWarnings("ConstantConditions")
    public static Bitmap getBitmapWithReplacedColor(@NonNull Bitmap bitmap, @ColorInt int colorToReplace, @ColorInt int replaceWith) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int minX = width;
        int minY = height;
        int maxX = -1;
        int maxY = -1;

        Bitmap newBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int pixel;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int index = y * width + x;
                pixel = pixels[index];
                if (pixel == colorToReplace) {
                    pixels[index] = replaceWith;
                }
                if (pixels[index] != replaceWith) {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return Bitmap.createBitmap(newBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public static int getIconsColorFromBitmap(Bitmap bitmap, Context context) {
        int color = 0;

        final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, context.getResources().getDisplayMetrics());

        final int pictureHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                256, context.getResources().getDisplayMetrics());

        int divider = (int) (twentyFourDip) + UIUtils.getStatusBarHeight(context, true);

        final int heightDivider = (int) (pictureHeight / divider);

        if (bitmap != null) {

            Palette palette = new Palette.Builder(bitmap)
                    .clearFilters()
                    .generate();

            boolean isDark;
            @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette, false);
            if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                isDark = ColorUtils.isDark(bitmap, true, 1, heightDivider, false);
            } else {
                isDark = lightness == ColorUtils.IS_DARK;
            }

            Palette.Swatch swatch1 = null, swatch2 = null, swatch3 = null, swatch4 = null;

            if (isDark) {
                swatch1 = palette.getVibrantSwatch();
                swatch2 = palette.getMutedSwatch();
            } else {
                swatch1 = palette.getDarkVibrantSwatch();
                swatch2 = palette.getDarkMutedSwatch();
            }

            swatch3 = palette.getLightVibrantSwatch();
            swatch4 = palette.getLightMutedSwatch();

            if (swatch1 != null) {
                color = swatch1.getRgb();
            } else if (swatch2 != null) {
                color = swatch2.getRgb();
            } else if (swatch3 != null && isDark) {
                color = swatch3.getRgb();
            } else if (swatch4 != null && isDark) {
                color = swatch4.getRgb();
            }

        }

        return color;
    }

    public static int getIconsColorForViewer(Bitmap bitmap, Context context) {
        int color = 0;

        final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, context.getResources().getDisplayMetrics());

        if (bitmap != null) {

            Palette palette = new Palette.Builder(bitmap)
                    .clearFilters()
                    .setRegion(0, 0, twentyFourDip, bitmap.getHeight() - 1)
                    .generate();

            boolean isDark;
            @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette, true);
            if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                isDark = ColorUtils.isDark(bitmap, 0, bitmap.getHeight() / 2, true);
            } else {
                isDark = lightness == ColorUtils.IS_DARK;
            }

            Palette.Swatch swatch1 = null, swatch2 = null, swatch3 = null, swatch4 = null;

            if (isDark) {
                swatch1 = palette.getVibrantSwatch();
                swatch2 = palette.getMutedSwatch();
            } else {
                swatch1 = palette.getDarkVibrantSwatch();
                swatch2 = palette.getDarkMutedSwatch();
            }

            swatch3 = palette.getLightVibrantSwatch();
            swatch4 = palette.getLightMutedSwatch();

            if (swatch1 != null) {
                color = swatch1.getRgb();
            } else if (swatch2 != null) {
                color = swatch2.getRgb();
            } else if (swatch3 != null && isDark) {
                color = swatch3.getRgb();
            } else if (swatch4 != null && isDark) {
                color = swatch4.getRgb();
            }

        }

        return color;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int convertMinutesToMillis(int minute) {
        return minute * 60 * 1000;
    }

    public static int convertMillisToMinutes(int millis) {
        return millis / 60 / 1000;
    }

    public static int convertMillisToHours(long millis) {
        return (int) millis / 60 / 60 / 1000;
    }

}