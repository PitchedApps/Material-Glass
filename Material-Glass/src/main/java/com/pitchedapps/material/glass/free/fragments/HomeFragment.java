package com.pitchedapps.material.glass.free.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.pitchedapps.material.glass.free.R;
import com.pitchedapps.material.glass.free.activities.MainActivity;

import java.lang.reflect.Constructor;

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

        TextView apponebtn = (TextView) root.findViewById(R.id.appone_button);
        apponebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).result.setSelection(5);
                ((MainActivity) getActivity()).switchFragment(5, getResources().getString(R.string.donate), "Donate");
            }
        });

        TextView apptwobtn = (TextView) root.findViewById(R.id.appXda_button);
        apptwobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apptwo = new Intent(Intent.ACTION_VIEW, Uri.parse(AppXdaPackage));
                startActivity(apptwo);
            }
        });

        TextView appthreebtn = (TextView) root.findViewById(R.id.appPitched_glass_button);
        appthreebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appthree = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + AppPitchedGlassPackage));
                startActivity(appthree);
            }
        });

        TextView appfourbtn = (TextView) root.findViewById(R.id.appTbo_button);
        appfourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appfour = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_link)));
                startActivity(appfour);
            }
        });

        TextView appfourbtnxda = (TextView) root.findViewById(R.id.appTbo_button_xda);
        appfourbtnxda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appfour = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tbo_xda)));
                startActivity(appfour);
            }
        });
		
		TextView appplusbtn = (TextView) root.findViewById(R.id.appPlus_button);
        appplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appplus = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_plus_link)));
                startActivity(appplus);
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

        FloatingActionButton fabApply = (FloatingActionButton) root.findViewById(R.id.apply_home);
        Intent cm = getActivity().getPackageManager().getLaunchIntentForPackage("org.cyanogenmod.theme.chooser");
        Intent cyngn = getActivity().getPackageManager().getLaunchIntentForPackage("com.cyngn.theme.chooser");
        Intent rro = getActivity().getPackageManager().getLaunchIntentForPackage("com.lovejoy777.rroandlayersmanager");
        fabApply.setVisibility(View.VISIBLE);
        fabApply.setColorNormal(getResources().getColor(R.color.fab_unpressed));
        fabApply.setColorPressed(getResources().getColor(R.color.fab_pressed));
        fabApply.setColorRipple(getResources().getColor(R.color.semitransparent_white));
        fabApply.show(true);
        fabApply.attachToScrollView(content);
        if (cm != null) {
            fabApply.setImageResource(R.drawable.ic_cm);
            Log.d("MGlass", "org.cyanogenmod.theme.chooser is installed");
            fabApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentcm = getActivity().getPackageManager().getLaunchIntentForPackage("org.cyanogenmod.theme.chooser");
                    if (intentcm == null) {
                        Toast.makeText(getActivity(), getString(R.string.cm_not_installed), Toast.LENGTH_SHORT).show();
                    } else {
                        final String className = "com.pitchedapps.material.glass.free.utilities.CmThemeEngineLauncher";
                        Class<?> cl = null;
                        try {
                            cl = Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            Log.e("LAUNCHER CLASS MISSING", "Launcher class for: '" + className + "' missing!");
                        }
                        if (cl != null) {
                            Constructor<?> constructor = null;
                            try {
                                constructor = cl.getConstructor(Context.class);
                            } catch (NoSuchMethodException e) {
                                Log.e("LAUNCHER CLASS CONS",
                                        "Launcher class for: '" + className + "' is missing a constructor!");
                            }
                            try {
                                if (constructor != null)
                                    constructor.newInstance(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            });
        } else if (cyngn != null) {
            fabApply.setImageResource(R.drawable.ic_cyngn);
            Log.d("MGlass", "com.cyngn.theme.chooser is installed");
            fabApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentcyngn = getActivity().getPackageManager().getLaunchIntentForPackage("com.cyngn.theme.chooser");
                    if (intentcyngn == null) {
                        Toast.makeText(getActivity(), getString(R.string.cyngn_not_installed), Toast.LENGTH_SHORT).show();
                    } else {

                        getActivity().startActivity(intentcyngn);
                    }
                }


            });
        } else if (rro != null) {
            fabApply.setImageResource(R.drawable.ic_rro);
            Log.d("MGlass", "com.lovejoy777.rroandlayersmanager is installed");
            fabApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentrro = getActivity().getPackageManager().getLaunchIntentForPackage("com.lovejoy777.rroandlayersmanager");
                    if (intentrro == null) {
                        Toast.makeText(getActivity(), getString(R.string.rro_not_installed), Toast.LENGTH_SHORT).show();
                    } else {
                        getActivity().startActivity(intentrro);
                    }
                }
            });
        } else {
            fabApply.setImageResource(R.drawable.ic_rro);
            Log.d("MGlass", "No theme engine found");
//            fabApply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog noThemeEngine = new AlertDialog.Builder(getActivity())
//                            .create();
//                    noThemeEngine.setCancelable(false);
//                    noThemeEngine.setTitle(R.string.NTED_title);
//                    noThemeEngine.setMessage(getActivity().getString(R.string.NTED_message));
////                    noThemeEngine.setButton(getActivity().getString(R.string.close), new DialogInterface.OnClickListener() {
////
////                        public void onClick(DialogInterface dialog, int which) {
////                            dialog.dismiss();
////                        }
////                    });
//                    noThemeEngine.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//                    noThemeEngine.show();
//                }
//            });
        }
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
