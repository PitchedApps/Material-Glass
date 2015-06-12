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
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.pitchedapps.material.glass.BuildConfig;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.adapters.ChangelogAdapter;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.Util;
import com.pkmmte.requestmanager.PkRequestManager;
import com.pkmmte.requestmanager.RequestSettings;

//import org.sufficientlysecure.donations.DonationsFragment;
import com.pitchedapps.material.glass.fragments.DonationsFragment;
import org.sufficientlysecure.donations.google.util.IabHelper;
import org.sufficientlysecure.donations.google.util.IabResult;
import org.sufficientlysecure.donations.google.util.Inventory;


public class MainActivity extends AppCompatActivity {

    private static final boolean WITH_LICENSE_CHECKER = false;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    public boolean mIsPremium = false;
    private static final String TAG = "M_Glass: ";
    IabHelper mHelper;
    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzw5avemZxxO/DYaMJOayfZIrUKSyVMaBk+oVgwXeIpciMmfLFtn4r94hwh0XIxLYkYcpxGW/c1GwE2LJiWEasVL3QLtsS664ETqLE1zdBMpuuaUre85K4YQGSS5Q8p8HtrIas52mikTk38o7FvGGRtypDGjLHciWYFKgJXweoLXF2tWKDtus8K/UOW6zlXYyetXq8qcl5vhFq9kmpD+R4Dk7hwAoGDqafcpF52uFcNMUNDn6YVdV000fN0EHR9D+P1+Oro1UG6P77k1tk8E3AboLu1f7m6GFIJJlHO1BAQVxR55OelGgC/l355RWLGB1RKR/fbQblpCHd6NLOd5AswIDAQAB/j/iGZQlXU0qkAv2BA6epOX1ihbMz78iD4SmViJlECHN8bKMHxouRNd9pkmQKxwEBHg5/xDC/PHmSCXFx/gcY/xa4etA1CSfXjcsS9i94n+j0gGYUg69rNkp+p/09nO9sgfRTAQppTxtgKaXwpfKe1A8oqmDUfOnPzsEAG6ogQL6Svo6ynYLVKIvRPPhXkq+fp6sJ5YVT5Hr356yCXlM++G56Pk8Z+tPzNjjvGSSs/MsYtgFaqhPCsnKhb55xHkc8GJ9haq8k3PSqwMSeJHnGiDq5lzdmsjdmGkWdQq2jIhKlhMZMm5VQWn0T59+xjjIIwIDAQAB";
    private static final String[] GOOGLE_CATALOG_FREE = new String[]{"glass.donation.1",
            "glass.donation.2", "glass.donation.3", "glass.donation.5", "glass.donation.10",
            "glass.donation.20"};
    private static final String[] GOOGLE_CATALOG_PRO = new String[]{"glass.donation.consumable.1",
            "glass.donation.consumable.2", "glass.donation.consumable.3", "glass.donation.consumable.5", "glass.donation.consumable.10",
            "glass.donation.consumable.20"};

    /**
     * PayPal
     */
    private static final String PAYPAL_USER = "pitchedapps@gmail.com";
    private static final String PAYPAL_CURRENCY_CODE = "CAD";

    private AccountHeader headerResult = null;
    public Drawer result = null;
    public String version;
    private String[] mGoogleCatalog;
    private String thaApp;
    private String thaPreviews;
    private String thaWalls;
    private String thaRequest;
    private String thaDonate;
    private String thaCredits;
    private String thaInfo;
    private int currentItem = -1;
    private boolean firstrun, enable_features;
    private Preferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab a reference to the manager and store it in a variable. This helps make code shorter.
        PkRequestManager mRequestManager = PkRequestManager.getInstance(this);
        mRequestManager.setDebugging(false);
        // Set your custom settings. Email address is required! Everything else is set to default.
        mRequestManager.setSettings(new RequestSettings.Builder()
                .addEmailAddress(getResources().getString(R.string.email_id))
                .emailSubject(getResources().getString(R.string.email_request_subject))
                .emailPrecontent(getResources().getString(R.string.request_precontent))
                .saveLocation(Environment.getExternalStorageDirectory().getAbsolutePath() + getResources().getString(R.string.request_save_location))
//                .appfilterName(getResources().getString(R.string.request_appfilter))
//                .compressFormat(PkRequestManager.PNG)
//                .appendInformation(true)
//                .createAppfilter(true)
//                .createZip(true)
                //TODO add filter
//                .filterDefined(true)
//                .byteBuffer(2048)
//                .compressQuality(100)
                .build());

        // Load apps ahead of time
        mRequestManager.loadAppsIfEmptyAsync();

        mPrefs = new Preferences(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleCatalog = GOOGLE_CATALOG_FREE;
        thaApp = getResources().getString(R.string.app_name);
        String thaHome = getResources().getString(R.string.section_one);
        thaPreviews = getResources().getString(R.string.section_two);
        thaWalls = getResources().getString(R.string.section_four);
        thaRequest = getResources().getString(R.string.section_five);
        thaDonate = getResources().getString(R.string.donate);
        thaCredits = getResources().getString(R.string.section_six);
        thaInfo = getResources().getString(R.string.section_eight);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionFirstLine(getResources().getString(R.string.app_long_name))
                .withSelectionSecondLine("v" + Util.getAppVersion(this))
                .withSavedInstance(savedInstanceState)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        enable_features = mPrefs.isFeaturesEnabled();
        firstrun = mPrefs.isFirstRun();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(thaHome).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(thaPreviews).withIcon(GoogleMaterial.Icon.gmd_palette).withIdentifier(2),
                        new PrimaryDrawerItem().withName(thaInfo).withIcon(GoogleMaterial.Icon.gmd_info).withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(thaDonate).withIdentifier(5),
                        new SecondaryDrawerItem().withName(thaCredits).withIdentifier(6)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    switchFragment(1, thaApp, "Home");
                                    break;
                                case 2:
                                    switchFragment(2, thaPreviews, "Previews");
                                    break;
                                case 3:
                                    switchFragment(3, thaInfo, "Info");
                                    break;
                                case 4:
                                    if (Util.hasNetwork(MainActivity.this)) {
                                        switchFragment(4, thaWalls, "Wallpapers");
                                    } else {
                                        showNotConnectedDialog();
                                    }
                                    break;
                                case 5:
                                    switchFragment(5, thaDonate, "Donate");
                                    break;
                                case 6:
                                    switchFragment(6, thaCredits, "Credits");
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
//check this
                        if (iDrawerItem instanceof Nameable) {
                            if (((Nameable) iDrawerItem).getName().equals(thaPreviews)) {
                                if (mIsPremium || !BuildConfig.DONATIONS_GOOGLE) {
                                    switchFragment(-1, thaRequest, "Request");
                                    result.closeDrawer();
                                    return true;
                                }
                            }
                        }
                        return false;
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

        //Setup donations

        final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

                if (inventory != null) {
                    Log.d(TAG, "IAP inventory exists");

                    Log.d(TAG, "Donations 1 is " + inventory.hasPurchase("glass.donation.1"));
                    Log.d(TAG, "Donations 2 is " + inventory.hasPurchase("glass.donation.2"));
                    Log.d(TAG, "Donations 3 is " + inventory.hasPurchase("glass.donation.3"));
                    Log.d(TAG, "Donations 5 is " + inventory.hasPurchase("glass.donation.5"));
                    Log.d(TAG, "Donations 10 is " + inventory.hasPurchase("glass.donation.10"));
                    Log.d(TAG, "Donations 20 is " + inventory.hasPurchase("glass.donation.20"));

                    if (inventory.hasPurchase("glass.donation.1") ||
                            inventory.hasPurchase("glass.donation.2") ||
                            inventory.hasPurchase("glass.donation.3") ||
                            inventory.hasPurchase("glass.donation.5") ||
                            inventory.hasPurchase("glass.donation.10") ||
                            inventory.hasPurchase("glass.donation.20")) {
                        Log.d(TAG, "IAP inventory contains a donation");

                        mIsPremium = true;
                    }
                }
                if (isPremium()) {
                    mGoogleCatalog = GOOGLE_CATALOG_PRO;
                }
            }
        };

        mHelper = new IabHelper(MainActivity.this, GOOGLE_PUBKEY);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("Donations unavailable.")
                            .content("Your device doesn't support In App Billing.  This could be because you need to update your Google Play Store application, or because you live in a country where In App Billing is disabled.")
                            .positiveText(android.R.string.ok)
                            .show();

                } else {
                    mHelper.queryInventoryAsync(false, mGotInventoryListener);
                }

            }
        });
    }

    public void switchFragment(int itemId, String title, String fragment) {
        if (currentItem == itemId) {
            // Don't allow re-selection of the currently active item
            return;
        }
        currentItem = itemId;
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        if (title.equals(thaDonate)) {
            DonationsFragment donationsFragment;
            if (BuildConfig.DONATIONS_GOOGLE) {
                donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, true, GOOGLE_PUBKEY, mGoogleCatalog,
                        getResources().getStringArray(R.array.donation_google_catalog_values), true, PAYPAL_USER,
                        PAYPAL_CURRENCY_CODE, getString(R.string.donation_paypal_item), false, null, null, false, null);
            } else {
                donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, false, null, null, null, true, PAYPAL_USER,
                        PAYPAL_CURRENCY_CODE, getString(R.string.donation_paypal_item), false, null, null, false, null);
            }
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.main, donationsFragment, "donationsFragment")
                    .commit();
        } else if (title.equals(thaRequest)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    //TODO fix error
                    .replace(R.id.main, Fragment.instantiate(MainActivity.this, "com.pkmmte.requestmanager.RequestFragment"))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.main, Fragment.instantiate(MainActivity.this,
                            "com.pitchedapps.material.glass.fragments." + fragment + "Fragment"))
                    .commit();
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
                                MARKET_URL + getString(R.string.package_name);
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
        IDrawerItem walls = new PrimaryDrawerItem().withName(thaWalls).withIcon(GoogleMaterial.Icon.gmd_landscape).withIdentifier(4);
        if (enable_features) {
            result.addItem(walls, 3);
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

    public boolean isPremium() {
        return mIsPremium;
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

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        switchFragment(5, thaDonate, "Donate");
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

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}