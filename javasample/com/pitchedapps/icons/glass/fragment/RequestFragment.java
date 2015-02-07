package com.pitchedapps.icons.glass.fragment;

import java.util.List;

import view.QuickScroll;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.RequestAdapter;
import com.pitchedapps.icons.glass.util.Utils;
import com.pkmmte.requestmanager.AppInfo;
import com.pkmmte.requestmanager.AppLoadListener;
import com.pkmmte.requestmanager.PkRequestManager;
import com.pkmmte.requestmanager.SendRequestListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RequestFragment extends Fragment implements AppLoadListener, SendRequestListener, OnItemClickListener
{
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "RequestFragment";
	private final String HIDDEN_AUTO = "Hidden Auto Card";
	
	// Preferences
	private SharedPreferences mPrefs;
	
	// Request Manager
	private PkRequestManager mRequestManager;
	
	// AppInfo List & Adapter
	private List<AppInfo> mApps;
	private RequestAdapter mAdapter;
	
	// Progress Dialog
	private ProgressDialog progressDialog;
	
	// UI Handler
	private Handler mHandler;
	
	// Views
	private GridView mGrid;
	private QuickScroll mScroll;
	private LinearLayout mLoading;
	private TextView txtProgress;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_request, container, false);
		
		mPrefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, 0);
		setHasOptionsMenu(true);
		initViews(view);
		toggleAutoCard(view);
		adjustPadding();
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		// Initialize the UI handler for modifying UI through background threads (listeners)
		mHandler = new Handler();
		
		// Grab a reference to the manager and store it in a variable. This helps make code shorter.
		mRequestManager = PkRequestManager.getInstance(getActivity());
		
		// Initialize your progress dialog
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		// Android has a bug where a horizontal progress dialog won't show text if you change if after showing it. This is a workaround.
		progressDialog.setMessage("");
		
		// Set even listeners
		setListeners();
		mRequestManager.loadAppsIfEmptyAsync();
		
		// Set layout visibility based on loaded state
		toggleLayoutVisibility(mRequestManager.appsLoaded());
		
		// Get apps and populate if necessary
		mApps = mRequestManager.getApps();
		populateGrid();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		// Remove all event listeners
		removeListeners();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.request, menu);
		try{
			menu.findItem(R.id.auto).setVisible(mPrefs.getBoolean(HIDDEN_AUTO, false));
		} catch(Exception e){}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.send:
				sendRequest();
				return true;
			case R.id.auto:
				sendAutomaticRequest();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void toggleActionItems(Menu menu, boolean drawerOpen)
	{
		menu.findItem(R.id.send).setVisible(!drawerOpen);
		try {
			if(mPrefs.getBoolean(HIDDEN_AUTO, false))
				menu.findItem(R.id.auto).setVisible(!drawerOpen);
		} catch(Exception e) {}
	}
	
	private void initViews(View v)
	{
		mGrid = (GridView) v.findViewById(R.id.appGrid);
		mScroll = (QuickScroll) v.findViewById(R.id.quickScroll);
		mLoading = (LinearLayout) v.findViewById(R.id.Loading);
		txtProgress = (TextView) v.findViewById(R.id.txtProgress);
	}
	
	private void toggleAutoCard(View v)
	{
		mPrefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, 0);
		
		if(mPrefs.getBoolean(HIDDEN_AUTO, false))
			return;
		
		final View autoCard = ((ViewStub) v.findViewById(R.id.autoStub)).inflate();
		((Button) autoCard.findViewById(R.id.btnAccept)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendAutomaticRequest();
			}
		});
		((Button) autoCard.findViewById(R.id.btnDecline)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Never show this again
				Editor mEditor = mPrefs.edit();
				mEditor.putBoolean(HIDDEN_AUTO, true);
				mEditor.commit();
				
				// TODO Remove this with a fancy animation
				autoCard.setVisibility(View.GONE);
			}
		});
	}
	
	/**
	 * Adjust bottom GridView padding if System Bar
	 * Tint is set and not null.
	 */
	private void adjustPadding()
	{
		try {
			SystemBarTintManager mTintManager = ((MainActivity) getActivity()).getTintManager();
			if(mTintManager != null) {
				int bottomPadding = mTintManager.getConfig().getPixelInsetBottom();
				mGrid.setPadding(mGrid.getPaddingLeft(), mGrid.getPaddingTop(), mGrid.getPaddingRight(), bottomPadding + mGrid.getPaddingBottom());
				((MarginLayoutParams) mScroll.getLayoutParams()).bottomMargin = bottomPadding + ((MarginLayoutParams) mScroll.getLayoutParams()).bottomMargin;
			}
		}
		catch(Exception e) { }
	}
	
	private void setListeners()
	{
		mRequestManager.addAppLoadListener(this);
		mRequestManager.addSendRequestListener(this);
	}
	
	private void removeListeners()
	{
		mRequestManager.removeAppLoadListener(this);
		mRequestManager.removeSendRequestListener(this);
	}
	private void populateGrid()
	{
		// Don't populate yet if there are no apps loaded or the list is empty
		if(mApps == null || mApps.size() == 0)
			return;
		
		mAdapter = new RequestAdapter(getActivity(), mApps);
		mGrid.setAdapter(mAdapter);
		mGrid.setOnItemClickListener(this);
		
		mScroll.init(QuickScroll.TYPE_INDICATOR_WITH_HANDLE, mGrid, mAdapter, QuickScroll.STYLE_HOLO);
		mScroll.setFixedSize(1);
		mScroll.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 48);
		mScroll.setVisibility(View.VISIBLE);
	}
	
	private void toggleLayoutVisibility(boolean visible)
	{
		if(visible) {
			mLoading.setVisibility(View.GONE);
			mGrid.setVisibility(View.VISIBLE);
			mScroll.setVisibility(View.VISIBLE);
		}
		else {
			mGrid.setVisibility(View.GONE);
			mScroll.setVisibility(View.GONE);
			mLoading.setVisibility(View.VISIBLE);
		}
	}
	
	private void sendRequest()
	{
		// Don't do anything if no apps are selected
		if(mRequestManager.getNumSelected() < 1) {
			Toast.makeText(getActivity(), getString(R.string.none_selected), Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Workaround for an issue with the manager. Uncomment if you're not using listeners.
		// mRequestManager.setActivity(getActivity());
		
		// Send Request
		mRequestManager.sendRequestAsync();
		
		// Show the progress dialog
		progressDialog.show();
	}
	
	private void sendAutomaticRequest()
	{
		// Parallel Multi-Loading Can Get Ugly
		if(!mRequestManager.appsLoaded()) {
			Toast.makeText(getActivity(), getString(R.string.wait_apps_load), Toast.LENGTH_SHORT).show();
		}
		
		// Workaround for an issue with the manager. Uncomment if you're not using listeners.
		// mRequestManager.setActivity(getActivity());
		
		// Send the request! (if not already started)
		mRequestManager.sendAutomaticRequestAsync();
		
		// Show the progress dialog
		progressDialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id)
	{
		mAdapter.animateView(position, mGrid);
		AppInfo mApp = mApps.get(position);
		mApp.setSelected(!mApp.isSelected());
		mApps.set(position, mApp);
	}

	@Override
	public void onAppPreload()
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				toggleLayoutVisibility(mRequestManager.appsLoaded());
			}
		});
	}

	@Override
	public void onAppLoading(final int status, final int progress)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(!mRequestManager.appsLoaded()) {
					txtProgress.setText(getString(R.string.loading_apps) + progress);
				}
			}
		});
	}
	
	@Override
	public void onAppLoaded()
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				toggleLayoutVisibility(true);
				mApps = mRequestManager.getApps();
				populateGrid();
			}
		});
	}
	
	@Override
	public void onRequestStart(final boolean automatic) 
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				progressDialog.setTitle(getString(R.string.sending_request));
				progressDialog.setMessage(getString(R.string.preparing_build_request));
				progressDialog.setIndeterminate(true);
			}
		});
	}
	
	@Override
	public void onRequestBuild(final boolean automatic, final int progress)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				progressDialog.setTitle(getString(R.string.sending_request));
				progressDialog.setMessage(getString(R.string.building_icon_request));
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(PkRequestManager.MAX_PROGRESS);
				progressDialog.setProgress(progress);
			}
		});
	}
	
	@Override
	public void onRequestFinished(final boolean automatic, final boolean intentSuccessful, final Intent intent)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// Close progress dialog
				progressDialog.dismiss();

				// Start the intent manually if it was not successful
				if(!intentSuccessful) {
					startActivity(intent);
				}
			}
		});
	}
}
