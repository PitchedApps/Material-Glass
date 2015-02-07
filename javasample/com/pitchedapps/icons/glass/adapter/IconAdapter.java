package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pitchedapps.icons.glass.R;

public class IconAdapter extends BaseAdapter
{
	private Context mContext;
	private List<Integer> mIcons;
	
	public IconAdapter(Context context, List<Integer> icons)
	{
		this.mContext = context;
		this.mIcons = icons;
	}
	
	@Override
	public int getCount()
	{
		return mIcons.size();
	}
	
	@Override
	public Integer getItem(int position)
	{
		return mIcons.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_icon_section_icon, null);
			
			holder = new ViewHolder();
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		// Set the image icon
		holder.imgIcon.setImageResource(mIcons.get(position));
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public ImageView imgIcon;
	}
}