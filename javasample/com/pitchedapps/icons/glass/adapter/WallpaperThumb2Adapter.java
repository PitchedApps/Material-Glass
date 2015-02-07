package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import view.SquareImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pitchedapps.icons.glass.R;
import com.pk.wallpapermanager.Wallpaper;
import com.squareup.picasso.Picasso;

public class WallpaperThumb2Adapter extends BaseAdapter
{
	// Essential resources
	private Context mContext;
	private List<Wallpaper> mWallpapers;
	
	public WallpaperThumb2Adapter(Context context, List<Wallpaper> wallpapers)
	{
		this.mContext = context;
		this.mWallpapers = wallpapers;
	}
	
	@Override
	public int getCount()
	{
		return mWallpapers.size();
	}
	
	@Override
	public Wallpaper getItem(int position)
	{
		return mWallpapers.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		Wallpaper mWall = mWallpapers.get(position);
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_wallpaper_thumb, null);
			
			holder = new ViewHolder();
			holder.imgThumb = (SquareImageView) convertView.findViewById(R.id.imgThumb);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		// Load the wallpaper
		if(mWall.isLocal()) {
			holder.imgThumb.setImageResource(mWall.getThumbResource());
			return convertView;
		}
		
		Picasso.with(mContext).load(mWall.getThumbUri()).placeholder(R.drawable.wall_thumb_placeholder).error(R.drawable.wall_thumb_failed).fit().into(holder.imgThumb);
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public SquareImageView imgThumb;
	}
}