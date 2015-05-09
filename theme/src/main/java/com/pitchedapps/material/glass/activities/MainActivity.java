package com.pitchedapps.material.glass.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pitchedapps.material.glass.adapters.ChangelogAdapter;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.Util;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.pitchedapps.material.glass.R;


public class MainActivity extends AppCompatActivity {

    private static final boolean WITH_LICENSE_CHECKER = false;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    public Drawer.Result result = null;
    private String thaApp;
    private String thaPreviews;
    private String thaWalls;
    private String thaDonate;
    private String thaCredits;
    public String version;
    private int currentItem = -1;
    private boolean firstrun, enable_features;
    private Preferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = new Preferences(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thaApp = getResources().getString(R.string.app_name);
        String thaHome = getResources().getString(R.string.section_one);
        thaPreviews = getResources().getString(R.string.section_two);
        thaWalls = getResources().getString(R.string.section_four);
        thaDonate = getResources().getString(R.string.section_five);
        thaCredits = getResources().getString(R.string.section_six);

        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionFirstLine(getResources().getString(R.string.app_long_name))
                .withSelectionSecondLine("v" + Util.getAppVersion(this))
                .withSavedInstance(savedInstanceState)
                .build();

        enable_features = mPrefs.isFeaturesEnabled();
        firstrun = mPrefs.isFirstRun();

        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
//                .withStatusBarColor(R.color.primary)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(thaHome).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(thaPreviews).withIcon(GoogleMaterial.Icon.gmd_palette).withIdentifier(2),
//                        new PrimaryDrawerItem().withName(thaApply).withIcon(GoogleMaterial.Icon.gmd_open_in_browser).withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(thaDonate).withIdentifier(4),
                        new SecondaryDrawerItem().withName(thaCredits).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    switchFragment(1, thaApp, "Home");
                                    break;
                                case 2:
                                    switchFragment(2, thaPreviews, "Previews");
                                    break;
//                                case 3:
//                                    switchFragment(3, thaApply, "Apply");
//                                    break;
                                case 3:
                                    if (Util.hasNetwork(MainActivity.this)) {
                                        switchFragment(3, thaWalls, "Wallpapers");
                                    } else {
                                        showNotConnectedDialog();
                                    }
                                    break;
                                case 4:
//                                    switchFragment(4, thaDonate, "Donate");
                                    Intent intent = new Intent(MainActivity.this, DonationsActivity.class);
                                    startActivity(intent);
//                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    break;
                                case 5:
                                    switchFragment(5, thaCredits, "Credits");
                                    break;
                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result.getListView().setVerticalScrollBarEnabled(false);
        runLicenseChecker();

        if (savedInstanceState == null) {
            currentItem = -1;
            result.setSelectionByIdentifier(1);
        }
    }

    public void switchFragment(int itemId, String title, String fragment) {
        if (currentItem == itemId) {
            // Don't allow re-selection of the currently active item
            return;
        }
        currentItem = itemId;
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.main, Fragment.instantiate(MainActivity.this,
                        "com.pitchedapps.material.glass.fragments." + fragment + "Fragment"))
                .commit();
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
        } else if (result != null) {
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
                String shareBody =
                        getResources().getString(R.string.share_one) +
                                getResources().getString(R.string.iconpack_designer) +
                                getResources().getString(R.string.share_two) +
                                MARKET_URL + getPackageName();
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, (getResources().getString(R.string.share_title))));
                break;

            case R.id.sendemail:
                StringBuilder emailBuilder = new StringBuilder();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getResources().getString(R.string.email_id)));
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));

                emailBuilder.append("\n \n \nOS Version: ").append(System.getProperty("os.version")).append("(").append(Build.VERSION.INCREMENTAL).append(")");
                emailBuilder.append("\nOS API Level: ").append(Build.VERSION.SDK_INT);
                emailBuilder.append("\nDevice: ").append(Build.DEVICE);
                emailBuilder.append("\nManufacturer: ").append(Build.MANUFACTURER);
                emailBuilder.append("\nModel (and Product): ").append(Build.MODEL).append(" (").append(Build.PRODUCT).append(")");
                PackageInfo appInfo = null;
                try {
                    appInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                assert appInfo != null;
                emailBuilder.append("\nApp Version Name: ").append(appInfo.versionName);
                emailBuilder.append("\nApp Version Code: ").append(appInfo.versionCode);

                intent.putExtra(Intent.EXTRA_TEXT, emailBuilder.toString());
                startActivity(Intent.createChooser(intent, (getResources().getString(R.string.send_title))));
                break;

            case R.id.changelog:
                showChangelog();
                break;
        }
        return true;
    }

    private void addItemsToDrawer() {
        IDrawerItem walls = new PrimaryDrawerItem().withName(thaWalls).withIcon(GoogleMaterial.Icon.gmd_landscape).withIdentifier(3);
        if (enable_features) {
            result.addItem(walls, 2);
        }
    }

    private void runLicenseChecker() {
        if (firstrun) {
            if (WITH_LICENSE_CHECKER) {
                checkLicense();
            } else {
                mPrefs.setFeaturesEnabled(true);
                addItemsToDrawer();
                showChangelogDialog();
            }
        } else {
            if (WITH_LICENSE_CHECKER) {
                if (!enable_features) {
                    showNotLicensedDialog();
                } else {
                    addItemsToDrawer();
                    showChangelogDialog();
                }
            } else {
                addItemsToDrawer();
                showChangelogDialog();
            }
        }
    }

    private void showChangelog() {
        new MaterialDialog.Builder(this)
                .title(R.string.changelog_dialog_title)
                .adapter(new ChangelogAdapter(this, R.array.fullchangelog), null)
                .positiveText(R.string.nice)
                .negativeText(R.string.ratebtn)
                .neutralText(R.string.donate)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mPrefs.setNotFirstrun();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + getString(R.string.package_name)));
                        startActivity(browserIntent);
                    }

                    //TODO fix
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        Intent intent = new Intent(MainActivity.this, DonationsActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    private void showChangelogDialog() {
        String launchinfo = getSharedPreferences("PrefsFile", MODE_PRIVATE).getString("version", "0");
        if (launchinfo != null && !launchinfo.equals(Util.getAppVersion(this)))
            showChangelog();
        storeSharedPrefs();
    }

    @SuppressLint("CommitPrefEdits")
    private void storeSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        sharedPreferences.edit().putString("version", Util.getAppVersion(this)).commit();
    }

    private void showNotConnectedDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.no_conn_title)
                .content(R.string.no_conn_content)
                .positiveText(android.R.string.ok)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        int nSelection = currentItem - 1;
                        if (result != null)
                            result.setSelection(nSelection);
                    }
                }).show();
    }

    private void checkLicense() {
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
                        }).show();
            } else {
                showNotLicensedDialog();
            }
        } catch (Exception e) {
            showNotLicensedDialog();
        }
    }

    private void showNotLicensedDialog() {
        enable_features = false;
        mPrefs.setFeaturesEnabled(false);
        new MaterialDialog.Builder(this)
                .title(R.string.license_failed_title)
                .content(R.string.license_failed)
                .positiveText(R.string.download)
                .negativeText(R.string.exit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + getPackageName()));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        finish();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                }).show();
    }

}