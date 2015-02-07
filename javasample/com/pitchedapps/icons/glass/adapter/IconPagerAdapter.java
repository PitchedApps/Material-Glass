package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IconPagerAdapter extends FragmentPagerAdapter
{
	private Context mContext;
	private List<Fragment> mFragments;
	private int[] mTitles;
	
	public IconPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments, int[] titles)
	{
		super(fm);
		this.mContext = context;
		this.mFragments = fragments;
		this.mTitles = titles;
	}
	
	@Override
	public CharSequence getPageTitle(int position)
	{
		try {
			return this.mContext.getResources().getString(mTitles[position]);
		}
		catch(Exception e) {
			return "null";
		}
	}

	@Override
	public Fragment getItem(int position)
	{
		return mFragments.get(position);
	}

	@Override
	public int getCount()
	{
		return mFragments.size();
	}
}