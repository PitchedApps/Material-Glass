package com.pitchedapps.material.glass.fragments;

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

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.DonationsActivity;

public class HomeFragment extends Fragment {

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    //    private String PlayStoreDevAccount, PlayStoreListing, AppOnePackage, AppTwoPackage, AppThreePackage;
    private String PGL;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_home, container, false);

//        PlayStoreDevAccount = getResources().getString(R.string.play_store_dev_link);
//        PlayStoreListing = getActivity().getPackageName();
        PGL = getResources().getString(R.string.pitched_glass_link);
//        AppTwoPackage = getResources().getString(R.string.app_two_package);
//        AppThreePackage = getResources().getString(R.string.app_three_package);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.app_name);

        ObservableScrollView content = (ObservableScrollView) root.findViewById(R.id.HomeContent);

        //Cards
        CardView cardone = (CardView) root.findViewById(R.id.pitched_glass_card);
        if (AppIsInstalled(PGL)) {
            cardone.setVisibility((cardone.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        }


        TextView donatebtn = (TextView) root.findViewById(R.id.donate_button);
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donate = new Intent(getActivity(), DonationsActivity.class);
                startActivity(donate);
                //TODO add animations
            }
        });

        TextView playbtn = (TextView) root.findViewById(R.id.play_button);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devPlay = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_dev_link)));
                startActivity(devPlay);
            }
        });

        TextView xdabtn = (TextView) root.findViewById(R.id.xda_button);
        xdabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent xda = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.xda_link)));
                startActivity(xda);
            }
        });

        TextView pitchedglassbtn = (TextView) root.findViewById(R.id.pitchedglass_button);
        pitchedglassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pitchedglass = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.pitched_glass_link)));
                startActivity(pitchedglass);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.cm_apply);
        fab.setColorNormal(getResources().getColor(R.color.fab_unpressed));
        fab.setColorPressed(getResources().getColor(R.color.fab_pressed));
        fab.setColorRipple(getResources().getColor(R.color.ripple));
        fab.show(true);
        fab.attachToScrollView(content);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("org.cyanogenmod.theme.chooser");
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
