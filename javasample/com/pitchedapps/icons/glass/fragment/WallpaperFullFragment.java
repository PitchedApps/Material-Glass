package com.pitchedapps.icons.glass.fragment;

import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.R;
import com.pk.wallpapermanager.PkWallpaperManager;
import com.pk.wallpapermanager.Wallpaper;
import com.pk.wallpapermanager.WallpaperDownloadListener;
import com.pk.wallpapermanager.WallpaperSetListener;
import com.squareup.picasso.Picasso;

public class WallpaperFullFragment extends Fragment implements WallpaperSetListener, WallpaperDownloadListener
{
	// Index for parameter passsing
	private static final String WALLPAPER_INDEX_KEY = "Wallpaper Index";
	
	// Wallpaper Manager & Current Wallpaper
	private PkWallpaperManager mWallpaperManager;
	private Wallpaper mWallpaper;
	
	// Progress Dialog
	private ProgressDialog progressDialog;
	
	// UI Handler
	private Handler mHandler;
	
	// Wallpaper ImageView & Zoom Attacher
	private ImageView imgWallpaper;
	private PhotoViewAttacher mAttacher;
	
	// Static function for creating new instance based on a wallpaper
	public static WallpaperFullFragment newInstance(int index)
	{
		WallpaperFullFragment mFragment = new WallpaperFullFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt(WALLPAPER_INDEX_KEY, index);
		mFragment.setArguments(mBundle);
		
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_wallpaper_full, container, false);
		
		imgWallpaper = (ImageView) view.findViewById(R.id.imgWallpaper);
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		// Initialize your UI handler
		mHandler = new Handler();
		
		// Get instance to the wallpaper manager
		mWallpaperManager = PkWallpaperManager.getInstance(getActivity());
		
		// Add event listeners
		setListeners();
		
		// Get the desired wallpaper based on index key
		mWallpaper = mWallpaperManager.getWallpapers().get(getArguments().getInt(WALLPAPER_INDEX_KEY));
		
		loadImage();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		
		// Remove event listeners to prevent weird bugs
		removeListeners();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.wallpaper, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.save:
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.show();
				mWallpaperManager.downloadWallpaperAsync(mWallpaper, null, null);
				
				return true;
			case R.id.apply:
				mWallpaperManager.setWallpaperAsync(mWallpaper);
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void toggleActionItems(Menu menu, boolean drawerOpen)
	{
		menu.findItem(R.id.save).setVisible(!drawerOpen);
		menu.findItem(R.id.apply).setVisible(!drawerOpen);
	}
	
	private void setListeners()
	{
		mWallpaperManager.addWallpaperSetListener(this);
		mWallpaperManager.addWallpaperDownloadListener(this);
	}
	
	private void removeListeners()
	{
		mWallpaperManager.removeWallpaperSetListener(this);
		mWallpaperManager.removeWallpaperDownloadListener(this);
	}
	
	private void loadImage()
	{
		if(mWallpaper.isLocal()) {
			imgWallpaper.setImageResource(mWallpaper.getFullResource());
			mAttacher = new PhotoViewAttacher(imgWallpaper);
			mAttacher.update();
		}
		else {
			new AsyncTask<Void, Void, Void>() {
				private Bitmap Wallpaper;
				
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					imgWallpaper.setImageResource(R.drawable.wall_placeholder);
				}
				
				@Override
				protected Void doInBackground(Void... params) {
					try {
						Wallpaper = Picasso.with(getActivity()).load(mWallpaper.getFullUri()).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
				}
				
				@Override
				protected void onPostExecute(Void p) {
					imgWallpaper.setImageBitmap(Wallpaper);
					mAttacher = new PhotoViewAttacher(imgWallpaper);
					mAttacher.update();
				}
			}.execute();
		}
	}
	
	@Override
	public void onWallpaperDownloading(final Wallpaper mWall, final int progress)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(progressDialog != null)
					progressDialog.setMessage(getActivity().getString(R.string.wallpaper_downloading) + progress + "%");
			}
		});
		
		// TODO
		// Set your progress here
	}
	
	@Override
	public void onWallpaperDownloaded(Wallpaper mWall)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(progressDialog != null)
					progressDialog.dismiss();
				
				Toast.makeText(getActivity(), getActivity().getString(R.string.wallpaper_downloaded), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void onWallpaperDownloadFailed(Wallpaper mWall)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getActivity(), getActivity().getString(R.string.wallpaper_download_failed), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void onWallpaperSet(Bitmap wallpaper)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getActivity(), getActivity().getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void onWallpaperSetFailed()
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getActivity(), getActivity().getString(R.string.wallpaper_set_failed), Toast.LENGTH_SHORT).show();
			}
		});
	}
}