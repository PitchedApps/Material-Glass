package com.pitchedapps.icons.glass.fragment;

import java.util.ArrayList;
import java.util.List;

import view.PagerSlidingTabStrip;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.IconAdapter;
import com.pitchedapps.icons.glass.adapter.IconPagerAdapter;

public class IconFragment extends Fragment
{
	// Section Titles
	private List<Fragment> mFragments;
	private int[] mTitles = 
							{
								R.string.icons_latest, 
								R.string.icons_all,  
//								R.string.icons_system,
//								R.string.icons_play,
//								R.string.icons_games,
//								R.string.icons_misc
							};
	
	// Tabs, ViewPager, Adapter
	private PagerSlidingTabStrip mTabs;
	private ViewPager mPager;
	private IconPagerAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_icon, container, false);
		initViews(view);
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		addPages();
		mAdapter = new IconPagerAdapter(getChildFragmentManager(), getActivity(), mFragments, mTitles);
		mPager.setAdapter(mAdapter);
		mTabs.setViewPager(mPager);
	}
	
	private void initViews(View v)
	{
		mTabs = (PagerSlidingTabStrip) v.findViewById(R.id.sectionTabs);
		mPager = (ViewPager) v.findViewById(R.id.iconPager);
	}
	
	private void addPages()
	{
		mFragments = new ArrayList<Fragment>();
		mFragments.add(IconSectionFragment.newInstance(R.array.latesticons));
		mFragments.add(IconSectionFragment.newInstance(R.array.icon_pack));
//		mFragments.add(IconSectionFragment.newInstance(R.array.systemicons));
//		mFragments.add(IconSectionFragment.newInstance(R.array.playicons));
//		mFragments.add(IconSectionFragment.newInstance(R.array.gamesicons));
//		mFragments.add(IconSectionFragment.newInstance(R.array.miscicons));
	}
	
	public static class IconSectionFragment extends Fragment
	{
		public static final String KEY_ARRAY = "ARRAY";
		public static final String KEY_LIST = "TYPE";
		
		private List<Integer> mIcons;
		private IconAdapter mAdapter;
		private GridView mGrid;
		private ProgressBar mProgress;
		
		public static IconSectionFragment newInstance(int list)
		{
			IconSectionFragment mFragment = new IconSectionFragment();
			Bundle args = new Bundle();
			args.putInt(KEY_LIST, list);
			mFragment.setArguments(args);
			
			return mFragment;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.fragment_icon_section, container, false);
			mGrid = (GridView) view.findViewById(R.id.appGrid);
			mProgress = (ProgressBar) view.findViewById(R.id.progressBar);
			return view;
		}
		
		@Override
		public void onStart()
		{
			super.onStart();
			
			// Retrieve the stored drawable array if it's available
			if(getArguments().containsKey(KEY_ARRAY))
				mIcons = getArguments().getIntegerArrayList(KEY_ARRAY);
			
			// Load if empty or display if already loaded
			if(mIcons == null || mIcons.size() == 0)
				loadIconsAsync();
			else {
				mAdapter = new IconAdapter(getActivity(), mIcons);
				mGrid.setAdapter(mAdapter);
			}
		}
		
		private void loadIconsAsync()
		{
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected void onPreExecute() {
					mGrid.setVisibility(View.GONE);
					mProgress.setVisibility(View.VISIBLE);
				}
				
				@Override
				protected Void doInBackground(Void... params) {
					int list = getArguments().getInt(KEY_LIST);
					Resources mRes = getActivity().getResources();
					String packageName = mRes.getString(R.string.theme_package);

					String[] extras = mRes.getStringArray(list);
					mIcons = new ArrayList<Integer>();
					
					for(String extra : extras) {
						int res = mRes.getIdentifier(extra, "drawable", packageName);
						if(res != 0)
							mIcons.add(res);
					}
					
					getArguments().putIntegerArrayList(KEY_ARRAY, (ArrayList<Integer>) mIcons);
					
					return null;
				}
				
				@Override
				protected void onPostExecute(Void p) {
					mProgress.setVisibility(View.GONE);
					mGrid.setVisibility(View.VISIBLE);
					
					mAdapter = new IconAdapter(getActivity(), mIcons);
					mGrid.setAdapter(mAdapter);
				}
				
			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
		}
	}
}
