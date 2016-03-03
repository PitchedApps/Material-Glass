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

package com.pitchedapps.material.glass.activities;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialize.util.UIUtils;

import org.sufficientlysecure.donations.DonationsFragment;
import org.sufficientlysecure.donations.google.util.IabHelper;
import org.sufficientlysecure.donations.google.util.IabResult;
import org.sufficientlysecure.donations.google.util.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.pitchedapps.material.glass.BuildConfig;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.adapters.RequestsAdapter;
import com.pitchedapps.material.glass.dialogs.FolderChooserDialog;
import com.pitchedapps.material.glass.dialogs.ISDialogs;
import com.pitchedapps.material.glass.fragments.RequestsFragment;
import com.pitchedapps.material.glass.fragments.SettingsFragment;
import com.pitchedapps.material.glass.fragments.WallpapersFragment;
import com.pitchedapps.material.glass.models.DrawerHeaderStyle;
import com.pitchedapps.material.glass.models.IconItem;
import com.pitchedapps.material.glass.models.WallpapersList;
import com.pitchedapps.material.glass.tasks.LoadIconsLists;
import com.pitchedapps.material.glass.utilities.PermissionUtils;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;
import com.pitchedapps.material.glass.utilities.ZooperIconFontsHelper;
import com.pitchedapps.material.glass.views.CustomCoordinatorLayout;

public class ShowcaseActivity extends AppCompatActivity implements FolderChooserDialog.FolderSelectionCallback, PermissionUtils.OnPermissionResultListener {

    public static boolean WITH_LICENSE_CHECKER = false,
            WITH_INSTALLED_FROM_AMAZON = false,
            WITH_ZOOPER_SECTION = false,
            WITH_DONATIONS_SECTION = false,
            WITH_ICONS_BASED_CHANGELOG = true,
            WITH_ALTERNATIVE_ABOUT_SECTION = true,
            WITH_SECONDARY_DRAWER_ITEMS_ICONS = false,

            //Donations stuff
            DONATIONS_GOOGLE = false,
            DONATIONS_PAYPAL = false,
            DONATIONS_FLATTR = false,
            DONATIONS_BITCOIN = false,
            SHOW_LOAD_ICONS_DIALOG = true;

    private static String[] mGoogleCatalog = new String[0],
            GOOGLE_CATALOG_VALUES = new String[0];

    ///test array values
    private static String[] primaryDrawerItems = new String[0], secondaryDrawerItems = new String[0];

    private static String GOOGLE_PUBKEY = "",
            PAYPAL_USER = "",
            PAYPAL_CURRENCY_CODE = "";

    public static DrawerHeaderStyle drawerHeaderStyle = DrawerHeaderStyle.NORMAL_HEADER;

    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    public boolean mIsPremium = false;

    private String action = "action";
    private static final String
            adw_action = "org.adw.launcher.icons.ACTION_PICK_ICON",
            turbo_action = "com.phonemetra.turbo.launcher.icons.ACTION_PICK_ICON",
            nova_action = "com.novalauncher.THEME";

    public static boolean iconsPicker, wallsPicker, iconsPickerEnabled = false, wallsEnabled = false,
            applyEnabled = false, SHUFFLE = true, shuffleIcons = true, selectAll = true;

    private static String thaAppName, thaHome, thaPreviews, thaApply, thaWalls, thaRequest, thaDonate, thaFAQs,
            thaZooper, thaCredits, thaSettings;

    private static AppCompatActivity context;

    public String version;

    public static long currentItem = -1, wallsIdentifier = 0,
            iconsPickerIdentifier = 0, applyIdentifier = 0, settingsIdentifier = 0, donationsIdentifier = 0,
            secondaryStart = 0;

    public static int numOfIcons = 4, wallpaper = -1;

    private boolean mLastTheme, mLastNavBar;
    private static Preferences mPrefs;

    public static MaterialDialog settingsDialog, loadIcons;
    public static Toolbar toolbar;
    public static AppBarLayout appbar;
    public static CollapsingToolbarLayout collapsingToolbarLayout;
    public static CustomCoordinatorLayout coordinatorLayout;
    public static FloatingActionButton fab;
    public static ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8;
    public static TextView titleView;
    public static ImageView toolbarHeader;
    public static Bitmap toolbarHeaderImage;

    public static Drawer drawer;
    public AccountHeader drawerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThemeUtils.onActivityCreateSetNavBar(this);
        }

        super.onCreate(savedInstanceState);

        context = this;
        mPrefs = new Preferences(ShowcaseActivity.this);

        String[] configurePrimaryDrawerItems = getResources().getStringArray(R.array.primary_drawer_items);
        primaryDrawerItems = new String[configurePrimaryDrawerItems.length + 1];
        primaryDrawerItems[0] = "Main";
        for (int i = 0; i < configurePrimaryDrawerItems.length; i++) {
            primaryDrawerItems[i + 1] = configurePrimaryDrawerItems[i];
        }

        getAction();

        WITH_ICONS_BASED_CHANGELOG = getResources().getBoolean(R.bool.icons_changelog);
        WITH_ALTERNATIVE_ABOUT_SECTION = getResources().getBoolean(R.bool.cards_credits);
        WITH_SECONDARY_DRAWER_ITEMS_ICONS = getResources().getBoolean(R.bool.secondary_drawer_items_icons);

        shuffleIcons = getResources().getBoolean(R.bool.shuffle_toolbar_icons);

        //donations stuff
        //google
        final String[] GOOGLE_CATALOG_FREE = getResources().getStringArray(R.array.nonconsumable_google_donation_items);
        final String[] GOOGLE_CATALOG_PRO = getResources().getStringArray(R.array.consumable_google_donation_items);
        mGoogleCatalog = GOOGLE_CATALOG_FREE;
        GOOGLE_CATALOG_VALUES = getResources().getStringArray(R.array.google_donations_catalog);
        //TODO check if 50 is a good reference value
        if (!(GOOGLE_PUBKEY.length() > 50) || !(GOOGLE_CATALOG_VALUES.length > 0) || !(GOOGLE_CATALOG_FREE.length == GOOGLE_CATALOG_PRO.length) || !(GOOGLE_CATALOG_FREE.length == GOOGLE_CATALOG_VALUES.length)) {
            DONATIONS_GOOGLE = false; //google donations setup is incorrect
        }

        //paypal
        PAYPAL_USER = getResources().getString(R.string.paypal_user);
        PAYPAL_CURRENCY_CODE = getResources().getString(R.string.paypal_currency_code);
        if (!(PAYPAL_USER.length() > 5) || !(PAYPAL_CURRENCY_CODE.length() > 1)) {
            DONATIONS_PAYPAL = false; //paypal content incorrect
        }

        WITH_DONATIONS_SECTION = DONATIONS_GOOGLE || DONATIONS_PAYPAL || DONATIONS_FLATTR || DONATIONS_BITCOIN; //if one of the donations are enabled, then the section is enabled

        //Initialize SecondaryDrawerItems
        if (WITH_DONATIONS_SECTION) {
            secondaryDrawerItems = new String[]{"Credits", "Settings", "Donations"};
        } else {
            secondaryDrawerItems = new String[]{"Credits", "Settings"};
        }

        if (WITH_ALTERNATIVE_ABOUT_SECTION) { //use alternative credits layout if selected
            secondaryDrawerItems[0] = "CreditsAlt";
        }

        switch (getResources().getInteger(R.integer.nav_drawer_header_style)) {
            case 1:
                drawerHeaderStyle = DrawerHeaderStyle.NORMAL_HEADER;
                break;
            case 2:
                drawerHeaderStyle = DrawerHeaderStyle.MINI_HEADER;
                break;
            case 3:
                drawerHeaderStyle = DrawerHeaderStyle.NO_HEADER;
                break;
        }


        setContentView(R.layout.showcase_activity);

        runLicenseChecker();

        coordinatorLayout = (CustomCoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.title);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        toolbarHeader = (ImageView) findViewById(R.id.toolbarHeader);

        setSupportActionBar(toolbar);

        thaAppName = getResources().getString(R.string.app_name);
        thaHome = getResources().getString(R.string.section_home);
        thaPreviews = getResources().getString(R.string.section_icons);
        thaApply = getResources().getString(R.string.section_apply);
        thaWalls = getResources().getString(R.string.section_wallpapers);
        thaRequest = getResources().getString(R.string.section_icon_request);
        thaDonate = getResources().getString(R.string.section_donate);
        thaCredits = getResources().getString(R.string.section_about);
        thaSettings = getResources().getString(R.string.title_settings);
        thaFAQs = getResources().getString(R.string.faqs_section);
        thaZooper = getResources().getString(R.string.zooper_section_title);

        collapsingToolbarLayout.setTitle(thaAppName);

        Utils.setupCollapsingToolbarTextColors(context, collapsingToolbarLayout);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        //Setup donations
        final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                //TODO test this
                if (inventory != null) {
                    Utils.showLog(context, "IAP inventory exists");
                    for (String aGOOGLE_CATALOG_FREE : GOOGLE_CATALOG_FREE) {
                        Utils.showLog(context, aGOOGLE_CATALOG_FREE + " is " + inventory.hasPurchase(aGOOGLE_CATALOG_FREE));
                        if (inventory.hasPurchase(aGOOGLE_CATALOG_FREE)) { //at least one donation value found, now premium
                            mIsPremium = true;
                        }
                    }
                }
                if (isPremium()) {
                    mGoogleCatalog = GOOGLE_CATALOG_PRO;
                }
            }
        };

        setupDrawer(toolbar, savedInstanceState);

        if (savedInstanceState == null) {
            if (iconsPicker && iconsPickerEnabled) {
                loadIcons = ISDialogs.showLoadingIconsDialog(context);
                loadIcons.show();
                loadIcons.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        drawerItemClick(iconsPickerIdentifier);
                        drawer.setSelection(iconsPickerIdentifier);
                    }
                });
                if (!SHOW_LOAD_ICONS_DIALOG) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadIcons.dismiss();
                        }
                    }, 500);
                }
            } else if (wallsPicker && mPrefs.areFeaturesEnabled() && wallsEnabled) {
                drawerItemClick(wallsIdentifier);
                drawer.setSelection(wallsIdentifier);
            } else {
                if (mPrefs.getSettingsModified()) {
                    drawerItemClick(settingsIdentifier);
                    drawer.setSelection(settingsIdentifier);
                } else {
                    currentItem = -1;
                    drawerItemClick(1);
                    drawer.setSelection(1);
                }
            }
        }
    }

    public static String fragment2title(String fragment) {
        switch (fragment) {
            case "Main":
                return thaAppName;
            case "Previews":
                return " ";
            case "Apply":
                return thaApply;
            case "Wallpapers":
                return thaWalls;
            case "Requests":
                return thaRequest;
            case "Zooper":
                return thaZooper;
            case "Donations":
                return thaDonate;
            case "FAQs":
                return thaFAQs;
            case "Credits":
                return thaCredits;
            case "CreditsAlt":
                return thaCredits;
            case "Settings":
                return thaSettings;
        }
        return ":(";
    }

    public static void switchFragment(long itemId, String fragment,
                                      AppCompatActivity context) {

        if (currentItem == itemId) {
            // Don't allow re-selection of the currently active item
            return;
        }
        currentItem = itemId;

        //Fragment Switcher
        if (mPrefs.getAnimationsEnabled()) {
            if (fragment.equals("Donations")) {
                DonationsFragment donationsFragment;
                donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG,
                        DONATIONS_GOOGLE, GOOGLE_PUBKEY, mGoogleCatalog, GOOGLE_CATALOG_VALUES,
                        DONATIONS_PAYPAL, PAYPAL_USER, PAYPAL_CURRENCY_CODE, context.getString(R.string.section_donate),
                        DONATIONS_FLATTR, null, null,
                        DONATIONS_BITCOIN, null);
                context.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.main, donationsFragment, "donationsFragment")
                        .commit();
//            } else if (fragment.equals("Main")) {
//
//                MainFragment mainFragment;
//                mainFragment = MainFragment.newInstance(sHomeCards);
//                context.getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                        .replace(R.id.main, mainFragment, "mainFragment")
//                        .commit();
            } else {
                context.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.main, Fragment.instantiate(context,
                                "com.pitchedapps.material.glass.fragments." + fragment + "Fragment"))
                        .commit();
            }
        } else {
            if (fragment.equals("Donations")) {
                DonationsFragment donationsFragment;
                donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG,
                        DONATIONS_GOOGLE, GOOGLE_PUBKEY, mGoogleCatalog, GOOGLE_CATALOG_VALUES,
                        DONATIONS_PAYPAL, PAYPAL_USER, PAYPAL_CURRENCY_CODE, context.getString(R.string.section_donate),
                        DONATIONS_FLATTR, null, null,
                        DONATIONS_BITCOIN, null);
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, donationsFragment, "donationsFragment")
                        .commit();
            } else {
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, Fragment.instantiate(context,
                                "com.pitchedapps.material.glass.fragments." + fragment + "Fragment"))
                        .commit();
            }
        }

        collapsingToolbarLayout.setTitle(fragment2title(fragment));

        if (drawer != null) {
            drawer.setSelection(itemId);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLastTheme = ThemeUtils.darkTheme;
        mLastNavBar = ThemeUtils.coloredNavBar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!iconsPicker && !wallsPicker) {
            setupToolbarHeader(this, toolbarHeader);
        }
        Utils.setupToolbarIconsAndTextsColors(context, appbar, toolbar, toolbarHeaderImage);
        if (mLastTheme != ThemeUtils.darkTheme
                || mLastNavBar != ThemeUtils.coloredNavBar) {
            ThemeUtils.restartActivity(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (drawer != null)
            outState = drawer.saveInstanceState(outState);
        if (collapsingToolbarLayout != null && collapsingToolbarLayout.getTitle() != null) {
            outState.putString("toolbarTitle", collapsingToolbarLayout.getTitle().toString());
        }
        outState.putInt("currentSection", (int) currentItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (collapsingToolbarLayout != null) {
            Utils.setupCollapsingToolbarTextColors(this, collapsingToolbarLayout);
            collapsingToolbarLayout.setTitle(savedInstanceState.getString("toolbarTitle",
                    thaAppName));
        }
        drawerItemClick(savedInstanceState.getInt("currentSection"));
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (drawer != null && currentItem != 1 && !iconsPicker) {
            drawer.setSelection(1);
        } else if (drawer != null) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingsDialog != null) {
            settingsDialog.dismiss();
            settingsDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == PermissionUtils.PERMISSION_REQUEST_CODE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtils.permissionReceived() != null)
                    PermissionUtils.permissionReceived().onStoragePermissionGranted();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int i = item.getItemId();
        if (i == R.id.changelog) {
            if (WITH_ICONS_BASED_CHANGELOG) {
                ISDialogs.showIconsChangelogDialog(this);
            } else {
                ISDialogs.showChangelogDialog(this);
            }
        } else if (i == R.id.refresh) {
            WallpapersFragment.refreshWalls(context);
            loadWallsList();
        } else if (i == R.id.columns) {
            ISDialogs.showColumnsSelectorDialog(context);
        } else if (i == R.id.select_all) {
            RequestsFragment.requestsAdapter.selectOrDeselectAll(selectAll);
            selectAll = !selectAll;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            RequestsAdapter adapter = ((RequestsAdapter) RequestsFragment.mRecyclerView.getAdapter());
            if (adapter != null) {
                adapter.selectOrDeselectAll(false);
            }
        }
    }

    private void runLicenseChecker() {
        mPrefs.setSettingsModified(false);
        mPrefs.setFirstRun(getSharedPreferences("PrefsFile", MODE_PRIVATE).getBoolean("first_run", true));
        if (mPrefs.isFirstRun()) {
            if (WITH_LICENSE_CHECKER) {
                checkLicense();
            } else {
                mPrefs.setFeaturesEnabled(true);
                showChangelogDialog();
            }
            mPrefs.setFirstRun(false);
            getSharedPreferences("PrefsFile", MODE_PRIVATE).edit()
                    .putBoolean("first_run", false).commit();
        } else {
            if (WITH_LICENSE_CHECKER) {
                if (!mPrefs.areFeaturesEnabled()) {
                    showNotLicensedDialog();
                } else {
                    showChangelogDialog();
                }
            } else {
                showChangelogDialog();
            }
        }
    }

    private void showChangelogDialog() {
        String launchinfo = getSharedPreferences("PrefsFile", MODE_PRIVATE).getString("version", "0");
        storeSharedPrefs();
        if (launchinfo != null && !launchinfo.equals(Utils.getAppVersion(this))) {
            if (WITH_ZOOPER_SECTION) {
                if (!PermissionUtils.canAccessStorage(this)) {
                    PermissionUtils.requestStoragePermission(this, this);
                } else {
                    new ZooperIconFontsHelper(context).check(true);
                }
            }
            if (WITH_ICONS_BASED_CHANGELOG) {
                ISDialogs.showIconsChangelogDialog(this);
            } else {
                ISDialogs.showChangelogDialog(this);
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void storeSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        sharedPreferences.edit().putString("version", Utils.getAppVersion(this)).commit();
    }

    private void checkLicense() {
        String installer = getPackageManager().getInstallerPackageName(getPackageName());
        try {
            if (installer.equals("com.google.android.feedback") ||
                    installer.equals("com.android.vending")) {
                ISDialogs.showLicenseSuccessDialog(this, new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        mPrefs.setFeaturesEnabled(true);
                        showChangelogDialog();
                    }
                });
            } else if (installer.equals("com.amazon.venezia") && WITH_INSTALLED_FROM_AMAZON) {
                ISDialogs.showLicenseSuccessDialog(this, new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        mPrefs.setFeaturesEnabled(true);
                        showChangelogDialog();
                    }
                });
            } else {
                showNotLicensedDialog();
            }
        } catch (Exception e) {
            showNotLicensedDialog();
        }
    }

    private void showNotLicensedDialog() {
        mPrefs.setFeaturesEnabled(false);
        ISDialogs.showLicenseFailDialog(this,
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + getPackageName()));
                        startActivity(browserIntent);
                    }
                }, new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        finish();
                    }
                }, new MaterialDialog.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }, new MaterialDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
    }

    public void loadWallsList() {
        if (mPrefs.getWallsListLoaded()) {
            WallpapersList.clearList();
            mPrefs.setWallsListLoaded(!mPrefs.getWallsListLoaded());
        }
        new WallpapersFragment.DownloadJSON(new WallsListInterface() {

            @Override
            public void checkWallsListCreation(boolean result) {
                mPrefs.setWallsListLoaded(result);
                if (WallpapersFragment.mSwipeRefreshLayout != null) {
                    WallpapersFragment.mSwipeRefreshLayout.setEnabled(false);
                    WallpapersFragment.mSwipeRefreshLayout.setRefreshing(false);
                }
                if (WallpapersFragment.mAdapter != null) {
                    WallpapersFragment.mAdapter.notifyDataSetChanged();
                }
            }
        }, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onStoragePermissionGranted() {
        new ZooperIconFontsHelper(context).check(true);
    }

    public interface WallsListInterface {

        void checkWallsListCreation(boolean result);
    }

    public void onFolderSelection(@NonNull File folder) {
        mPrefs.setDownloadsFolder(folder.getAbsolutePath());
        SettingsFragment.changeValues(getApplicationContext());
    }

    public void setupDrawer(final Toolbar toolbar, Bundle savedInstanceState) {

        //Initialize PrimaryDrawerItem
        PrimaryDrawerItem home, previews, walls, requests, apply, faqs, zooper;

        //initialize SecondaryDrawerItem
        SecondaryDrawerItem creditsItem, settingsItem, donationsItem;

        secondaryStart = primaryDrawerItems.length + 1; //marks the first identifier value that should be used

        DrawerBuilder drawerBuilder;

        drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withFireOnInitialOnClick(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            drawerItemClick(drawerItem.getIdentifier());
                        }
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == iconsPickerIdentifier && mIsPremium) {
                            switchFragment(iconsPickerIdentifier, "Requests", context);
                            drawer.closeDrawer();
                        }
                        return false;
                    }
                });

        for (int i = 0; i < primaryDrawerItems.length; i++) {
            switch (primaryDrawerItems[i]) {
                case "Main":
                    home = new PrimaryDrawerItem().withName(thaHome).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(i + 1);
                    drawerBuilder.addDrawerItems(home);
                    break;

                case "Previews":
                    iconsPickerEnabled = true;
                    iconsPickerIdentifier = i + 1;
                    previews = new PrimaryDrawerItem().withName(thaPreviews).withIcon(GoogleMaterial.Icon.gmd_palette).withIdentifier(iconsPickerIdentifier);
                    drawerBuilder.addDrawerItems(previews);
                    break;

                case "Wallpapers":
                    wallsEnabled = true;
                    wallsIdentifier = i + 1;
                    walls = new PrimaryDrawerItem().withName(thaWalls).withIcon(GoogleMaterial.Icon.gmd_landscape).withIdentifier(wallsIdentifier);
                    drawerBuilder.addDrawerItems(walls);
                    break;

                case "Requests":
                    requests = new PrimaryDrawerItem().withName(thaRequest).withIcon(GoogleMaterial.Icon.gmd_comment_list).withIdentifier(i + 1);
                    drawerBuilder.addDrawerItems(requests);
                    break;

                case "Apply":
                    applyEnabled = true;
                    applyIdentifier = i + 1;
                    apply = new PrimaryDrawerItem().withName(thaApply).withIcon(GoogleMaterial.Icon.gmd_open_in_browser).withIdentifier(applyIdentifier);
                    drawerBuilder.addDrawerItems(apply);
                    break;

                case "FAQs":
                    faqs = new PrimaryDrawerItem().withName(thaFAQs).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(i + 1);
                    drawerBuilder.addDrawerItems(faqs);
                    break;

                case "Zooper":
                    WITH_ZOOPER_SECTION = true;
                    zooper = new PrimaryDrawerItem().withName(thaZooper).withIcon(GoogleMaterial.Icon.gmd_widgets).withIdentifier(i + 1);
                    drawerBuilder.addDrawerItems(zooper);
                    break;
            }
        }

        drawerBuilder.addDrawerItems(new DividerDrawerItem()); //divider between primary and secondary

        if (WITH_SECONDARY_DRAWER_ITEMS_ICONS) {
            for (int i = 0; i < secondaryDrawerItems.length; i++) {
                switch (secondaryDrawerItems[i]) {
                    case "Credits":
                        creditsItem = new SecondaryDrawerItem().withName(thaCredits).withIcon(GoogleMaterial.Icon.gmd_info).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(creditsItem);
                        break;
                    case "CreditsAlt":
                        creditsItem = new SecondaryDrawerItem().withName(thaCredits).withIcon(GoogleMaterial.Icon.gmd_info).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(creditsItem);
                        break;
                    case "Settings":
                        settingsIdentifier = i + secondaryStart;
                        settingsItem = new SecondaryDrawerItem().withName(thaSettings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(settingsIdentifier);
                        drawerBuilder.addDrawerItems(settingsItem);
                        break;
                    case "Donations":
                        donationsIdentifier = i + secondaryStart;
                        donationsItem = new SecondaryDrawerItem().withName(thaDonate).withIcon(GoogleMaterial.Icon.gmd_money_box).withIdentifier(donationsIdentifier);
                        drawerBuilder.addDrawerItems(donationsItem);
                        break;
                }
            }
        } else {
            for (int i = 0; i < secondaryDrawerItems.length; i++) {
                switch (secondaryDrawerItems[i]) {
                    case "Credits":
                        creditsItem = new SecondaryDrawerItem().withName(thaCredits).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(creditsItem);
                        break;
                    case "CreditsAlt":
                        creditsItem = new SecondaryDrawerItem().withName(thaCredits).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(creditsItem);
                        break;
                    case "Settings":
                        settingsItem = new SecondaryDrawerItem().withName(thaSettings).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(settingsItem);
                        break;
                    case "Donations":
                        donationsItem = new SecondaryDrawerItem().withName(thaDonate).withIdentifier(i + secondaryStart);
                        drawerBuilder.addDrawerItems(donationsItem);
                        break;
                }
            }
        }

        drawerBuilder.withSavedInstance(savedInstanceState);

        String headerAppName = "", headerAppVersion = "";

        if (getResources().getBoolean(R.bool.with_drawer_texts)) {
            headerAppName = getResources().getString(R.string.app_long_name);
            headerAppVersion = "v " + Utils.getAppVersion(this);
        }

        switch (drawerHeaderStyle) {
            case NORMAL_HEADER:
                drawerHeader = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeaderBackground(R.drawable.drawer_header)
                        .withSelectionFirstLine(headerAppName)
                        .withSelectionSecondLine(headerAppVersion)
                        .withProfileImagesClickable(false)
                        .withResetDrawerOnProfileListClick(false)
                        .withSelectionListEnabled(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .withSavedInstance(savedInstanceState)
                        .build();

                drawerBuilder.withAccountHeader(drawerHeader);
                break;
            case MINI_HEADER:
                drawerBuilder.withHeader(R.layout.mini_drawer_header);
                break;
            case NO_HEADER:
                break;
        }

        drawer = drawerBuilder.build();

        if (drawerHeaderStyle.equals(DrawerHeaderStyle.MINI_HEADER)) {
            ImageView miniHeader = (ImageView) drawer.getHeader().findViewById(R.id.mini_drawer_header);
            miniHeader.getLayoutParams().height = UIUtils.getActionBarHeight(this) + UIUtils.getStatusBarHeight(this);
            TextView appVersion = (TextView) drawer.getHeader().findViewById(R.id.text_app_version);
            TextView appName = (TextView) drawer.getHeader().findViewById(R.id.text_app_name);
            if (context.getResources().getBoolean(R.bool.mini_header_solid_color)) {
                int backgroundColor = ThemeUtils.darkTheme ?
                        ContextCompat.getColor(context, R.color.dark_theme_primary) :
                        ContextCompat.getColor(context, R.color.light_theme_primary);
                miniHeader.setBackgroundColor(backgroundColor);
                int iconsColor = ThemeUtils.darkTheme ?
                        ContextCompat.getColor(this, R.color.toolbar_text_dark) :
                        ContextCompat.getColor(this, R.color.toolbar_text_light);
                appVersion.setTextColor(iconsColor);
                appName.setTextColor(iconsColor);
            } else {
                miniHeader.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.drawer_header));
                appVersion.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                appName.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            }
            appName.setText(headerAppName);
            appVersion.setText(headerAppVersion);
        }
    }

    public static void drawerItemClick(long id) {
        if (id <= primaryDrawerItems.length) {
            switchFragment(id, primaryDrawerItems[(int) id - 1], context);
        } else {
            switchFragment(id, secondaryDrawerItems[((int) (id - secondaryStart))], context);
        }
    }

    public void getAction() {
        try {
            action = getIntent().getAction();
        } catch (Exception e) {
            action = "action";
        }

        try {
            switch (action) {
                case adw_action:
                case turbo_action:
                case nova_action:
                case Intent.ACTION_PICK:
                case Intent.ACTION_GET_CONTENT:
                    iconsPicker = true;
                    wallsPicker = false;
                    break;
                case Intent.ACTION_SET_WALLPAPER:
                    iconsPicker = false;
                    wallsPicker = true;
                    break;
                default:
                    iconsPicker = false;
                    wallsPicker = false;
                    break;
            }
        } catch (ActivityNotFoundException | NullPointerException e) {
            iconsPicker = false;
            wallsPicker = false;
        }
    }

    public static void setupIcons(final ImageView icon1, final ImageView icon2,
                                  final ImageView icon3, final ImageView icon4,
                                  final ImageView icon5, final ImageView icon6,
                                  final ImageView icon7, final ImageView icon8,
                                  int numOfIcons) {

        ArrayList<IconItem> icons = null;

        if (LoadIconsLists.getIconsLists() != null) {
            icons = LoadIconsLists.getIconsLists().get(1).getIconsArray();
        }

        ArrayList<IconItem> finalIconsList = new ArrayList<>();

        if (icons != null && SHUFFLE && shuffleIcons) {
            Collections.shuffle(icons);
        }

        int i = 0;

        if (icons != null) {
            while (i < numOfIcons) {
                finalIconsList.add(icons.get(i));
                i++;
            }
            icon1.setImageResource(finalIconsList.get(0).getResId());
            icon2.setImageResource(finalIconsList.get(1).getResId());
            icon3.setImageResource(finalIconsList.get(2).getResId());
            icon4.setImageResource(finalIconsList.get(3).getResId());
            if (numOfIcons == 6) {
                icon5.setImageResource(finalIconsList.get(4).getResId());
                icon6.setImageResource(finalIconsList.get(5).getResId());
            } else if (numOfIcons == 8) {
                icon5.setImageResource(finalIconsList.get(4).getResId());
                icon6.setImageResource(finalIconsList.get(5).getResId());
                icon7.setImageResource(finalIconsList.get(6).getResId());
                icon8.setImageResource(finalIconsList.get(7).getResId());
            }
        }

        SHUFFLE = false;
    }

    public static void animateIcons(ImageView icon1, ImageView icon2,
                                    ImageView icon3, ImageView icon4,
                                    ImageView icon5, ImageView icon6,
                                    ImageView icon7, ImageView icon8,
                                    int numOfIcons) {

        icon1.setVisibility(View.GONE);
        icon2.setVisibility(View.GONE);
        icon3.setVisibility(View.GONE);
        icon4.setVisibility(View.GONE);
        icon5.setVisibility(View.GONE);
        icon6.setVisibility(View.GONE);
        icon7.setVisibility(View.GONE);
        icon8.setVisibility(View.GONE);

        if (!iconsPicker && !wallsPicker) {
            switch (numOfIcons) {
                case 4:
                    icon1.setVisibility(View.VISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon3.setVisibility(View.VISIBLE);
                    icon4.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    icon1.setVisibility(View.VISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon3.setVisibility(View.VISIBLE);
                    icon4.setVisibility(View.VISIBLE);
                    icon5.setVisibility(View.VISIBLE);
                    icon6.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    icon1.setVisibility(View.VISIBLE);
                    icon2.setVisibility(View.VISIBLE);
                    icon3.setVisibility(View.VISIBLE);
                    icon4.setVisibility(View.VISIBLE);
                    icon5.setVisibility(View.VISIBLE);
                    icon6.setVisibility(View.VISIBLE);
                    icon7.setVisibility(View.VISIBLE);
                    icon8.setVisibility(View.VISIBLE);
                    break;
            }
        }

        if (mPrefs.getAnimationsEnabled()) {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.bounce);
            playIconsAnimations(icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, anim, numOfIcons);
        }
    }

    private static void playIconsAnimations(ImageView icon1, ImageView icon2,
                                            ImageView icon3, ImageView icon4,
                                            ImageView icon5, ImageView icon6,
                                            ImageView icon7, ImageView icon8,
                                            Animation anim, int numOfIcons) {

        icon1.startAnimation(anim);
        icon2.startAnimation(anim);
        icon3.startAnimation(anim);
        icon4.startAnimation(anim);

        switch (numOfIcons) {
            case 6:
                icon5.startAnimation(anim);
                icon6.startAnimation(anim);
                break;
            case 8:
                icon5.startAnimation(anim);
                icon6.startAnimation(anim);
                icon7.startAnimation(anim);
                icon8.startAnimation(anim);
                break;
        }
    }

    public static void setupToolbarHeader(Context context, ImageView toolbarHeader) {

        toolbarHeader.setImageResource(R.drawable.heroimage);
        ShowcaseActivity.toolbarHeaderImage = Utils.drawableToBitmap(
                ContextCompat.getDrawable(context, R.drawable.heroimage));

        toolbarHeader.setVisibility(View.VISIBLE);
    }

    public boolean isPremium() {
        return mIsPremium;
    }

//    public void addHomeCard(HomeCard object) {
//        homeCards.add(object);
//    }

//    public void sendCards() {
//        mainIntent = new Intent(this, MainFragment.class);
//        Log.e("asdf home", homeCards.toString());
//        mainIntent.putParcelableArrayListExtra("homeCards", homeCards);
//        sHomeCards = homeCards;
//        Log.e("asdf", "cards sent");
//        Log.e("asdf", "" + sHomeCards.toString());
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("homeCards", (Parcelable) homeCards);
// set Fragmentclass Arguments
//        MainFragment mF = new MainFragment();
//        mF.setArguments(bundle);
//        MainFragment(mainIntent);
//    }

    public void enableDonations(boolean WITH_DONATIONS_SECTION) {
        this.WITH_DONATIONS_SECTION = WITH_DONATIONS_SECTION;
    }

    public void enableGoogleDonations(boolean DONATIONS_GOOGLE) {
        this.DONATIONS_GOOGLE = DONATIONS_GOOGLE;
    }

    public void enablePaypalDonations(boolean DONATIONS_PAYPAL) {
        this.DONATIONS_PAYPAL = DONATIONS_PAYPAL;
    }

    public void enableFlattrDonations(boolean DONATIONS_FLATTR) {
        this.DONATIONS_FLATTR = DONATIONS_FLATTR;
    }

    public void enableBitcoinDonations(boolean DONATIONS_BITCOIN) {
        this.DONATIONS_BITCOIN = DONATIONS_BITCOIN;
    }

    public void enableLicenseCheck(boolean WITH_LICENSE_CHECKER) {
        this.WITH_LICENSE_CHECKER = WITH_LICENSE_CHECKER;
    }

    public void enableAmazonInstalls(boolean WITH_INSTALLED_FROM_AMAZON) {
        this.WITH_INSTALLED_FROM_AMAZON = WITH_INSTALLED_FROM_AMAZON;
    }

    public void setGooglePubkey(String GOOGLE_PUBKEY) {
        this.GOOGLE_PUBKEY = GOOGLE_PUBKEY;
    }
}