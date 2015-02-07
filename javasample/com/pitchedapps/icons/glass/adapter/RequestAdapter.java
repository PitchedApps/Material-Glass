package com.pitchedapps.icons.glass.adapter;

import java.util.List;
import java.util.Locale;

import view.QuickScroll.Scrollable;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.icons.glass.R;
import com.pkmmte.requestmanager.AppInfo;

public class RequestAdapter extends BaseAdapter implements Scrollable
{
	private Context mContext;
	private List<AppInfo> mApps;
	
	public RequestAdapter(Context context, List<AppInfo> apps)
	{
		this.mContext = context;
		this.mApps = apps;
	}
	
	@Override
	public int getCount()
	{
		return mApps.size();
	}
	
	@Override
	public AppInfo getItem(int position)
	{
		return mApps.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public String getIndicatorForPosition(int childposition, int groupposition)
	{
		return Character.toString(mApps.get(childposition).getName().charAt(0)).toUpperCase(Locale.getDefault());
	}
	
	@Override
	public int getScrollPosition(int childposition, int groupposition)
	{
		return childposition;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		AppInfo mApp = mApps.get(position);
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_request_item, parent, false);
			
			holder = new ViewHolder();
			holder.txtCode = (TextView) convertView.findViewById(R.id.txtCode);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.chkSelected = (ImageView) convertView.findViewById(R.id.chkSelected);

			holder.Card = (LinearLayout) convertView.findViewById(R.id.Card);
			holder.btnContainer = (FrameLayout) convertView.findViewById(R.id.btnIconContainer);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName.setText(mApp.getName());
		holder.txtCode.setText(mApp.getCode());
		holder.imgIcon.setImageDrawable(mApp.getImage());
		
		if(mApp.isSelected()) {
			selectCard(true, holder.Card);
			holder.chkSelected.setVisibility(View.VISIBLE);
		}
		else {
			selectCard(false, holder.Card);
			holder.chkSelected.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void selectCard(boolean Selected, LinearLayout Card)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			if (Selected)
				Card.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.card2_bg_selected));
			else
				Card.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_card2_bg));
		}
		else {
			if (Selected)
				Card.setBackground(mContext.getResources().getDrawable(R.drawable.card2_bg_selected));
			else
				Card.setBackground(mContext.getResources().getDrawable(R.drawable.selector_card2_bg));
		}
	}
	
	public void animateView(int position, GridView grid) {
		View v = grid.getChildAt(position - grid.getFirstVisiblePosition());

		ViewHolder holder = new ViewHolder();
		holder.Card = (LinearLayout) v.findViewById(R.id.Card);
		holder.btnContainer = (FrameLayout) v.findViewById(R.id.btnIconContainer);
		holder.imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
		holder.chkSelected = (ImageView) v.findViewById(R.id.chkSelected);

		if (mApps.get(position).isSelected())
			animateAppDeselected(holder);
		else
			animateAppSelected(holder);

	}

	private void animateAppSelected(final ViewHolder holderFinal)
	{
		// Declare AnimatorSets
		final AnimatorSet animOut = (AnimatorSet) AnimatorInflater
				.loadAnimator(mContext, R.anim.card_flip_right_out);
		final AnimatorSet animIn = (AnimatorSet) AnimatorInflater.loadAnimator(
				mContext, R.anim.card_flip_left_in);
		animOut.setTarget(holderFinal.btnContainer);
		animIn.setTarget(holderFinal.btnContainer);
		animOut.addListener(new AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
				// Nothing
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				selectCard(true, holderFinal.Card);
				holderFinal.chkSelected.setVisibility(View.VISIBLE);
				animIn.start();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// Nothing
			}

			@Override
			public void onAnimationStart(Animator animation) {
				selectCard(false, holderFinal.Card);
				holderFinal.chkSelected.setVisibility(View.GONE);
			}
		});
		animOut.start();
	}

	private void animateAppDeselected(final ViewHolder holderFinal) {
		// Declare AnimatorSets
		final AnimatorSet animOut = (AnimatorSet) AnimatorInflater
				.loadAnimator(mContext, R.anim.card_flip_left_out);
		final AnimatorSet animIn = (AnimatorSet) AnimatorInflater.loadAnimator(
				mContext, R.anim.card_flip_right_in);
		animOut.setTarget(holderFinal.btnContainer);
		animIn.setTarget(holderFinal.btnContainer);
		animOut.addListener(new AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
				// Nothing
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				selectCard(false, holderFinal.Card);
				holderFinal.chkSelected.setVisibility(View.GONE);
				animIn.start();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// Nothing
			}

			@Override
			public void onAnimationStart(Animator animation) {
				selectCard(true, holderFinal.Card);
				holderFinal.chkSelected.setVisibility(View.VISIBLE);
			}
		});
		animOut.start();
	}
	
	private class ViewHolder
	{
		public TextView txtCode;
		public TextView txtName;
		public ImageView imgIcon;
		public ImageView chkSelected;

		public LinearLayout Card;
		public FrameLayout btnContainer;
	}
}