package com.pitchedapps.material.glass.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.utils.Preferences;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class Main extends ActionBarActivity {

    private static final int PROFILE_SETTING = 1;
    public Drawer.Result result = null;
    public AccountHeader.Result headerResult = null;
    public String thaApp, thaHome, thaPreviews, thaWalls, thaCredits;
//    public String thaApp, thaHome, thaPreviews, thaApply, thaWalls, thaRequest, thaCredits;
    public String version, drawerVersion;
    public int currentItem;
    SharedPreferences sharedPreferences;
    private boolean firstrun, enable_features;
    private Preferences mPrefs;
    private boolean withLicenseChecker = false;
    private Context context;

//TODO theme card in main view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mPrefs = new Preferences(Main.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thaApp = getResources().getString(R.string.app_name);
        thaHome = getResources().getString(R.string.section_one);
        thaPreviews = getResources().getString(R.string.section_two);
//        thaApply = getResources().getString(R.string.section_three);
        thaWalls = getResources().getString(R.string.section_four);
//        thaRequest = getResources().getString(R.string.section_five);
        thaCredits = getResources().getString(R.string.section_seven);

        drawerVersion = "v " + getResources().getString(R.string.current_version);

        currentItem = 1;

        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionFirstLine(getResources().getString(R.string.app_long_name))
                .withSelectionSecondLine(drawerVersion)
                .withSavedInstance(savedInstanceState)
                .build();

        enable_features = mPrefs.isFeaturesEnabled();
        firstrun = mPrefs.isFirstRun();

        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(thaHome).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(thaPreviews).withIcon(GoogleMaterial.Icon.gmd_palette).withIdentifier(2),
//                        new PrimaryDrawerItem().withName(thaApply).withIcon(GoogleMaterial.Icon.gmd_loyalty).withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(thaCredits).withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem != null) {

                            if (drawerItem.getIdentifier() == 1) {
                                currentItem = 1;
                                getSupportActionBar().setTitle(thaApp);
                                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Home"));
                                tx.commit();
                            } else if (drawerItem.getIdentifier() == 2) {
                                currentItem = 2;
                                getSupportActionBar().setTitle(thaPreviews);
                                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                                tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Previews"));
                                tx.commit();
//                            } else if (drawerItem.getIdentifier() == 3) {
//                                currentItem = 3;
//                                getSupportActionBar().setTitle(thaApply);
//                                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//                                tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//                                tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Apply"));
//                                tx.commit();
                            } else if (drawerItem.getIdentifier() == 3) {
                                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                                if (isConnected == true) {
                                    currentItem = 3;
                                    getSupportActionBar().setTitle(thaWalls);
                                    FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                                    tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                    tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Wallpapers"));
                                    tx.commit();
                                } else {
                                    showNotConnectedDialog();
                                }

//                            } else if (drawerItem.getIdentifier() == 5) {
//                                currentItem = 5;
//                                getSupportActionBar().setTitle(thaRequest);
//                                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//                                tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//                                tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Request"));
//                                tx.commit();
                            } else if (drawerItem.getIdentifier() == 4) {
                                currentItem = 4;
                                getSupportActionBar().setTitle(thaCredits);
                                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                tx.replace(R.id.main, Fragment.instantiate(Main.this, "com.pitchedapps.material.glass.fragments.Credits"));
                                tx.commit();
                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result.getListView().setVerticalScrollBarEnabled(false);

        runLicenseChecker();

        if (savedInstanceState == null) {
            result.setSelectionByIdentifier(1);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else if (result != null && currentItem != 1) {
            result.setSelection(0);
        } else if (result != null && currentItem == 1) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check out this awesome icon pack by " + getResources().getString(R.string.iconpack_designer) + ".    Download Here: " + getResources().getString(R.string.play_store_link);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, (getResources().getString(R.string.share_title))));
                break;

            case R.id.rate:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_link)));
                startActivity(browserIntent);
                break;

            case R.id.sendemail:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getResources().getString(R.string.email_id)));
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
                startActivity(Intent.createChooser(intent, (getResources().getString(R.string.send_title))));
                break;

            case R.id.changelog:
                changelog();
                break;
            //TODO theme changelog

        }
        return true;
    }

    public void addItemsToDrawer() {
        IDrawerItem walls = new PrimaryDrawerItem().withName(thaWalls).withIcon(GoogleMaterial.Icon.gmd_landscape).withIdentifier(3);
//        IDrawerItem request = new PrimaryDrawerItem().withName(thaRequest).withIcon(GoogleMaterial.Icon.gmd_forum).withIdentifier(5);
        if (enable_features) {
            result.addItem(walls, 3);
//            result.addItem(request, 4);
        } else {
        }
    }

    private void runLicenseChecker() {
        if (firstrun) {
            if (withLicenseChecker) {
                checkLicense();
            } else {
                mPrefs.setFeaturesEnabled(true);
                addItemsToDrawer();
                showChangelogDialog();
            }
        } else {
            if (withLicenseChecker) {
                if (!enable_features) {
                    showNotLicensedDialog();
                }
            } else {
                addItemsToDrawer();
                showChangelogDialog();
            }
        }
    }

    private void changelog() {
        new MaterialDialog.Builder(context)
                .title(R.string.changelog_dialog_title)
                .content(R.string.changelog_content)
                .positiveText(R.string.nice)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mPrefs.setNotFirstrun();
                    }
                })
                .show();
    }

    private void showChangelogDialog() {

        String launchinfo = getSharedPreferences("PrefsFile", MODE_PRIVATE).getString("version", "0");
        if (launchinfo.equals(getResources().getString(R.string.current_version))) {
        } else {
            changelog();
        }

        storeSharedPrefs();


    }

    protected void storeSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("version", getResources().getString(R.string.current_version));
        editor.commit();
    }

    private void showNotConnectedDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.no_conn_title)
                .content(R.string.no_conn_content)
                .positiveText(R.string.ok)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        int nSelection = currentItem - 1;
                        if (result != null) {
                            result.setSelection(nSelection);
                        }

                    }
                })
                .show();
    }

    public void checkLicense() {
        String installer = getPackageManager().getInstallerPackageName(getPackageName());
        try {
            if (installer.equals("com.google.android.feedback") ||
                    installer.equals("com.android.vending")) {
                new MaterialDialog.Builder(this)
                        .title(R.string.license_success_title)
                        .content(R.string.license_success)
                        .positiveText(R.string.close)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {

                                enable_features = true;
                                mPrefs.setFeaturesEnabled(true);
                                addItemsToDrawer();
                                showChangelogDialog();

                            }
                        })
                        .show();


            } else {
                enable_features = false;
                mPrefs.setFeaturesEnabled(false);
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.license_failed_title)
                        .content(R.string.license_failed)
                        .positiveText(R.string.download)
                        .negativeText(R.string.exit)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_link)));
                                startActivity(browserIntent);

                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {

                                finish();

                            }
                        })
                        .show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });

            }
        } catch (Exception e) {
            enable_features = false;
            mPrefs.setFeaturesEnabled(false);
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.license_failed_title)
                    .content(R.string.license_failed)
                    .positiveText(R.string.download)
                    .negativeText(R.string.exit)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_link)));
                            startActivity(browserIntent);

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            finish();

                        }
                    })
                    .show();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
    }

    private void showNotLicensedDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.license_failed_title)
                .content(R.string.license_failed)
                .positiveText(R.string.download)
                .negativeText(R.string.exit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_link)));
                        startActivity(browserIntent);

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                        finish();

                    }
                })
                .show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }


}