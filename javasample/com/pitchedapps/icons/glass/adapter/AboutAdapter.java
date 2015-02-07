package com.pitchedapps.icons.glass.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.model.ContactItem;

public class AboutAdapter extends BaseAdapter
{
	// Essential Resources
	private Context mContext;
	private List<ContactItem> mInfoList;
	private onContactListener mListener;
	
	public AboutAdapter(Context context)
	{
		this.mContext = context;
		this.mInfoList = new ArrayList<ContactItem>();
	}
	
	public AboutAdapter(Context context, List<ContactItem> info)
	{
		this.mContext = context;
		this.mInfoList = info;
	}
	
	public void addItem(ContactItem info)
	{
		this.mInfoList.add(info);
	}
	
	public void setOnClickListener(onContactListener listener)
	{
		this.mListener = listener;
	}
	
	@Override
	public int getCount()
	{
		return mInfoList.size();
	}
	
	@Override
	public ContactItem getItem(int position)
	{
		return mInfoList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		final ContactItem mInfo = mInfoList.get(position);
		
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.fragment_about_item, parent, false);
			
			holder = new ViewHolder();
			holder.mCard = (LinearLayout) convertView.findViewById(R.id.Card);
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.txtData = (TextView) convertView.findViewById(R.id.txtData);
			holder.txtLink = (TextView) convertView.findViewById(R.id.txtLink);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Set icon & data
		holder.imgIcon.setImageResource(mInfo.getIcon());
		holder.txtData.setText(mInfo.getData());
		holder.txtLink.setText(mInfo.getLink().toString());
		
		// onClick event
		if(mListener != null) {
			holder.mCard.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onClick(mInfo.getLink());
				}
			});
		}
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public LinearLayout mCard;
		public ImageView imgIcon;
		public TextView txtData;
		public TextView txtLink;
	}
	
	public interface onContactListener
	{
		void onClick(Uri link);
	}
}