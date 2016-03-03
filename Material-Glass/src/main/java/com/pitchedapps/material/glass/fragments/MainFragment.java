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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.adapters.HomeListAdapter;
import com.pitchedapps.material.glass.models.HomeCard;
import com.pitchedapps.material.glass.utilities.LauncherIntents;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private Context context;

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private static final String PITCHED_GLASS = "com.pitchedapps.icons.glass";
    private static final String MATERIAL_GLASS = "com.pitchedapps.material.glass.free";
    private ViewGroup layout;

    private boolean cm, cyngn, rro; //to store theme engine installation status

    private RecyclerView mRecyclerView;
    private ArrayList<HomeCard> homeCards = new ArrayList<>();
    private Drawable iconsDrawable, playStoreDrawable;

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

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.home_rv);

        setupIcons(getActivity());

        AppCompatButton donatebtn = (AppCompatButton) layout.findViewById(R.id.rate_button);
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rate = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + MATERIAL_GLASS));
                startActivity(rate);
            }
        });

        AppCompatButton iconsbtn = (AppCompatButton) layout.findViewById(R.id.donate_button);

        iconsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowcaseActivity.drawerItemClick(ShowcaseActivity.donationsIdentifier);
                ShowcaseActivity.drawer.setSelection(ShowcaseActivity.donationsIdentifier);
            }
        });

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        homeCards.add(new HomeCard.Builder()
                .title(getResources().getString(R.string.more_apps))
                .description(getResources().getString(R.string.more_apps_long))
                .icon(playStoreDrawable)
                .onClickLink(getResources().getString(R.string.iconpack_author_playstore))
                .build());

//        if (!Utils.isAppInstalled(context, PITCHED_GLASS) {
            homeCards.add(new HomeCard.Builder()
                    .title(getResources().getString(R.string.home_pitched_glass))
                    .description(getResources().getString(R.string.home_pitched_glass_long))
                    .icon(iconsDrawable)
                    .onClickLink(MARKET_URL + PITCHED_GLASS)
                    .build());
//        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        HomeListAdapter mAdapter = new HomeListAdapter(homeCards, context);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        super.onViewCreated(view, savedInstanceState);
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

        iconsDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_android_alt)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable wallsDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_collection_image)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        Drawable widgetsDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_widgets)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        playStoreDrawable = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_case_play)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);
    }

    private void modifyFABIcon() {
        cm = Utils.isAppInstalled(context, "org.cyanogenmod.theme.chooser");
        cyngn = Utils.isAppInstalled(context, "com.cyngn.theme.chooser");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rro = Utils.isAppInstalled(context, "com.lovejoy777.rroandlayersmanager");
        } else {
            rro = false; //don't enable rro before lollipop, it didn't exist before that
        }

        if (cm || cyngn) {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_apply_cm));
        } else if (rro) {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_apply_layers));
        } else {
            ShowcaseActivity.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_question));
        }
    }


}