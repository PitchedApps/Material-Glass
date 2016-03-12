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

package com.pitchedapps.material.glass.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.adapters.ChangelogAdapter;
import com.pitchedapps.material.glass.fragments.WallpapersFragment;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;

/**
 * This Class was created by Patrick Jung
 * on 07.01.16. For more Details and Licensing
 * have a look at the README.md
 */

public final class ISDialogs {

    /*
    Dialogs used in the Showcase Activity
     */

    public static void showChangelogDialog(Context context) {
        if (context.getResources().getBoolean(R.bool.changelog_ripples)) {
            ShowcaseActivity.changelog = new MaterialDialog.Builder(context)
                    .title(R.string.changelog_dialog_title)
                    .adapter(new ChangelogAdapter(context, R.array.fullchangelog), null)
                    .positiveText(R.string.great)
                    .build();
            ShowcaseActivity.changelog.show();
        } else {
            ShowcaseActivity.changelog = new MaterialDialog.Builder(context)
                    .title(R.string.changelog_dialog_title)
                    .adapter(new ChangelogAdapter(context, R.array.fullchangelog), null)
                    .positiveText(R.string.great)
                    .listSelector(android.R.color.transparent)
                    .build();
            ShowcaseActivity.changelog.show();
        }
    }

    /*
    ViewerActivity Dialogs
     */

    public static void showApplyWallpaperDialog(Context context, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNeutral) {
        new MaterialDialog.Builder(context)
                .title(R.string.apply)
                .content(R.string.confirm_apply)
                .positiveText(R.string.apply)
                .neutralText(R.string.crop)
                .negativeText(android.R.string.cancel)
                .onPositive(onPositive)
                .onNeutral(onNeutral)
                .show();
    }

    public static MaterialDialog showDownloadDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content(R.string.downloading_wallpaper)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public static void showWallpaperDetailsDialog(final Context context, String wallName,
                                                  String wallAuthor, String wallDimensions,
                                                  String wallCopyright) {

        MaterialDialog dialog = new MaterialDialog.Builder(context).title(wallName)
                .customView(R.layout.wallpaper_details, false)
                .positiveText(context.getResources().getString(R.string.close))
                .build();

        View v = dialog.getCustomView();

        ImageView authorIcon, dimensIcon, copyrightIcon;

        authorIcon = (ImageView) v.findViewById(R.id.icon_author);
        dimensIcon = (ImageView) v.findViewById(R.id.icon_dimensions);
        copyrightIcon = (ImageView) v.findViewById(R.id.icon_copyright);

        int light = ContextCompat.getColor(context, R.color.drawable_tint_dark);
        int dark = ContextCompat.getColor(context, R.color.drawable_tint_light);

        authorIcon.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_account)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24));

        dimensIcon.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_aspect_ratio_alt)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24));

        copyrightIcon.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_info)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24));

        LinearLayout author = (LinearLayout) v.findViewById(R.id.authorName);
        LinearLayout dimensions = (LinearLayout) v.findViewById(R.id.wallDimensions);
        LinearLayout copyright = (LinearLayout) v.findViewById(R.id.wallCopyright);

        TextView authorText = (TextView) v.findViewById(R.id.wallpaper_author_text);
        TextView dimensionsText = (TextView) v.findViewById(R.id.wallpaper_dimensions_text);
        TextView copyrightText = (TextView) v.findViewById(R.id.wallpaper_copyright_text);

        if (wallAuthor.equals("null") || wallAuthor.equals("")) {
            author.setVisibility(View.GONE);
        } else {
            authorText.setText(wallAuthor);
        }

        if (wallDimensions.equals("null") || wallDimensions.equals("")) {
            dimensions.setVisibility(View.GONE);
        } else {
            dimensionsText.setText(wallDimensions);
        }

        if (wallCopyright.equals("null") || wallCopyright.equals("")) {
            copyright.setVisibility(View.GONE);
        } else {
            copyrightText.setText(wallCopyright);
        }

        dialog.show();
    }


    /*
    Apply Fragment Dialogs
     */

    public static void showOpenInPlayStoreDialog(Context context, String title, String content, MaterialDialog.SingleButtonCallback onPositive) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(onPositive)
                .show();
    }

    public static void showGoogleNowLauncherDialog(Context context, MaterialDialog.SingleButtonCallback onPositive) {
        new MaterialDialog.Builder(context)
                .title(R.string.gnl_title)
                .content(R.string.gnl_content)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(onPositive)
                .show();
    }

    public static void showApplyAdviceDialog(Context context, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(R.string.advice)
                .content(R.string.apply_advice)
                .positiveText(R.string.close)
                .neutralText(R.string.dontshow)
                .onAny(callback)
                .show();
    }

    /*
    Request Fragment Dialogs
     */

    public static void showPermissionNotGrantedDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.md_error_label)
                .content(context.getResources().getString(R.string.md_storage_perm_error, R.string.app_name))
                .positiveText(android.R.string.ok)
                .show();
    }

    public static MaterialDialog showBuildingRequestDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content(R.string.building_request_dialog)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public static void showNoSelectedAppsDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.no_selected_apps_title)
                .content(R.string.no_selected_apps_content)
                .positiveText(android.R.string.ok)
                .show();
    }

    public static void showRequestLimitDialog(Context context, int maxApps) {
        String content = context.getResources().getString(R.string.apps_limit_dialog, String.valueOf(maxApps));
        new MaterialDialog.Builder(context)
                .title(R.string.section_icon_request)
                .content(content)
                .positiveText(android.R.string.ok)
                .show();
    }

    public static void showRequestLimitDayDialog(Context context, int hours) {
        String content = context.getResources().getString(R.string.apps_limit_dialog_day, String.valueOf(hours));
        new MaterialDialog.Builder(context)
                .title(R.string.section_icon_request)
                .content(content)
                .positiveText(android.R.string.ok)
                .show();
    }

    public static MaterialDialog showThemeChooserDialog(final Activity context) {
        int defaultTheme = context.getResources().getInteger(R.integer.default_theme) - 1;
        final int[] selectedTheme = {PreferenceManager.getDefaultSharedPreferences(context).getInt("theme", defaultTheme)};
        final int[] newSelectedTheme = new int[1];
        final Preferences mPrefs = new Preferences(context);
        return new MaterialDialog.Builder(context)
                .title(R.string.pref_title_themes)
                .items(R.array.themes)
                .itemsCallbackSingleChoice(selectedTheme[0], new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                ThemeUtils.changeToTheme(context, ThemeUtils.LIGHT);
                                break;
                            case 1:
                                ThemeUtils.changeToTheme(context, ThemeUtils.DARK);
                                break;
                            case 2:
                                ThemeUtils.changeToTheme(context, ThemeUtils.CLEAR);
                                break;
                            case 3:
                                ThemeUtils.changeToTheme(context, ThemeUtils.AUTO);
                                break;
                        }
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("theme", position).apply();
                        newSelectedTheme[0] = position;
                        if (newSelectedTheme[0] != selectedTheme[0]) {
                            mPrefs.setSettingsModified(true);
                            ThemeUtils.restartActivity(context);
                        }
                        return true;
                    }
                })
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build();
    }

    public static void showColumnsSelectorDialog(final Context context) {
        Preferences mPrefs = new Preferences(context);
        final int current = mPrefs.getWallsColumnsNumber();
        new MaterialDialog.Builder(context)
                .title(R.string.columns)
                .content(R.string.columns_desc)
                .items(R.array.columns_options)
                .itemsCallbackSingleChoice(current - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int position, CharSequence text) {
                        int newSelected = position + 1;
                        if (newSelected != current) {
                            WallpapersFragment.updateRecyclerView(newSelected);
                        }
                        return true;
                    }
                })
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .show();
    }

    public static void showSherryDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.sherry_title)
                .content(R.string.sherry_dialog)
                .neutralText(R.string.follow_her)
                .positiveText(R.string.close)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Utils.openLinkInChromeCustomTab(context,
                                context.getResources().getString(R.string.sherry_link));
                    }
                })
                .show();
    }

    public static void showUICollaboratorsDialog(final Context context, final String[] uiCollaboratorsLinks) {
        new MaterialDialog.Builder(context)
                .title(R.string.ui_design)
                .negativeText(R.string.close)
                .items(R.array.ui_collaborators_names)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view,
                                            final int i, CharSequence charSequence) {
                        Utils.openLinkInChromeCustomTab(context, uiCollaboratorsLinks[i]);
                    }
                }).show();
    }

    public static void showLibrariesDialog(final Context context, final String[] libsLinks) {
        new MaterialDialog.Builder(context)
                .title(R.string.implemented_libraries)
                .negativeText(R.string.close)
                .items(R.array.libs_names)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view,
                                            final int i, CharSequence charSequence) {
                        Utils.openLinkInChromeCustomTab(context, libsLinks[i]);
                    }
                }).show();
    }

    public static void showContributorsDialog(final Context context, final String[] contributorsLinks) {
        new MaterialDialog.Builder(context)
                .title(R.string.contributors)
                .negativeText(R.string.close)
                .items(R.array.contributors_names)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view,
                                            final int i, CharSequence charSequence) {
                        Utils.openLinkInChromeCustomTab(context, contributorsLinks[i]);
                    }
                }).show();
    }

    public static void showDesignerLinksDialog(final Context context, final String[] designerLinks) {
        new MaterialDialog.Builder(context)
                .title(R.string.more)
                .negativeText(R.string.close)
                .items(R.array.dev_sites)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view,
                                            final int i, CharSequence charSequence) {
                        Utils.openLinkInChromeCustomTab(context, designerLinks[i]);
                    }
                }).show();
    }

    public static void showTranslatorsDialogs(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.translators)
                .negativeText(R.string.close)
                .items(R.array.translators_names)
                .listSelector(android.R.color.transparent)
                .show();
    }

    /*
    Settings Fragment Dialogs
     */

    public static void showClearCacheDialog(Context context, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        new MaterialDialog.Builder(context)
                .title(R.string.clearcache_dialog_title)
                .content(R.string.clearcache_dialog_content)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(singleButtonCallback)
                .show();
    }

    public static MaterialDialog showHideIconDialog(Context context, MaterialDialog.SingleButtonCallback positive, MaterialDialog.SingleButtonCallback negative, DialogInterface.OnDismissListener dismissListener) {
        return new MaterialDialog.Builder(context)
                .title(R.string.hideicon_dialog_title)
                .content(R.string.hideicon_dialog_content)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive(positive)
                .onNegative(negative)
                .dismissListener(dismissListener)
                .show();
    }

    /*
    Another dialog
     */

    public static MaterialDialog showLoadingIconsDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content(R.string.loading_icons)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public static void showLoadingRequestAppsDialog(Context context) {
        new MaterialDialog.Builder(context)
                .content(R.string.loading_apps)
                .positiveText(android.R.string.ok)
                .show();
        }
}