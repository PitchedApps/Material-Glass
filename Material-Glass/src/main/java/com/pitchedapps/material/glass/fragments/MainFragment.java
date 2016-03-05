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

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.utilities.LauncherIntents;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;

public class MainFragment extends Fragment {

    private Context context;

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private static final String PITCHED_GLASS = "com.pitchedapps.icons.glass";
    private static final String MATERIAL_GLASS = "com.pitchedapps.material.glass.free";
    private ViewGroup layout;

    private boolean cm, cyngn, rro; //to store theme engine installation status

    private ImageView iPlay, iPGlass, iGPlus, iXDA;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        try {
            layout = (ViewGroup) inflater.inflate(R.layout.main_section, container, false);
        } catch (InflateException e) {
            //Do nothing
        }

        iPlay = (ImageView) layout.findViewById(R.id.home_play_image);
        iPGlass = (ImageView) layout.findViewById(R.id.home_pitched_glass_image);
        iGPlus = (ImageView) layout.findViewById(R.id.home_gplus_image);
        iXDA = (ImageView) layout.findViewById(R.id.home_xda_image);

        setupIcons(getActivity());

        AppCompatButton iconsbtn = (AppCompatButton) layout.findViewById(R.id.rate_button);
        iconsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rate = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + MATERIAL_GLASS));
                startActivity(rate);
            }
        });

        AppCompatButton donatebtn = (AppCompatButton) layout.findViewById(R.id.donate_button);

        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowcaseActivity.drawerItemClick(ShowcaseActivity.donationsIdentifier);
                ShowcaseActivity.drawer.setSelection(ShowcaseActivity.donationsIdentifier);
            }
        });

        AppCompatButton emailbtn = (AppCompatButton) layout.findViewById(R.id.email_button);

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendEmailWithDeviceInfo(context);
            }
        });

        LinearLayout playCard = (LinearLayout) layout.findViewById(R.id.home_play);
        playCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLink(context, getResources().getString(R.string.dev_playstore));
            }
        });

        LinearLayout pitchedGlassCard = (LinearLayout) layout.findViewById(R.id.home_pitched_glass);
        View pitchedGlassDivider = layout.findViewById(R.id.home_pitched_glass_divider);
        if (Utils.isAppInstalled(context, PITCHED_GLASS)) {
            pitchedGlassCard.setVisibility(View.GONE);
            pitchedGlassDivider.setVisibility(View.GONE);
        } else {
            pitchedGlassCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.openLink(context, MARKET_URL + PITCHED_GLASS);
                }
            });
        }

        LinearLayout gPlusCard = (LinearLayout) layout.findViewById(R.id.home_gplus);
        gPlusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLink(context, getResources().getString(R.string.dev_gplus_community));
            }
        });

        LinearLayout xdaCard = (LinearLayout) layout.findViewById(R.id.home_xda);
        xdaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLink(context, getResources().getString(R.string.dev_xda));
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        showFAB();
        Utils.expandToolbar(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ShowcaseActivity.currentItem != 1) { //TODO figure out why I need this
            ShowcaseActivity.fab.setVisibility(View.GONE);
            ShowcaseActivity.fab.hide();
        }
    }

    private void showFAB() {
        modifyFABIcon();

        ShowcaseActivity.fab.setVisibility(View.VISIBLE);
        ShowcaseActivity.fab.show();
        ShowcaseActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cm || cyngn) {
                    new LauncherIntents(getActivity(), "Cmthemeengine");
                } else if (rro) {
                    new LauncherIntents(getActivity(), "Layers");
                } else {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.NTED_title)
                            .content(R.string.NTED_message)
                            .show();
                }
            }
        });
    }

    private void setupIcons(Context context) {
        final int light = ContextCompat.getColor(context, R.color.drawable_tint_dark);
        final int dark = ContextCompat.getColor(context, R.color.drawable_tint_light);

        Drawable iconsDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_android_alt)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable playStoreDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_case_play)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable gplusDrawable = ContextCompat.getDrawable(context, R.drawable.ic_gplus);
        gplusDrawable.setTint(ThemeUtils.darkTheme ? light : dark);

        Drawable xdaDrawable = ContextCompat.getDrawable(context, R.drawable.ic_xda);
        xdaDrawable.setTint(ThemeUtils.darkTheme ? light : dark);

        iPlay.setImageDrawable(playStoreDrawable);
        iPGlass.setImageDrawable(iconsDrawable);
        iGPlus.setImageDrawable(gplusDrawable);
        iXDA.setImageDrawable(xdaDrawable);
    }

    private void modifyFABIcon() {
        cm = Utils.isAppInstalled(context, "org.cyanogenmod.theme.chooser");
        cyngn = Utils.isAppInstalled(context, "com.cyngn.theme.chooser");
        //don't enable rro before lollipop, it didn't exist before that
        rro = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Utils.isAppInstalled(context, "com.lovejoy777.rroandlayersmanager");

        if (cm || cyngn) {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_apply_cm));
        } else if (rro) {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_apply_layers));
        } else {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_question));
        }
    }


}