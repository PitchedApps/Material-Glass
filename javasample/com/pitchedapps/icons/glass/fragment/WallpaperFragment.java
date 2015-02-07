package com.pitchedapps.icons.glass.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.WallpaperThumb2Adapter;
import com.pk.wallpapermanager.CloudWallpaperListener;
import com.pk.wallpapermanager.LocalWallpaperListener;
import com.pk.wallpapermanager.PkWallpaperManager;
import com.pk.wallpapermanager.Wallpaper;

public class WallpaperFragment extends Fragment implements OnItemClickListener, LocalWallpaperListener, CloudWallpaperListener
{
	// Important Wallpaper Manager
	private PkWallpaperManager mWallpaperManager;
	
	// List of all wallpapers
	private List<Wallpaper> mWallpapers;
	
	// Adapter
	private WallpaperThumb2Adapter mAdapter;
	
	// Views
	private GridView mGrid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);
		
		// Initialize your views
		initViews(view);
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		// Get a reference to your PkWallpaperManager
		mWallpaperManager = PkWallpaperManager.getInstance(getActivity());
		
		// Set event listeners
		setListeners();
		
		//
		mWallpapers = mWallpaperManager.getWallpapers();
		
		//
		populateGrid();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		// Remove listeners to prevent weird bugs
		removeListeners();
	}
	
	private void initViews(View v)
	{
		mGrid = (GridView) v.findViewById(R.id.wallpaperGrid);
	}
	
	private void setListeners()
	{
		mWallpaperManager.addLocalWallpaperListener(this);
		mWallpaperManager.addCloudWallpaperListener(this);
	}
	
	private void removeListeners()
	{
		mWallpaperManager.removeLocalWallpaperListener(this);
		mWallpaperManager.removeCloudWallpaperListener(this);
	}
	
	private void populateGrid()
	{
		// Do nothing if wallpaper list is either null or empty
		if(mWallpapers == null || mWallpapers.size() == 0)
			return;
		
		// Populate the GridView
		mAdapter = new WallpaperThumb2Adapter(getActivity(), mWallpapers);
		mGrid.setAdapter(mAdapter);
		mGrid.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		((MainActivity) getActivity()).callWallpaperFragment(position);
	}
	
	@Override
	public void onLocalWallpapersLoading()
	{
		// TODO
	}
	
	@Override
	public void onLocalWallpapersLoaded()
	{
		mWallpapers = mWallpaperManager.getWallpapers();
	}
	
	@Override
	public void onCloudWallpapersLoading()
	{
		// TODO
	}
	
	@Override
	public void onCloudWallpapersLoaded()
	{
		mWallpapers = mWallpaperManager.getWallpapers();
	}
	
	@Override
	public void onCloudWallpapersLoadFailed()
	{
		// TODO
	}
}