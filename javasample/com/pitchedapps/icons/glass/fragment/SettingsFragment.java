package com.pitchedapps.icons.glass.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.SettingsAdapter;
import com.pitchedapps.icons.glass.model.SettingsItem;
import com.pitchedapps.icons.glass.util.Dialogs;
import com.pitchedapps.icons.glass.util.Utils;
import com.pk.wallpapermanager.PkWallpaperManager;

public class SettingsFragment extends ListFragment
{
	// ID Keys
	private final int WALLPAPERS_CLOUD = 0;
	private final int WALLPAPERS_STORAGE = 1;
	private final int MISC_CACHE = 2;
	private final int MISC_ICON = 3;
	private final int CREDITS_PEOPLE = 4;
	private final int CREDITS_LIBRARIES = 5;
	
	// App Preferences
	private SharedPreferences mPrefs;
	private final String KEY_WALLPAPERS_CLOUD = "Wallpapers Cloud";
	private final String KEY_LAUNCHER_ICON = "Launcher Icon";
	
	// List Adapter
	private SettingsAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		mPrefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, 0);
		addSettings();
		setListAdapter(mAdapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		// Reinitialize our preferences if they somehow became null
		if(mPrefs == null)
			mPrefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, 0);
		
		// We have a custom divider so let's disable this
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
		
		// Apply custom selector
		getListView().setSelector(R.drawable.selector_transparent_lgray);
		getListView().setDrawSelectorOnTop(false);
		
		// Calculte overall cache size asynchronously
		calculateCacheAsync();
	}
	
	private void addSettings()
	{
		mAdapter = new SettingsAdapter(getActivity());
		
		mAdapter.addHeader(getString(R.string.wallpapers));
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_CHECKBOX)
			.title(getString(R.string.cloud_wallpapers))
			.description(getString(R.string.cloud_wallpapers_description))
			.selected(mPrefs.getBoolean(KEY_WALLPAPERS_CLOUD, true))
			.id(WALLPAPERS_CLOUD)
			.build());
		mAdapter.addDivider();
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_TEXT)
			.title(getString(R.string.save_location))
			.description(getString(R.string.save_location_description) + "\n" + PkWallpaperManager.getInstance(getActivity()).getSettings().getSaveLocation())
			.id(WALLPAPERS_STORAGE)
			.build());
		
		mAdapter.addHeader(getString(R.string.miscellaneous));
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_TEXT)
			.title(getString(R.string.clear_cache))
			.description(getString(R.string.clear_cache_description))
			.id(MISC_CACHE)
			.build());
		mAdapter.addDivider();
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_CHECKBOX)
			.title(getString(R.string.toggle_launcher_icon))
			.description(getString(R.string.toggle_launcher_icon_description))
			.selected(mPrefs.getBoolean(KEY_LAUNCHER_ICON, true))
			.id(MISC_ICON)
			.build());
		
		mAdapter.addHeader(getString(R.string.credits));
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_TEXT)
			.title(getString(R.string.people))
			.description(getString(R.string.people_description))
			.id(CREDITS_PEOPLE)
			.build());
		mAdapter.addDivider();
		mAdapter.addItem(new SettingsItem.Builder()
			.type(SettingsAdapter.TYPE_TEXT)
			.title(getString(R.string.libraries))
			.description(getString(R.string.libraries_description))
			.id(CREDITS_LIBRARIES)
			.build());
	}
	
	@Override  
	public void onListItemClick(ListView list, View view, int position, long id)
	{
		SettingsItem mSetting = mAdapter.getItem(position);
		
		switch(mSetting.getID()) {
			case WALLPAPERS_CLOUD:
				// Update preference
				boolean cloudWallpapers = mPrefs.getBoolean(KEY_WALLPAPERS_CLOUD, true);
				Editor mEditor = mPrefs.edit();
				mEditor.putBoolean(KEY_WALLPAPERS_CLOUD, !cloudWallpapers);
				mEditor.commit();
				
				// Update setting in list
				mAdapter.getSetting(WALLPAPERS_CLOUD).setSelected(!cloudWallpapers);
				mAdapter.notifyDataSetChanged();
				break;
			case WALLPAPERS_STORAGE:
				// TODO Use the "Android DirectoryChooser" library for this once we migrate to Android Studio
				break;
			case MISC_CACHE:
				boolean success = Utils.clearCache(getActivity());
				Toast.makeText(getActivity(), success ? getString(R.string.cache_success) : getString(R.string.cache_fail), Toast.LENGTH_SHORT).show();
				calculateCacheAsync();
				break;
			case MISC_ICON:
				// Disable if enabled and vice versa
				boolean iconEnabled = mPrefs.getBoolean(KEY_LAUNCHER_ICON, true);
				if(iconEnabled) {
					Utils.enableLauncherIcon(getActivity());
					Toast.makeText(getActivity(), getString(R.string.launcher_icon_disabled), Toast.LENGTH_LONG).show();
				}
				else {
					Utils.disableLauncherIcon(getActivity());
					Toast.makeText(getActivity(), getString(R.string.launcher_icon_enabled), Toast.LENGTH_LONG).show();
				}
				
				// Update preference
				Editor mEditor2 = mPrefs.edit();
				mEditor2.putBoolean(KEY_LAUNCHER_ICON, !iconEnabled);
				mEditor2.commit();
				
				// Update setting in list
				mAdapter.getSetting(MISC_ICON).setSelected(!iconEnabled);
				mAdapter.notifyDataSetChanged();
				break;
			case CREDITS_PEOPLE:
				Dialogs.getCreditsPeopleDialog(getActivity()).show();
				break;
			case CREDITS_LIBRARIES:
				Dialogs.getCreditsLibraryDialog(getActivity()).show();
				break;
		}
	}
	
	private void calculateCacheAsync()
	{
		new AsyncTask<Void, Void, Void>() {
			private String cacheSize = "Unknown";
			
			@Override
			protected void onPreExecute() {
				mAdapter.getSetting(MISC_CACHE).setDescription(getString(R.string.clear_cache_description) + "\n" + getString(R.string.calculating));
				mAdapter.notifyDataSetChanged();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				cacheSize = Utils.humanReadableByteCount(Utils.getTotalCacheSize(getActivity()), true);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				try {
					mAdapter.getSetting(MISC_CACHE).setDescription(getString(R.string.clear_cache_description) + "\n" + cacheSize);
					mAdapter.notifyDataSetChanged();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
}