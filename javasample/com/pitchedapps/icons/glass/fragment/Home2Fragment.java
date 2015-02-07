package com.pitchedapps.icons.glass.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;

import com.pitchedapps.icons.glass.DonationsActivity;
import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.Launcher2Adapter;
import com.pitchedapps.icons.glass.util.Dialogs;
import com.pkmmte.applylauncher.Launcher;
import com.pkmmte.applylauncher.PkApplyLauncher;

import java.util.ArrayList;
import java.util.List;

import view.FixedGridView;

public class Home2Fragment extends Fragment
{
	// List of launchers & adapter
	List<Launcher> mLaunchers;
	Launcher2Adapter mAdapter;
	
	// Views
	private FixedGridView launcherGrid;
	private Button btnMore;
	private Button btnWallpapers;
	private Button btnDonate;
	///private ImageButton btnPaypal;
	private Button btnPaypal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_home2, container, false);
		initViews(view);
		loadLaunchers();
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		setListeners();
	}
	
	/**
	 * Initialize your views here
	 * @param v
	 */
	@SuppressLint("WrongViewCast")
    private void initViews(View v)
	{
		launcherGrid = (FixedGridView) v.findViewById(R.id.launcherGrid);
		btnMore = (Button) v.findViewById(R.id.btnMore);
		btnWallpapers = (Button) v.findViewById(R.id.btnWallpapers);
		///btnManual = (Button) v.findViewById(R.id.btnManual);
		btnDonate = (Button) v.findViewById(R.id.btnDonate);
		///btnAutomatic = (ImageButton) v.findViewById(R.id.btnAuto);
		btnPaypal = (Button) v.findViewById(R.id.btnPaypal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_view);
                scrollView.setPadding(0, 0, 0, getNavigationBarHeight());
                scrollView.setClipToPadding(false);
            }
        }
	}

    private int getNavigationBarHeight() {
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
	
	/**
	 * Loads the launchers to display in the main grid.
	 * Feel free to edit it to change launchers and/or images.
	 */
	private void loadLaunchers()
	{
		// Create a custom list of launchers of your choice
		mLaunchers = new ArrayList<Launcher>();
		mLaunchers.add(PkApplyLauncher.getActionLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_action));
		mLaunchers.add(PkApplyLauncher.getNovaLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_nova));
		mLaunchers.add(PkApplyLauncher.getApexLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_apex));
		mLaunchers.add(PkApplyLauncher.getAdwLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_adw));
		mLaunchers.add(PkApplyLauncher.getSmartLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_smart));
		mLaunchers.add(PkApplyLauncher.getAviateLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_aviate));
		mLaunchers.add(PkApplyLauncher.getNextLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_next));
		mLaunchers.add(PkApplyLauncher.getGoLauncher().putExtra(Launcher2Adapter.LAUNCHER_ICON_KEY, R.drawable.ic_launcher_go));
		
		// Create adapter and apply it to the launcher grid
		mAdapter = new Launcher2Adapter(getActivity(), mLaunchers);
		launcherGrid.setAdapter(mAdapter);
	}
	
	private void setListeners()
	{
		launcherGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Fix
				// Get the selected launcher...
				Launcher selectedLauncher = mLaunchers.get(position);
				
				// Apply the launcher...
				boolean result = PkApplyLauncher.applyLauncher(selectedLauncher, getActivity(), getActivity().getString(R.string.package_name));
				
				// Launcher play store if not found
				if(!result) {
					PkApplyLauncher.launchPlayStore(selectedLauncher, getActivity());
					// Show the toast you like
					//Toast.makeText(context, getActivity().getString(selectedLauncher.getExtraInt("TOAST")), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show the apply launcher dialog
				Dialogs.getLaunchersDialog(getActivity()).show();
			}
		});
		
		btnWallpapers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).selectPage(MainActivity.WALLPAPERS);
				//Intent intent = new Intent(getActivity(), WallpaperActivity.class);
				//startActivity(intent);
			}
		});
        btnDonate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                //TODO fix this....
                Intent intent = new Intent(getActivity(), DonationsActivity.class);
                startActivity(intent);
              /*  Intent donate = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
                        ("https://play.google.com/store/apps/details?id=com.pitch.donate"));
                startActivity(donate);*/
			}
		});
        btnPaypal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent paypal = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
                        ("http://goo.gl/9VJXsj"));
                startActivity(paypal);
			}
		});
	}
}
