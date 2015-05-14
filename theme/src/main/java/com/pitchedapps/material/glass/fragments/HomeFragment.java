package com.pitchedapps.material.glass.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pitchedapps.material.glass.activities.MainActivity;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import com.pitchedapps.material.glass.R;

public class HomeFragment extends Fragment {

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    private String PlayStoreDevAccount, PlayStoreListing, AppFourPackage, AppTwoPackage, AppThreePackage;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_home, container, false);

        PlayStoreDevAccount = getResources().getString(R.string.play_store_dev_link);
        PlayStoreListing = getString(R.string.package_name);
        AppTwoPackage = getResources().getString(R.string.app_two_package);
        AppThreePackage = getResources().getString(R.string.app_three_package);
        AppFourPackage = getResources().getString(R.string.app_four_package);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.app_name);

        ObservableScrollView content = (ObservableScrollView) root.findViewById(R.id.HomeContent);

        //Cards
        CardView cardthree = (CardView) root.findViewById(R.id.cardThree);
        CardView cardfour = (CardView) root.findViewById(R.id.cardFour);

        if (AppIsInstalled(AppThreePackage)) {
            cardthree.setVisibility((cardthree.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }

        if (AppIsInstalled(AppFourPackage)) {
            cardfour.setVisibility((cardfour.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }


        TextView playbtn = (TextView) root.findViewById(R.id.play_button);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devPlay = new Intent(Intent.ACTION_VIEW, Uri.parse(PlayStoreDevAccount));
                startActivity(devPlay);
            }
        });

        TextView apponebtn = (TextView) root.findViewById(R.id.appone_button);
        apponebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).result.setSelectionByIdentifier(5);
                ((MainActivity) getActivity()).switchFragment(5, getResources().getString(R.string.section_five), "Donate");
            }
        });

        TextView apptwobtn = (TextView) root.findViewById(R.id.apptwo_button);
        apptwobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apptwo = new Intent(Intent.ACTION_VIEW, Uri.parse(AppTwoPackage));
                startActivity(apptwo);
            }
        });

        TextView appthreebtn = (TextView) root.findViewById(R.id.appthree_button);
        appthreebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appthree = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + AppThreePackage));
                startActivity(appthree);
            }
        });

        TextView appfourbtn = (TextView) root.findViewById(R.id.appfour_button);
        appfourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appfour = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_link)));
                startActivity(appfour);
            }
        });

        TextView appfourbtnxda = (TextView) root.findViewById(R.id.appfour_button_xda);
        appfourbtnxda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appfour = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_xda)));
                startActivity(appfour);
            }
        });

        TextView ratebtn = (TextView) root.findViewById(R.id.rate_button);
        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rate = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + PlayStoreListing));
                startActivity(rate);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.apply_btn);
        fab.setColorNormal(getResources().getColor(R.color.primary));
        fab.setColorPressed(getResources().getColor(R.color.primary_dark));
        fab.setColorRipple(getResources().getColor(R.color.semitransparent_white));
        fab.show(true);
        fab.attachToScrollView(content);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("org.cyanogenmod.theme.chooser" + "");
//                Intent intent = new Intent();

//                Context context = getContext(); // this.getContext(); getApplicationContext(); etc.
//                String sPackName = context.getPackageName();

//                intent.setComponent(new ComponentName("org.cyanogenmod.theme.chooser", "org.cyanogenmod.theme.chooser.ChooserActivity"));
//                intent.putExtra("pkgName", "com.pitchedapps.material.glass.xda");
//                startActivity(intent);
                //TODO make this work
                if (intent == null) {
                    Toast.makeText(getActivity(), getString(R.string.cm_not_installed), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }


        });

        return root;
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
