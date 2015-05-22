package com.pitchedapps.material.glass.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.MainActivity;
import com.pitchedapps.material.glass.inappbilling.IabHelper;
import com.pitchedapps.material.glass.inappbilling.IabResult;
import com.pitchedapps.material.glass.inappbilling.Inventory;
import com.pitchedapps.material.glass.inappbilling.Purchase;
//import org.sufficientlysecure.donations.google.util.IabHelper;
//import org.sufficientlysecure.donations.google.util.IabResult;
//import org.sufficientlysecure.donations.google.util.Purchase;

public class DonationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_credits, container, false);

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.donate_home_title);

        setupKarma(View);

        updatePremiumUi();

    }
//TODO merge later
//    public static final String ARG_DEBUG = "debug";
//
//    public static final String ARG_GOOGLE_ENABLED = "googleEnabled";
//    public static final String ARG_GOOGLE_PUBKEY = "googlePubkey";
//    public static final String ARG_GOOGLE_CATALOG = "googleCatalog";
//    public static final String ARG_GOOGLE_CATALOG_VALUES = "googleCatalogValues";
//
//    public static final String ARG_PAYPAL_ENABLED = "paypalEnabled";
//    public static final String ARG_PAYPAL_USER = "paypalUser";
//    public static final String ARG_PAYPAL_CURRENCY_CODE = "paypalCurrencyCode";
//    public static final String ARG_PAYPAL_ITEM_NAME = "mPaypalItemName";
//
//    public static final String ARG_BITCOIN_ENABLED = "bitcoinEnabled";
//    public static final String ARG_BITCOIN_ADDRESS = "bitcoinAddress";
//
//    private static final String TAG = "Donations Library";
//
//    // http://developer.android.com/google/play/billing/billing_testing.html
//    private static final String[] CATALOG_DEBUG = new String[]{"android.test.purchased",
//            "android.test.canceled", "android.test.refunded", "android.test.item_unavailable"};
//
//    private Spinner mGoogleSpinner;
//
//    // Google Play helper object
//    private IabHelper mHelper;
//
//    protected boolean mDebug = false;
//
//    protected boolean mGoogleEnabled = false;
//    protected String mGooglePubkey = "";
//    protected String[] mGgoogleCatalog = new String[]{};
//    protected String[] mGoogleCatalogValues = new String[]{};
//
//    protected boolean mPaypalEnabled = false;
//    protected String mPaypalUser = "";
//    protected String mPaypalCurrencyCode = "";
//    protected String mPaypalItemName = "";
//
//
//    protected boolean mBitcoinEnabled = false;
//    protected String mBitcoinAddress = "";
//
//    public static DonationsFragment newInstance(boolean debug, boolean googleEnabled, String googlePubkey, String[] googleCatalog,
//                                                String[] googleCatalogValues, boolean paypalEnabled, String paypalUser,
//                                                String paypalCurrencyCode, String paypalItemName) {
//        DonationsFragment donationsFragment = new DonationsFragment();
//        Bundle args = new Bundle();
//
//        args.putBoolean(ARG_DEBUG, debug);
//
//        args.putBoolean(ARG_GOOGLE_ENABLED, googleEnabled);
//        args.putString(ARG_GOOGLE_PUBKEY, googlePubkey);
//        args.putStringArray(ARG_GOOGLE_CATALOG, googleCatalog);
//        args.putStringArray(ARG_GOOGLE_CATALOG_VALUES, googleCatalogValues);
//
//        args.putBoolean(ARG_PAYPAL_ENABLED, paypalEnabled);
//        args.putString(ARG_PAYPAL_USER, paypalUser);
//        args.putString(ARG_PAYPAL_CURRENCY_CODE, paypalCurrencyCode);
//        args.putString(ARG_PAYPAL_ITEM_NAME, paypalItemName);
//
//        donationsFragment.setArguments(args);
//        return donationsFragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mDebug = getArguments().getBoolean(ARG_DEBUG);
//
//        mGoogleEnabled = getArguments().getBoolean(ARG_GOOGLE_ENABLED);
//        mGooglePubkey = getArguments().getString(ARG_GOOGLE_PUBKEY);
//        mGgoogleCatalog = getArguments().getStringArray(ARG_GOOGLE_CATALOG);
//        mGoogleCatalogValues = getArguments().getStringArray(ARG_GOOGLE_CATALOG_VALUES);
//
//        mPaypalEnabled = getArguments().getBoolean(ARG_PAYPAL_ENABLED);
//        mPaypalUser = getArguments().getString(ARG_PAYPAL_USER);
//        mPaypalCurrencyCode = getArguments().getString(ARG_PAYPAL_CURRENCY_CODE);
//        mPaypalItemName = getArguments().getString(ARG_PAYPAL_ITEM_NAME);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.donations__fragment, container, false);
//    }
//
//    @TargetApi(11)
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        /* Google */
//        if (mGoogleEnabled) {
//            // inflate google view into stub
//            ViewStub googleViewStub = (ViewStub) getActivity().findViewById(
//                    R.id.donations__google_stub);
//            googleViewStub.inflate();
//
//            // choose donation amount
//            mGoogleSpinner = (Spinner) getActivity().findViewById(
//                    R.id.donations__google_android_market_spinner);
//            ArrayAdapter<CharSequence> adapter;
//            if (mDebug) {
//                adapter = new ArrayAdapter<CharSequence>(getActivity(),
//                        android.R.layout.simple_spinner_item, CATALOG_DEBUG);
//            } else {
//                adapter = new ArrayAdapter<CharSequence>(getActivity(),
//                        android.R.layout.simple_spinner_item, mGoogleCatalogValues);
//            }
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            mGoogleSpinner.setAdapter(adapter);
//
//            Button btGoogle = (Button) getActivity().findViewById(
//                    R.id.donations__google_android_market_donate_button);
//            btGoogle.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    donateGoogleOnClick(v);
//                }
//            });
//
//            // Create the helper, passing it our context and the public key to verify signatures with
//            if (mDebug)
//                Log.d(TAG, "Creating IAB helper.");
//            mHelper = new IabHelper(getActivity(), mGooglePubkey);
//
//            // enable debug logging (for a production application, you should set this to false).
//            mHelper.enableDebugLogging(mDebug);
//
//            // Start setup. This is asynchronous and the specified listener
//            // will be called once setup completes.
//            if (mDebug)
//                Log.d(TAG, "Starting setup.");
//            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//                public void onIabSetupFinished(IabResult result) {
//                    if (mDebug)
//                        Log.d(TAG, "Setup finished.");
//
//                    if (!result.isSuccess()) {
//                        // Oh noes, there was a problem.
//                        openDialog(android.R.drawable.ic_dialog_alert, R.string.donations__google_android_market_not_supported_title,
//                                getString(R.string.donations__google_android_market_not_supported));
//                        return;
//                    }
//
//                    // Have we been disposed of in the meantime? If so, quit.
//                    if (mHelper == null) return;
//                }
//            });
//        }
//
//        /* PayPal */
//        if (mPaypalEnabled) {
//            // inflate paypal view into stub
//            ViewStub paypalViewStub = (ViewStub) getActivity().findViewById(
//                    R.id.donations__paypal_stub);
//            paypalViewStub.inflate();
//
//            Button btPayPal = (Button) getActivity().findViewById(
//                    R.id.donations__paypal_donate_button);
//            btPayPal.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    donatePayPalOnClick(v);
//                }
//            });
//        }
//
//    }
//
//    /**
//     * Open dialog
//     */
//    void openDialog(int icon, int title, String message) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        dialog.setIcon(icon);
//        dialog.setTitle(title);
//        dialog.setMessage(message);
//        dialog.setCancelable(true);
//        dialog.setNeutralButton(R.string.donations__button_close,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }
//        );
//        dialog.show();
//    }
//
//    /**
//     * Donate button executes donations based on selection in spinner
//     */
//    public void donateGoogleOnClick(View view) {
//        final int index;
//        index = mGoogleSpinner.getSelectedItemPosition();
//        if (mDebug)
//            Log.d(TAG, "selected item in spinner: " + index);
//
//        if (mDebug) {
//            // when debugging, choose android.test.x item
//            mHelper.launchPurchaseFlow(getActivity(),
//                    CATALOG_DEBUG[index], IabHelper.ITEM_TYPE_INAPP,
//                    0, mPurchaseFinishedListener, null);
//        } else {
//            mHelper.launchPurchaseFlow(getActivity(),
//                    mGgoogleCatalog[index], IabHelper.ITEM_TYPE_INAPP,
//                    0, mPurchaseFinishedListener, null);
//        }
//    }
//
//    // Callback for when a purchase is finished
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            if (mDebug)
//                Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
//
//            // if we were disposed of in the meantime, quit.
//            if (mHelper == null) return;
//
//            if (result.isSuccess()) {
//                if (mDebug)
//                    Log.d(TAG, "Purchase successful.");
//
//                // directly consume in-app purchase, so that people can donate multiple times
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//
//                // show thanks openDialog
//                openDialog(android.R.drawable.ic_dialog_info, R.string.donations__thanks_dialog_title,
//                        getString(R.string.donations__thanks_dialog));
//            }
//        }
//    };
//
//    // Called when consumption is complete
//    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
//        public void onConsumeFinished(Purchase purchase, IabResult result) {
//            if (mDebug)
//                Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
//
//            // if we were disposed of in the meantime, quit.
//            if (mHelper == null) return;
//
//            if (result.isSuccess()) {
//                if (mDebug)
//                    Log.d(TAG, "Consumption successful. Provisioning.");
//            }
//            if (mDebug)
//                Log.d(TAG, "End consumption flow.");
//        }
//    };
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (mDebug)
//            Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
//        if (mHelper == null) return;
//
//        // Pass on the fragment result to the helper for handling
//        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
//            // not handled, so handle it ourselves (here's where you'd
//            // perform any handling of activity results not related to in-app
//            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
//        } else {
//            if (mDebug)
//                Log.d(TAG, "onActivityResult handled by IABUtil.");
//        }
//    }
//
//
//    /**
//     * Donate button with PayPal by opening browser with defined URL For possible parameters see:
//     * https://developer.paypal.com/webapps/developer/docs/classic/paypal-payments-standard/integration-guide/Appx_websitestandard_htmlvariables/
//     */
//    public void donatePayPalOnClick(View view) {
//        Uri.Builder uriBuilder = new Uri.Builder();
//        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
//        uriBuilder.appendQueryParameter("cmd", "_donations");
//
//        uriBuilder.appendQueryParameter("business", mPaypalUser);
//        uriBuilder.appendQueryParameter("lc", "US");
//        uriBuilder.appendQueryParameter("item_name", mPaypalItemName);
//        uriBuilder.appendQueryParameter("no_note", "1");
//        // uriBuilder.appendQueryParameter("no_note", "0");
//        // uriBuilder.appendQueryParameter("cn", "Note to the developer");
//        uriBuilder.appendQueryParameter("no_shipping", "1");
//        uriBuilder.appendQueryParameter("currency_code", mPaypalCurrencyCode);
//        Uri payPalUri = uriBuilder.build();
//
//        if (mDebug)
//            Log.d(TAG, "Opening the browser with the url: " + payPalUri.toString());
//
//        // Start your favorite browser
//        try {
//            Intent viewIntent = new Intent(Intent.ACTION_VIEW, payPalUri);
//            startActivity(viewIntent);
//        } catch (ActivityNotFoundException e) {
//            openDialog(android.R.drawable.ic_dialog_alert, R.string.donations__alert_dialog_title,
//                    getString(R.string.donations__alert_dialog_no_browser));
//        }
//    }


    private void setupKarma(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.buttonKarma1);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.1", 1, ((MainActivity) getActivity()).mPurchaseFinishedListener, "1");
            }
        });

        layout = (LinearLayout) view.findViewById(R.id.buttonKarma2);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.2", 2, ((MainActivity) getActivity()).mPurchaseFinishedListener, "2");
            }
        });

        layout = (LinearLayout) view.findViewById(R.id.buttonKarma3);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.3", 3, ((MainActivity) getActivity()).mPurchaseFinishedListener, "3");
            }
        });

        layout = (LinearLayout) view.findViewById(R.id.buttonKarma5);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.5", 5, ((MainActivity) getActivity()).mPurchaseFinishedListener, "5");
            }
        });

        layout = (LinearLayout) view.findViewById(R.id.buttonKarma10);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.10", 10, ((MainActivity) getActivity()).mPurchaseFinishedListener, "10");
            }
        });

        layout = (LinearLayout) view.findViewById(R.id.buttonKarma20);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).mHelper.launchPurchaseFlow(getActivity(), "glass.donation.20", 20, ((MainActivity) getActivity()).mPurchaseFinishedListener, "20");
            }
        });

//        TextView helpFurtherButton = (TextView) view.findViewById(R.id.buttonHelpFurther);
//        final LinearLayout expanded = (LinearLayout) view.findViewById(R.id.layoutExpandedDonateAgain);
//        final ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollView);
//        helpFurtherButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                expanded.setVisibility(View.VISIBLE);
//                scroll.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scroll.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
//            }
//        });
    }

    private void updatePremiumUi() {
        if (((MainActivity)getActivity()).isPremium()) {
            View againView = (View) getActivity().findViewById(R.id.layoutDonateAgain);
            againView.setVisibility(View.VISIBLE);
            View donateView = (View) getActivity().findViewById(R.id.layoutDonate);
            donateView.setVisibility(View.GONE);
        }
    }
}
