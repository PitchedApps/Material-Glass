package com.pitchedapps.icons.glass;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.adapter.NavDrawerAdapter;
import com.pitchedapps.icons.glass.fragment.AboutFragment;
import com.pitchedapps.icons.glass.fragment.Home2Fragment;
import com.pitchedapps.icons.glass.fragment.IconFragment;
import com.pitchedapps.icons.glass.fragment.RequestFragment;
import com.pitchedapps.icons.glass.fragment.SettingsFragment;
import com.pitchedapps.icons.glass.fragment.WallpaperFragment;
import com.pitchedapps.icons.glass.fragment.WallpaperFullFragment;
import com.pitchedapps.icons.glass.util.Dialogs;
import com.pk.wallpapermanager.PkWallpaperManager;
import com.pk.wallpapermanager.WallpaperSettings;
import com.pkmmte.requestmanager.PkRequestManager;
import com.pkmmte.requestmanager.RequestSettings;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import view.CustomShareActionProvider;
import view.PkDrawerLayout;

public class MainActivity extends FragmentActivity
{
	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";
	
	// Section Constants
	public static final int HOME = 0;
	///public static final int INFO = 1;
	public static final int ICONS = 1;
	public static final int REQUEST = 2;
	public static final int SETTINGS = 3;
	public static final int ABOUT = 4;
	public static final int COMMUNITY = 5;
	public static final int WALLPAPERS = 6;
	
	// ActionBar Stuff
	private ActionBar mActionBar;
	private CharSequence mTitle;
	
	// SystemBarTintManager & Configuration
	private SystemBarTintManager mTintManager;
	
	// Navigation Drawer
	private ActionBarDrawerToggle mDrawerToggle;
	private NavDrawerAdapter mDrawerAdapter;
	private PkDrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	// Sections & Manager
	private boolean fullWallView;
	private int currentSection;
	private List<Fragment> mSections;
	private Fragment mWallpaperFragment;
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initActionBar();
		initViews();
		initTranslucent();
		initRequestManager();
		initWallpaperManager();
		initSections();
		initNavDrawer();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		selectPage(currentSection);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		switch(currentSection) {
			case REQUEST:
				((RequestFragment) mSections.get(REQUEST)).toggleActionItems(menu, mDrawerLayout.isDrawerOpen(mDrawerList));
				
				break;
			case WALLPAPERS:
				if(fullWallView && mWallpaperFragment != null) {
					((WallpaperFullFragment) mWallpaperFragment).toggleActionItems(menu, mDrawerLayout.isDrawerOpen(mDrawerList));
				}
				
				break;
			default:
				break;
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate action bar menu
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Set up share intent
		CustomShareActionProvider mShareActionProvider = (CustomShareActionProvider) menu.findItem(R.id.share).getActionProvider();
		configureShare(mShareActionProvider);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		
		switch (item.getItemId())
		{
			case android.R.id.home:
				if(currentSection == WALLPAPERS && fullWallView) {
					selectPage(WALLPAPERS);
					return true;
				}
			
				return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
			case R.id.rate:
				startActivity(new Intent(Intent.ACTION_VIEW)
					.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getString(R.string.theme_package))));
				
				return true;
			case R.id.changelog:
				Dialogs.getChangelog(this).show();
				
				return true;
			case R.id.contact:
				Intent contactIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.dev_email), null));
				contactIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_subject));
				contactIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(contactIntent, getString(R.string.send_email)));
				
				return true;
			default:
				return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (currentSection == WALLPAPERS && fullWallView)
			{
				selectPage(WALLPAPERS);
				return true;
			}
//			else if(currentSection == INFO) {
//				if(((InfoFragment) mSections.get(INFO)).togglePreview())
//					return super.onKeyDown(keyCode, event);
//				else
//					return true;
//			}
			else
				return super.onKeyDown(keyCode, event);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void initActionBar()
	{
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setIcon(R.drawable.ic_launcher);
		
		Drawable abDrawable = getResources().getDrawable(R.drawable.ic_actionbar_bg);
		Drawable abbottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
		LayerDrawable abLayered = new LayerDrawable(new Drawable[] {abDrawable, abbottomDrawable });
		mActionBar.setBackgroundDrawable(abLayered);
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initTranslucent()
	{
		// Return if user isn't on a version that supports this feature yet
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return;
		
		// Set translucency window flags
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		
		// Initialize your Tint Manager
		mTintManager = new SystemBarTintManager(this);
		
		// Enable status bar tint and set to resource
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.drawable.ic_actionbar_bg);
		
		// Uncomment this line if you'd like to tint the nav bar as well
		//tintManager.setNavigationBarTintEnabled(true);
		
		// Set paddings & margins to main layout so they don't overlap the action/status bar
	    SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();
		int actionBarSize = getResources().getDimensionPixelSize(R.dimen.ab_height);
	    mDrawerList.setPadding(0, actionBarSize + config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
	    MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ((FrameLayout) findViewById(R.id.contentFragment)).getLayoutParams();
	    params.setMargins(0, actionBarSize + config.getStatusBarHeight(), config.getPixelInsetRight(), 0);
	}
	
	private void initViews()
	{
		mDrawerLayout = (PkDrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
	}
	
	/**
	 * Stores sections into an ArrayList for 
	 * later reuse. Order matters so beware when 
	 * messing with this part.
	 */
	private void initSections()
	{
		mFragmentManager = getSupportFragmentManager();
		mSections = new ArrayList<Fragment>();
		mSections.add(new Home2Fragment());
		///mSections.add(new InfoFragment());
		mSections.add(new IconFragment());
		mSections.add(new RequestFragment());
		mSections.add(new SettingsFragment());
		mSections.add(new AboutFragment());
		currentSection = HOME;
	}
	
	/**
	 * Configures the PkRequestManager tool and begins loading 
	 * applications asynchronously ahead of time.
	 */
	private void initRequestManager()
	{
		// Get global instance
		///
        PkRequestManager mRequestManager = PkRequestManager.getInstance(this);
        showToast("Coming Soon...", Toast.LENGTH_LONG);
		// Enable debugging. Disable this during production!
		mRequestManager.setDebugging(false);
		
		// Set your custom settings
		mRequestManager.setSettings(new RequestSettings.Builder()
		.addEmailAddress(getString(R.string.dev_email))
		.emailSubject(getResources().getString(R.string.request_subject))
		.emailPrecontent(getResources().getString(R.string.request_precontent))
		.saveLocation(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Themes/" + getString(R.string.theme_id) + "/Requests")
		.appfilterName(getResources().getString(R.string.request_appfilter))
		.compressFormat(PkRequestManager.PNG)
		.appendInformation(true)
		.createAppfilter(true)
		.createZip(true)
		.filterDefined(true)
		.byteBuffer(2048)
		.compressQuality(100)
		.build());
		
		// Load apps ahead of time
		mRequestManager.loadAppsIfEmptyAsync();
	}
///line below
    private void showToast(String s, int lengthLong) {
    }

    /**
	 * Configures the PkWallpaperManager tool and begins 
	 * fetching wallpapers ahead of time.
	 */
	private void initWallpaperManager()
	{
		// Get global instance
		PkWallpaperManager mWallpaperManager = PkWallpaperManager.getInstance(this);
		
		// Enable debugging. Disable this during production!
		mWallpaperManager.setDebugging(false);
		
		// Set your custom settings
		mWallpaperManager.setSettings(new WallpaperSettings.Builder()
		.addLocalWallpapers(getResources().getStringArray(R.array.wallpapers))
		.packageName(getResources().getString(R.string.theme_package))
		.metadataURL(getResources().getString(R.string.wallpaper_metadata_url))
		.saveLocation(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Themes/" + getString(R.string.theme_id) + "/Wallpapers")
		.thumbSuffix(getResources().getString(R.string.wallpaper_thumb_suffix))
		.build());
		
		// Fetch your wallpapers ahead of time
		mWallpaperManager.fetchWallpapersAsync();
	}
	
	/**
	 * Initializes your Navigation Drawer. 
	 * Menu items are added here. For the header and footer, 
	 * see drawer_header.xml and drawer_footer.xml in the layout folder.
	 */
	private void initNavDrawer()
	{
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_indicator, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				mActionBar.setSubtitle(mTitle);
				invalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				mActionBar.setSubtitle(null);
				invalidateOptionsMenu();
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerAdapter = new NavDrawerAdapter(MainActivity.this);
		mDrawerAdapter.addItem(getResources().getString(R.string.home));
		///mDrawerAdapter.addItem(getResources().getString(R.string.theme_info));
		mDrawerAdapter.addItem(getResources().getString(R.string.theme_icons));
		mDrawerAdapter.addItem(getResources().getString(R.string.request_icons));

		// Add header
		View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_header, mDrawerList, false);
		mDrawerList.addHeaderView(headerView, null, false);
		
		// Add footer
		View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_footer, mDrawerList, false);
		mDrawerList.addFooterView(footerView, null, false);
		
		// Set list adapter
		mDrawerList.setAdapter(mDrawerAdapter);
		
		// Apply onClick listeners to ListView items, footer, and header
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
				selectPage(position - mDrawerList.getHeaderViewsCount());
			}
		});
		((Button) footerView.findViewById(R.id.btnSettings)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPage(SETTINGS);
			}
		});
		((Button) footerView.findViewById(R.id.btnAbout)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPage(ABOUT);
			}
		});
		((Button) footerView.findViewById(R.id.btnCommunity)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPage(COMMUNITY);
			}
		});
		
		// Load banner using Picasso to initialize its singleton ahead of time
		Picasso.with(this).load(R.drawable.dev_banner).into((ImageView) headerView.findViewById(R.id.imgBanner));
	}
	
	/**
	 * Changes activity content to the specified 
	 * section fragment.
	 * 
	 * @param selection
	 */
	public void selectPage(int selection)
	{
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		final FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.quickfade_in, R.anim.quickfade_out);
		
		switch(selection) {
			case HOME:
				transaction.replace(R.id.contentFragment, mSections.get(HOME));
				mTitle = mDrawerAdapter.getItem(HOME);
				break;
//			case INFO:
//				transaction.replace(R.id.contentFragment, mSections.get(INFO));
//				mTitle = mDrawerAdapter.getItem(INFO);
//				break;
			case ICONS:
				// This is a work-around for Issue 42601
				// https://code.google.com/p/android/issues/detail?id=42601
				mSections.set(ICONS, new IconFragment());
				
				transaction.replace(R.id.contentFragment, mSections.get(ICONS));
				mTitle = mDrawerAdapter.getItem(ICONS);
				break;
			case REQUEST:
				transaction.replace(R.id.contentFragment, mSections.get(REQUEST));
				mTitle = mDrawerAdapter.getItem(REQUEST);
				break;
			case SETTINGS:
				transaction.replace(R.id.contentFragment, mSections.get(SETTINGS));
				mTitle = getResources().getString(R.string.settings);
				break;
			case ABOUT:
				/** Small Individual About Dialog **/
				//Dialogs.getAboutDialog(this).show();
				
				/** Full About Fragment **/
				transaction.replace(R.id.contentFragment, mSections.get(ABOUT));
				mTitle = getResources().getString(R.string.about);
				break;
			case COMMUNITY:
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.community_url))));
				break;
			case WALLPAPERS:
				// Alternate wallpaper section
				transaction.replace(R.id.contentFragment, new WallpaperFragment());
				mTitle = getResources().getString(R.string.wallpapers);
				break;
		}
		
		transaction.commit();

		fullWallView = false;
		currentSection = selection;
		mDrawerAdapter.setCurrentPage(selection);
		mDrawerLayout.closeDrawers();
	}
	
	public void callWallpaperFragment(int index)
	{
		fullWallView = true;
		mDrawerToggle.setDrawerIndicatorEnabled(false);
		mWallpaperFragment = WallpaperFullFragment.newInstance(index);
		
		final FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.quickfade_in, R.anim.quickfade_out);
		transaction.replace(R.id.contentFragment, mWallpaperFragment);
		transaction.commit();
	}
	
	/**
	 * Returns the SystemBarTintManager instance for 
	 * modifying tint or retrieving values inside subclasses.
	 * 
	 * @return
	 */
	public SystemBarTintManager getTintManager()
	{
		return mTintManager;
	}
	
	/**
	 * Sets up the share intent. 
	 * Will return prematurely if ShareActionProvider is null.
	 * 
	 * @param mShareActionProvider
	 */
	private void configureShare(CustomShareActionProvider mShareActionProvider)
	{
		// Can't configure if null
		if (mShareActionProvider == null)
			return;
		
		// Create and set the share intent
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.link_share));
		mShareActionProvider.setShareIntent(shareIntent);
	}
}
