package com.pitchedapps.icons.glass.adapter;

import java.util.List;

import view.FixedListView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.model.ChangelogItem;


public class ChangelogAdapter extends BaseAdapter
{
	// Context and list of items
	private Context context;
	private List<ChangelogItem> changeList;

	public ChangelogAdapter(Context context, List<ChangelogItem> changeList)
	{
		// Copy reference
		this.changeList = changeList;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		ChangelogItem entry = changeList.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.dialog_changelog_item, null);
			
			holder = new ViewHolder();
			holder.txtVersion = (TextView) convertView.findViewById(R.id.txtVersion);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			holder.listContent = (FixedListView) convertView.findViewById(R.id.listContent);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Set title and description text
		holder.txtVersion.setText(entry.getVersion());
		holder.txtDate.setText(entry.getDate());
		
		// TODO Subadapter for sublist
		
			
		return convertView;
	}

	@Override
	public int getCount()
	{
		return changeList.size();
	}

	@Override
	public ChangelogItem getItem(int position)
	{
		return changeList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	private static class ViewHolder
	{
		public TextView txtVersion;
		public TextView txtDate;
		public FixedListView listContent;
	}
}