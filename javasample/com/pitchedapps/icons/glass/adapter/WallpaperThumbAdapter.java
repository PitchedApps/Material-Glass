package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import view.FRelativeLayout;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.pitchedapps.icons.glass.R;
import com.pk.wallpapermanager.Wallpaper;
import com.squareup.picasso.Picasso;

public class WallpaperThumbAdapter extends BaseAdapter
{
	private Context mContext;
	private List<Wallpaper> mWallpapers;
	private OnThumbnailClickListener mListener;
	private SparseBooleanArray mSelectedItemsIds;
	private boolean clickEnabled;
	private int selectedWallpaper;

	public WallpaperThumbAdapter(Context context, List<Wallpaper> wallpapers)
	{
		this.mContext = context;
		this.mWallpapers = wallpapers;
		this.mSelectedItemsIds = new SparseBooleanArray();
		this.clickEnabled = true;
		this.selectedWallpaper = 0;
	}
	
	public WallpaperThumbAdapter(Context context, List<Wallpaper> wallpapers, OnThumbnailClickListener listener)
	{
		this.mContext = context;
		this.mWallpapers = wallpapers;
		this.mListener = listener;
		this.mSelectedItemsIds = new SparseBooleanArray();
		this.clickEnabled = true;
		this.selectedWallpaper = 0;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.clickEnabled = enabled;
	}
	
	public void setOnThumbnailClickListener(OnThumbnailClickListener listener)
	{
		this.mListener = listener;
	}

	@Override
	public int getCount()
	{
		return this.mWallpapers.size();
	}

	@Override
	public Object getItem(int position)
	{
		return this.mWallpapers.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		Wallpaper mWall = mWallpapers.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.activity_wallpaper_thumb, null);
			
			holder = new ViewHolder();
			holder.Container = (FRelativeLayout) convertView.findViewById(R.id.Container);
			holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
			holder.imgSelected = (ImageView) convertView.findViewById(R.id.imgSelected);
			holder.btnTitle = (Button) convertView.findViewById(R.id.btnTitle);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.Container.setClickable(clickEnabled);
		holder.Container.setOnClickListener(clickEnabled ? getOnClickListener(position) : null);
		
		Picasso.with(mContext).load(mWall.getThumbUri()).placeholder(R.drawable.wall_thumb_placeholder).error(R.drawable.wall_thumb_failed).into(holder.imgThumb);
		
		holder.imgSelected.setVisibility(selectedWallpaper == position ? View.VISIBLE : View.GONE);
		holder.btnTitle.setVisibility(mWall.getTitle().length() > 0 ? View.VISIBLE : View.GONE);
		
		holder.btnTitle.setText(mWall.getTitle());
		
		return convertView;
	}
	
	private OnClickListener getOnClickListener(final int position)
	{
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null) {
					selectedWallpaper = position;
					notifyDataSetChanged();
					mListener.onThumbClick(position);
				}
			}
		};
	}
	
	public interface OnThumbnailClickListener
	{
		public void onThumbClick(int position);
	}
	
	private class ViewHolder
	{
		public FRelativeLayout Container;
		public ImageView imgThumb;
		public ImageView imgSelected;
		public Button btnTitle;
	}
}
