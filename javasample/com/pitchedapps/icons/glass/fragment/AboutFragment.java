package com.pitchedapps.icons.glass.fragment;

import view.FixedListView;
import view.PkScrollView;
import view.PkScrollView.PkScrollViewListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.AboutAdapter;
import com.pitchedapps.icons.glass.adapter.AboutAdapter.onContactListener;
import com.pitchedapps.icons.glass.model.ContactItem;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

public class AboutFragment extends Fragment implements PkScrollViewListener, onContactListener
{
	private int lastTopValue;
	private AboutAdapter mAdapter;
	
	// Views
	private PkScrollView mScroll;
	private FrameLayout mHeader;
	private ImageView imgBanner;
	private FixedListView mList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		initViews(view);
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		fixSystemPadding();
		initSocial();
		//blurBanner(10);
		mAdapter.setOnClickListener(this);
		mScroll.setOnScrollListener(this);
	}
	
	private void initViews(View v)
	{
		mScroll = (PkScrollView) v.findViewById(R.id.scroll);
		mHeader = (FrameLayout) v.findViewById(R.id.header);
		imgBanner = (ImageView) v.findViewById(R.id.imgBanner);
		mList = (FixedListView) v.findViewById(R.id.aboutList);
	}
	
	private void fixSystemPadding()
	{
		// Get the tint manager
		SystemBarTintManager mTintManager = ((MainActivity) getActivity()).getTintManager();
		
		// Return if system tint isn't enabled
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || mTintManager == null)
			return;
		
		// Set the appropriate padding
		mList.setPadding(mList.getPaddingLeft(), mList.getPaddingTop(), mList.getPaddingRight(), mTintManager.getConfig().getPixelInsetBottom());
	}
	
	private void initSocial()
	{
		Resources mRes = getActivity().getResources();
		mAdapter = new AboutAdapter(getActivity());
		mAdapter.addItem(new ContactItem(R.drawable.ic_social_email, mRes.getString(R.string.dev_email), Uri.parse("mailto:" + mRes.getString(R.string.dev_email))));
		mAdapter.addItem(new ContactItem(R.drawable.ic_social_google_play, mRes.getString(R.string.dev_social_play), Uri.parse(mRes.getString(R.string.dev_social_play_link))));
		///mAdapter.addItem(new ContactItem(R.drawable.ic_social_github, mRes.getString(R.string.dev_social_github), Uri.parse(mRes.getString(R.string.dev_social_github_link))));
		mAdapter.addItem(new ContactItem(R.drawable.ic_social_xda, mRes.getString(R.string.dev_social_xda), Uri.parse(mRes.getString(R.string.dev_social_xda_link))));
		mAdapter.addItem(new ContactItem(R.drawable.ic_social_google_plus, mRes.getString(R.string.dev_social_gplus), Uri.parse(mRes.getString(R.string.dev_social_gplus_link))));
		///mAdapter.addItem(new ContactItem(R.drawable.ic_social_twitter, mRes.getString(R.string.dev_social_twitter), Uri.parse(mRes.getString(R.string.dev_social_twitter_link))));
		mList.setAdapter(mAdapter);
	}
	
	private void blurBanner(int blurRadius)
	{
		// DISABLED UNTIL ANDROID STUDIO SUPPORTS RENDERSCRIPT!!!
		Picasso
			.with(getActivity())
			.load(R.drawable.dev_banner)
			//.transform(new BlurTransform())
			.into(imgBanner);
	}
	
	private void parallaxHeader()
	{
		Rect rect = new Rect();
	    mHeader.getLocalVisibleRect(rect);
	    if (lastTopValue != rect.top){
	        lastTopValue = rect.top;
	        mHeader.setY((float) (rect.top/2.0));
	    }
	}
	
	@Override
	public void onClick(Uri link)
	{
		startActivity(new Intent().setData(link));
	}
	
	@Override
	public void onScrollChanged(PkScrollView scrollView, int x, int y, int oldx, int oldy)
	{
		parallaxHeader();
	}
}