package com.pitchedapps.icons.glass.adapter;

import java.util.ArrayList;
import java.util.List;

import view.CircularImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.model.CreditsPeopleItem;
import com.pitchedapps.icons.glass.util.RoundTransform;
import com.squareup.picasso.Picasso;

public class CreditsPeopleAdapter extends BaseAdapter
{
	// View Types
	public static final int TYPE_MAIN = 0;
	public static final int TYPE_TESTER = 1;
	public static final int TYPE_MAX_COUNT = 2;
	
	// Essential Resources
	private Context mContext;
	private List<CreditsPeopleItem> mPeople;
	private LayoutInflater mInflater;
	
	public CreditsPeopleAdapter(Context context)
	{
		this.mContext = context;
		this.mPeople = new ArrayList<CreditsPeopleItem>();
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public CreditsPeopleAdapter(Context context, List<CreditsPeopleItem> people)
	{
		this.mContext = context;
		this.mPeople = people;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addItem(CreditsPeopleItem person)
	{
		this.mPeople.add(person);
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return mPeople.get(position).getType();
	}
	
	@Override
	public int getViewTypeCount()
	{
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getCount()
	{
		return mPeople.size();
	}

	@Override
	public CreditsPeopleItem getItem(int position)
	{
		return mPeople.get(position);
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
		final CreditsPeopleItem mPerson = mPeople.get(position);
		int type = getItemViewType(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
			
			switch(type) {
				case TYPE_MAIN:
					convertView = mInflater.inflate(R.layout.credits_people_special, parent, false);

					holder.mCard = (LinearLayout) convertView.findViewById(R.id.Card);
					holder.imgAvatar = (CircularImageView) convertView.findViewById(R.id.imgAvatar);
					holder.imgBanner = (ImageView) convertView.findViewById(R.id.imgBanner);
					holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
					holder.txtTagline = (TextView) convertView.findViewById(R.id.txtTagline);
					holder.imgExpCol = (ImageView) convertView.findViewById(R.id.imgExpCol);
					holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
					break;
				case TYPE_TESTER:
					convertView = mInflater.inflate(R.layout.credits_people, parent, false);
					
					holder.mCard = (LinearLayout) convertView.findViewById(R.id.Card);
					holder.imgAvatar = (CircularImageView) convertView.findViewById(R.id.imgAvatar);
					holder.imgBanner = (ImageView) convertView.findViewById(R.id.imgBanner);
					holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
					holder.txtTagline = (TextView) convertView.findViewById(R.id.txtTagline);
					holder.imgExpCol = (ImageView) convertView.findViewById(R.id.imgExpCol);
					holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
					break;
			}
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPerson.setExpanded(!mPerson.isExpanded());
				notifyDataSetChanged();
			}
		});
		
		if(mPerson.getAvatar() != null) {
			holder.imgAvatar.setVisibility(View.VISIBLE);
			Picasso.with(mContext).load(mPerson.getAvatar()).transform(new RoundTransform(0, 0)).into(holder.imgAvatar);
		}
		else
			holder.imgAvatar.setVisibility(View.INVISIBLE);
		
		if(mPerson.getBanner() != null) {
			holder.imgBanner.setVisibility(View.VISIBLE);
			Picasso.with(mContext).load(mPerson.getBanner()).into(holder.imgBanner);
		}
		else
			holder.imgBanner.setVisibility(View.INVISIBLE);
		
		holder.txtName.setText(mPerson.getName());
		holder.txtTagline.setText(mPerson.getTagline());
		holder.txtDescription.setText(mPerson.getDescription());
		
		holder.imgExpCol.setImageResource(mPerson.isExpanded() ? R.drawable.ic_up : R.drawable.ic_down);
		holder.txtDescription.setVisibility(mPerson.isExpanded() ? View.VISIBLE : View.GONE);
		if(type == TYPE_MAIN)
			holder.txtTagline.setVisibility(mPerson.isExpanded() ? View.VISIBLE : View.GONE);
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public LinearLayout mCard;
		public CircularImageView imgAvatar;
		public ImageView imgBanner;
		public TextView txtName;
		public TextView txtTagline;
		public ImageView imgExpCol;
		public TextView txtDescription;
	}
}
