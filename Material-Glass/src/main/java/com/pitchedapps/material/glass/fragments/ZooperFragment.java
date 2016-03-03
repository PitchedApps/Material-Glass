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

package com.pitchedapps.material.glass.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.models.ZooperWidget;
import com.pitchedapps.material.glass.tasks.CopyFilesToStorage;
import com.pitchedapps.material.glass.tasks.LoadZooperWidgets;
import com.pitchedapps.material.glass.utilities.PermissionUtils;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;
import com.pitchedapps.material.glass.views.CustomCoordinatorLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ZooperFragment extends Fragment implements PermissionUtils.OnPermissionResultListener {

    private MaterialDialog dialog;
    private ViewGroup layout;
    private Context context;
    private CardView cardZooper, cardMU, cardMUInfo, installFonts, installIconsets, installBitmaps;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        boolean WITH_MEDIA_UTILITIES_WIDGETS = context.getResources().getBoolean(R.bool.mu_needed);

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        try {
            layout = (ViewGroup) inflater.inflate(R.layout.zooper_section, container, false);
        } catch (InflateException e) {
            //Do nothing
        }

        final int light = ContextCompat.getColor(context, R.color.drawable_tint_dark);
        final int dark = ContextCompat.getColor(context, R.color.drawable_tint_light);

        Drawable alert = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_alert_triangle)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable fonts = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_text_format)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable iconsets = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_toys)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable fire = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_fire)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable bitmaps = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_image_alt)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        ImageView fireIV = (ImageView) layout.findViewById(R.id.icon_preview);
        fireIV.setImageDrawable(fire);

        final ImageView preview = (ImageView) getActivity().findViewById(R.id.zooperWidget);

        final ArrayList<ZooperWidget> widgets = LoadZooperWidgets.widgets;

        if (context.getResources().getBoolean(R.bool.remove_zooper_previews_background)) {
            preview.setImageBitmap(widgets.get(i).getTransparentBackgroundPreview());
        } else {
            preview.setImageBitmap(widgets.get(i).getPreview());
        }

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i += 1;
                if (widgets.size() > 0) {
                    if (context.getResources().getBoolean(R.bool.remove_zooper_previews_background)) {
                        preview.setImageBitmap(widgets.get(i).getTransparentBackgroundPreview());
                    } else {
                        preview.setImageBitmap(widgets.get(i).getPreview());
                    }
                    if (i == widgets.size() - 1) {
                        i = -1;
                    }
                }
            }
        });

        ImageView zooperIV = (ImageView) layout.findViewById(R.id.icon_zooper);
        zooperIV.setImageDrawable(alert);

        ImageView muIV = (ImageView) layout.findViewById(R.id.icon_mu);
        muIV.setImageDrawable(alert);

        ImageView fontsIV = (ImageView) layout.findViewById(R.id.icon_fonts);
        fontsIV.setImageDrawable(fonts);

        ImageView iconsetsIV = (ImageView) layout.findViewById(R.id.icon_iconsets);
        iconsetsIV.setImageDrawable(iconsets);

        ImageView bitmapsIV = (ImageView) layout.findViewById(R.id.icon_bitmaps);
        bitmapsIV.setImageDrawable(bitmaps);

        cardZooper = (CardView) layout.findViewById(R.id.zooper_card);

        if (Utils.isAppInstalled(context, "org.zooper.zwpro")) {
            cardZooper.setVisibility(View.GONE);
        } else {
            cardZooper.setVisibility(View.VISIBLE);
        }

        AppCompatButton downloadZooper = (AppCompatButton) layout.findViewById(R.id.download_button);
        downloadZooper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new MaterialDialog.Builder(context)
                        .title(R.string.zooper_download_dialog_title)
                        .items(R.array.zooper_download_dialog_options)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int selection, CharSequence text) {
                                switch (selection) {
                                    case 0:
                                        Utils.openLinkInChromeCustomTab(context,
                                                "https://play.google.com/store/apps/details?id=org.zooper.zwpro");
                                        break;
                                    case 1:
                                        if (Utils.isAppInstalled(context, "com.amazon.venezia")) {
                                            Utils.openLinkInChromeCustomTab(context,
                                                    "amzn://apps/android?p=org.zooper.zwpro");
                                        } else {
                                            Utils.openLinkInChromeCustomTab(context,
                                                    "http://www.amazon.com/gp/mas/dl/android?p=org.zooper.zwpro");
                                        }
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

        cardMU = (CardView) layout.findViewById(R.id.mu_card);
        cardMUInfo = (CardView) layout.findViewById(R.id.mediautilities_info_card);

        if (WITH_MEDIA_UTILITIES_WIDGETS) {
            if (!Utils.isAppInstalled(context, "com.batescorp.notificationmediacontrols.alpha")) {
                cardMU.setVisibility(View.VISIBLE);
                cardMUInfo.setVisibility(View.GONE);
            } else {
                cardMU.setVisibility(View.GONE);
                cardMUInfo.setVisibility(View.VISIBLE);
            }
        } else {
            cardMU.setVisibility(View.GONE);
            cardMUInfo.setVisibility(View.GONE);
        }

        AppCompatButton downloadMU = (AppCompatButton) layout.findViewById(R.id.mu_download_button);
        downloadMU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        "https://play.google.com/store/apps/details?id=com.batescorp.notificationmediacontrols.alpha");
            }
        });

        AppCompatButton openMU = (AppCompatButton) layout.findViewById(R.id.mu_open_button);
        openMU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent muIntent = context.getPackageManager().getLaunchIntentForPackage(
                        "com.batescorp.notificationmediacontrols.alpha");
                startActivity(muIntent);
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.expandToolbar(getActivity());
        GridLayout grid = (GridLayout) getActivity().findViewById(R.id.iconsRow);
        grid.setVisibility(View.GONE);
        ImageView preview = (ImageView) getActivity().findViewById(R.id.zooperWidget);
        preview.setVisibility(View.VISIBLE);
        if (layout != null) {
            setupCards(true, true, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GridLayout grid = (GridLayout) getActivity().findViewById(R.id.iconsRow);
        grid.setVisibility(View.VISIBLE);
        ImageView preview = (ImageView) getActivity().findViewById(R.id.zooperWidget);
        preview.setVisibility(View.INVISIBLE);
    }

    private void setupCards(boolean fonts, boolean iconsets, boolean bitmaps) {

        final String fontsFolder = "fonts", iconsetsFolder = "iconsets", bitmapsFolder = "bitmaps";

        installFonts = (CardView) layout.findViewById(R.id.fonts_card);
        if (fonts) {
            if (checkAssetsInstalled(fontsFolder)) {
                installFonts.setVisibility(View.GONE);
            } else {
                installFonts.setVisibility(View.VISIBLE);
                installFonts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkAssetsInstalled(fontsFolder)) {
                            if (!PermissionUtils.canAccessStorage(getContext())) {
                                PermissionUtils.requestStoragePermission(getActivity(),
                                        ZooperFragment.this,
                                        fontsFolder);
                            } else {
                                installFonts(fontsFolder);
                            }
                        } else {
                            String snackBarContext = getActivity().getResources().getString(
                                    R.string.assets_installed, Utils.capitalizeText(fontsFolder));
                            Utils.showSimpleSnackbar(context, layout,
                                    snackBarContext, 1);
                            setupCards(true, false, false);
                        }
                    }
                });
            }
        }

        installIconsets = (CardView) layout.findViewById(R.id.iconsets_card);
        if (iconsets) {
            if (checkAssetsInstalled(iconsetsFolder)) {
                installIconsets.setVisibility(View.GONE);
            } else {
                installIconsets.setVisibility(View.VISIBLE);
                installIconsets.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkAssetsInstalled(iconsetsFolder)) {
                            if (!PermissionUtils.canAccessStorage(getContext())) {
                                PermissionUtils.requestStoragePermission(getActivity(),
                                        ZooperFragment.this,
                                        iconsetsFolder);
                            } else {
                                installFonts(iconsetsFolder);
                            }
                        } else {
                            String snackBarContext =
                                    getActivity().getResources().getString(
                                            R.string.assets_installed, Utils.capitalizeText(iconsetsFolder));
                            Utils.showSimpleSnackbar(context, layout,
                                    snackBarContext, 1);
                            setupCards(false, true, false);
                        }
                    }
                });
            }
        }

        installBitmaps = (CardView) layout.findViewById(R.id.bitmaps_card);
        if (bitmaps) {
            if (checkAssetsInstalled(bitmapsFolder)) {
                installBitmaps.setVisibility(View.GONE);
            } else {
                installBitmaps.setVisibility(View.VISIBLE);
                installBitmaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkAssetsInstalled(bitmapsFolder)) {
                            if (!PermissionUtils.canAccessStorage(getContext())) {
                                PermissionUtils.requestStoragePermission(getActivity(),
                                        ZooperFragment.this,
                                        bitmapsFolder);
                            } else {
                                installFonts(bitmapsFolder);
                            }
                        } else {
                            String snackBarContext =
                                    getActivity().getResources().getString(
                                            R.string.assets_installed, Utils.capitalizeText(bitmapsFolder));
                            Utils.showSimpleSnackbar(context, layout,
                                    snackBarContext, 1);
                            setupCards(false, false, true);
                        }
                    }
                });
            }
        }

        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) ((Activity) context).findViewById(R.id.mainCoordinatorLayout);
        coordinatorLayout.setScrollAllowed(getShownCardsNumber() > 3);
    }

    private boolean checkAssetsInstalled(String folder) {
        boolean assetsInstalled = true;

        String fileToIgnore1 = "material-design-iconic-font-v2.2.0.ttf";
        String fileToIgnore2 = "materialdrawerfont.ttf";

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(folder);
        } catch (IOException e) {
            //Do nothing
        }

        if (files != null && files.length > 0) {
            for (String filename : files) {
                if (!filename.equals(fileToIgnore1) && !filename.equals(fileToIgnore2)) {
                    File file = new File(Environment.getExternalStorageDirectory() + "/ZooperWidget/" + getFolderName(folder) + "/" + filename);
                    if (!file.exists()) {
                        assetsInstalled = false;
                    }
                }
            }
        }

        return assetsInstalled;
    }

    private String getFolderName(String folder) {
        switch (folder) {
            case "fonts":
                return "Fonts";
            case "iconsets":
                return "IconSets";
            case "bitmaps":
                return "Bitmaps";
            default:
                return folder;
        }
    }

    private void installFonts(String folderName) {
        String dialogContent =
                getActivity().getResources().getString(
                        R.string.copying_assets, getFolderName(folderName));
        dialog = new MaterialDialog.Builder(context)
                .content(dialogContent)
                .progress(true, 0)
                .cancelable(false)
                .show();
        new CopyFilesToStorage(context, dialog, folderName).execute();
    }

    private int getShownCardsNumber() {
        int num = 1;

        if (cardZooper.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        if (cardMU.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        if (cardMUInfo.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        if (installFonts.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        if (installIconsets.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        if (installBitmaps.getVisibility() == View.VISIBLE) {
            num += 1;
        }

        return num;
    }

    @Override
    public void onStoragePermissionGranted() {
        installFonts(PermissionUtils.folderName);
    }
}