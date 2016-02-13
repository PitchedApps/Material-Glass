package com.pitchedapps.material.glass.free.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.pitchedapps.material.glass.free.R;
import com.pitchedapps.material.glass.free.activities.MainActivity;
import com.pitchedapps.material.glass.free.utilities.ThemeEngineLauncher;

public class HomeFragment extends Fragment {

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    private String PlayStoreDevAccount, PlayStoreListing, AppTboPackage, AppPitchedGlassPackage, AppXdaPackage;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_home, container, false);

        PlayStoreDevAccount = getResources().getString(R.string.play_store_dev_link);
        PlayStoreListing = getString(R.string.package_name);
        AppXdaPackage = getResources().getString(R.string.app_xda_package);
        AppPitchedGlassPackage = getResources().getString(R.string.app_pitched_glass_package);
        AppTboPackage = getResources().getString(R.string.app_tbo_package);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.app_name);

        ObservableScrollView content = (ObservableScrollView) root.findViewById(R.id.HomeContent);

        //Cards
        CardView cardDonate = (CardView) root.findViewById(R.id.cardDonate);
        CardView cardPitchedGlass = (CardView) root.findViewById(R.id.cardPitchedGlass);
        CardView cardTbo = (CardView) root.findViewById(R.id.cardTbo);

        if (((MainActivity)getActivity()).mIsPremium) {
            cardDonate.setVisibility((cardDonate.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }
        if (AppIsInstalled(AppPitchedGlassPackage)) {
            cardPitchedGlass.setVisibility((cardPitchedGlass.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }

        if (AppIsInstalled(AppTboPackage)) {
            cardTbo.setVisibility((cardTbo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }


        TextView playbtn = (TextView) root.findViewById(R.id.play_button);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devPlay = new Intent(Intent.ACTION_VIEW, Uri.parse(PlayStoreDevAccount));
                startActivity(devPlay);
            }
        });

        TextView appDonate_button = (TextView) root.findViewById(R.id.appDonate_button);
        appDonate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).result.setSelection(5);
                ((MainActivity) getActivity()).switchFragment(5, getResources().getString(R.string.donate), "Donate");
            }
        });

        TextView appXda_button = (TextView) root.findViewById(R.id.appXda_button);
        appXda_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appXda = new Intent(Intent.ACTION_VIEW, Uri.parse(AppXdaPackage));
                startActivity(appXda);
            }
        });

        TextView appPitched_glass_button = (TextView) root.findViewById(R.id.appPitched_glass_button);
        appPitched_glass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appPitched_glass = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + AppPitchedGlassPackage));
                startActivity(appPitched_glass);
            }
        });

        TextView appTbobtn = (TextView) root.findViewById(R.id.appTbo_button);
        appTbobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appTbo = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_link)));
                startActivity(appTbo);
            }
        });

        TextView appTbobtnxda = (TextView) root.findViewById(R.id.appTbo_button_xda);
        appTbobtnxda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appTboXda = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_xda)));
                startActivity(appTboXda);
            }
        });
		
		TextView appPlusbtn = (TextView) root.findViewById(R.id.appPlus_button);
        appPlusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appPlus = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_plus_link)));
                startActivity(appPlus);
            }
        });

        TextView rateBtn = (TextView) root.findViewById(R.id.rate_button);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rate = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + PlayStoreListing));
                startActivity(rate);
            }
        });

        boolean cm = AppIsInstalled("org.cyanogenmod.theme.chooser");
        boolean cyngn = AppIsInstalled("com.cyngn.theme.chooser");
        boolean rro = AppIsInstalled("com.lovejoy777.rroandlayersmanager");

        FloatingActionButton fabApply = (FloatingActionButton) root.findViewById(R.id.apply_home);
        fabApply.setVisibility(View.VISIBLE);
        fabApply.setColorNormal(getResources().getColor(R.color.fab_unpressed));
        fabApply.setColorPressed(getResources().getColor(R.color.fab_pressed));
        fabApply.setColorRipple(getResources().getColor(R.color.semitransparent_white));
        fabApply.show(true);
        fabApply.attachToScrollView(content);
        if (cm || cyngn || rro) {
            if (cm) {
                fabApply.setImageResource(R.drawable.ic_cm);
                Log.d("MGlass", "org.cyanogenmod.theme.chooser is installed");
            } else if (rro) {
                fabApply.setImageResource(R.drawable.ic_rro);
                Log.d("MGlass", "com.lovejoy777.rroandlayersmanager is installed");
            } else {
                fabApply.setImageResource(R.drawable.ic_cyngn);
                Log.d("MGlass", "com.cyngn.theme.chooser is installed");
            }
            fabApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ThemeEngineLauncher(getActivity().getApplicationContext());
                    writing an error here so you don't compile this
                }

            });
        } else {
            fabApply.setImageResource(R.drawable.ic_question);
            Log.d("MGlass", "No theme engine found");
            fabApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noThemeEngine();
                }
            });
        }
        return root;
    }

    private void noThemeEngine() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.NTED_title)
                .content(R.string.NTED_message)
                .positiveText(R.string.understood)
                .show();
    }

    private boolean AppIsInstalled(String packageName) {
        final PackageManager pm = getActivity().getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }



}
