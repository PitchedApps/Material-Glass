package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pitchedapps.icons.glass.R;
import com.pkmmte.applylauncher.Launcher;

public class Launcher2Adapter extends BaseAdapter
{
	// Constant Key
	public static final String LAUNCHER_ICON_KEY = "Launcher Icon Key";
	
	// Utilities for populating
	private Context mContext;
	private List<Launcher> mLaunchers;
	
	public Launcher2Adapter(Context context, List<Launcher> launchers)
	{
		this.mContext = context;
		this.mLaunchers = launchers;
	}
	
	@Override
	public int getCount()
	{
		return mLaunchers.size();
	}
	
	@Override
	public Launcher getItem(int position)
	{
		return mLaunchers.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Launcher mLauncher = mLaunchers.get(position);
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_home2_launcher, parent, false);
			
			holder = new ViewHolder();
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		// Set the launcher's name and icon
		holder.imgIcon.setImageResource(mLauncher.getExtraInt(LAUNCHER_ICON_KEY));
		holder.txtName.setText(mLauncher.getName());
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public ImageView imgIcon;
		public TextView txtName;
	}
}