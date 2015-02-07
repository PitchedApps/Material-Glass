package com.pitchedapps.icons.glass;

import java.util.List;

import view.TwoWayView;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.adapter.WallpaperThumbAdapter;
import com.pitchedapps.icons.glass.adapter.WallpaperThumbAdapter.OnThumbnailClickListener;
import com.pk.wallpapermanager.CloudWallpaperListener;
import com.pk.wallpapermanager.LocalWallpaperListener;
import com.pk.wallpapermanager.PkWallpaperManager;
import com.pk.wallpapermanager.Wallpaper;
import com.pk.wallpapermanager.WallpaperDownloadListener;
import com.pk.wallpapermanager.WallpaperSetListener;
import com.squareup.picasso.Picasso;

public class WallpaperActivity extends Activity implements CloudWallpaperListener, LocalWallpaperListener, WallpaperDownloadListener, WallpaperSetListener, OnThumbnailClickListener, OnClickListener
{
	// Important wallpaper handler
	private PkWallpaperManager mWallpaperManager;
	
	// Keep track of currently selected wallpaper
	private Wallpaper selectedWallpaper;
	
	// List of all wallpaper objects
	private List<Wallpaper> mWallpapers;
	
	// Adapter for the thumbnail listview
	private WallpaperThumbAdapter mThumbAdapter;
	
	// UI handler
	private Handler mHandler;
	
	// Shows progress download
	private NotificationManager mNotificationManager;
	private Builder mBuilder;
	
	// Animations
	private Animation inAnimation;
	private Animation outAnimation;
	private boolean animationInProgress;
	
	// Thumbnail Visibility Status
	private boolean thumbsVisible;
	
	// Views
	private ImageView imgWallpaper;
	private TwoWayView mThumbList;
	private Button btnApply;
	private Button btnDownload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		hideStatusBar();
		setContentView(R.layout.activity_wallpaper);
		
		initViews();
		initAnimations();
		initNotification();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		mHandler = new Handler();
		
		// Store a reference of current instance for easier code
		mWallpaperManager = PkWallpaperManager.getInstance(WallpaperActivity.this);
		
		// Add all listeners to this activity. You can remove the ones you don't need.
		mWallpaperManager.addLocalWallpaperListener(this);
		mWallpaperManager.addCloudWallpaperListener(this);
		mWallpaperManager.addWallpaperDownloadListener(this);
		mWallpaperManager.addWallpaperSetListener(this);
		
		mWallpapers = mWallpaperManager.getWallpapers();
		selectWallpaper(0);
		
		mWallpaperManager.fetchWallpapersAsync();
		
		mThumbAdapter = new WallpaperThumbAdapter(this, mWallpapers, this);
		mThumbList.setAdapter(mThumbAdapter);
		animationInProgress = false;
		thumbsVisible = true;
		
		// Toggles the thumbList when you click on the large wallpaper. Remove this line if you don't want that feature.
		imgWallpaper.setOnClickListener(this);

		btnApply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWallpaperManager.setWallpaperAsync(selectedWallpaper);
			}
		});
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = "Downloading wallpaper " + (selectedWallpaper.getTitle().length() > 0 ? selectedWallpaper.getTitle() : mWallpapers.indexOf(selectedWallpaper));
				showToast(msg, Toast.LENGTH_SHORT);
				mWallpaperManager.downloadWallpaperAsync(selectedWallpaper, mNotificationManager, mBuilder);
			}
		});
		// TODO Make it so users can select multiple wallpapers to download at once. Use SparseBooleanArray in Adapter.
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		
		// Removes listeners once activity when activity isn't visible
		mWallpaperManager.removeLocalWallpaperListener(this);
		mWallpaperManager.removeCloudWallpaperListener(this);
		mWallpaperManager.removeWallpaperDownloadListener(this);
		mWallpaperManager.removeWallpaperSetListener(this);
	}
	
	private void hideStatusBar()
	{
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void initViews()
	{
		imgWallpaper = (ImageView) findViewById(R.id.imgWallpaper);
		mThumbList = (TwoWayView) findViewById(R.id.thumbList);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnDownload = (Button) findViewById(R.id.btnDownload);
	}
	
	private void initAnimations()
	{
		// Initializes animations. You can set it to something other than fading animation if you'd like
		inAnimation = AnimationUtils.loadAnimation(this,  R.anim.quickfade_in);
		outAnimation = AnimationUtils.loadAnimation(this,  R.anim.quickfade_out);
		
		// Makes sure the thumb list stays in the end state
		inAnimation.setFillAfter(true);
		outAnimation.setFillAfter(true);
		
		// In animation listener
		inAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				animationInProgress = true;
				mThumbList.setEnabled(true);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				thumbsVisible = true;
				mThumbAdapter.setEnabled(true);
				mThumbAdapter.notifyDataSetChanged();
				animationInProgress = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
		
		// Out animation listener
		outAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				animationInProgress = true;
				mThumbList.setEnabled(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				thumbsVisible = false;
				mThumbAdapter.setEnabled(false);
				mThumbAdapter.notifyDataSetChanged();
				animationInProgress = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
	}
	
	private void selectWallpaper(int position)
	{
		selectedWallpaper = mWallpapers.get(position);
		
		Picasso.with(this).load(selectedWallpaper.getFullUri()).placeholder(R.drawable.wall_placeholder).error(R.drawable.wall_failed).into(imgWallpaper);
	}
	
	private void toggleThumbnails(boolean animate)
	{
		if(animate) {
			if(thumbsVisible && !animationInProgress)
				mThumbList.startAnimation(outAnimation);
			else if (!animationInProgress)
				mThumbList.startAnimation(inAnimation);
		}
		else {
			thumbsVisible = !thumbsVisible;
			mThumbList.setVisibility(thumbsVisible ? View.VISIBLE : View.GONE);
			mThumbList.setEnabled(thumbsVisible);
			mThumbAdapter.setEnabled(thumbsVisible);
			mThumbAdapter.notifyDataSetChanged();
		}
	}
	
	private void initNotification()
	{
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle(getString(R.string.theme_name))
		.setSmallIcon(R.drawable.ic_notify_wall);
	}
	
	private void IntentService()
	{
		Intent resultIntent = new Intent(this, ResultActivity.class);
		//resultIntent.putExtra(Constants.EXTRA_MESSAGE, msg);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
		        Intent.FLAG_ACTIVITY_CLEAR_TASK);
		     
		// Because clicking the notification launches a new ("special") activity, 
		// there's no need to create an artificial back stack.
		PendingIntent resultPendingIntent =
		         PendingIntent.getActivity(
		         this,
		         0,
		         resultIntent,
		         PendingIntent.FLAG_UPDATE_CURRENT
		);

		// This sets the pending intent that should be fired when the user clicks the
		// notification. Clicking the notification launches a new activity.
		mBuilder.setContentIntent(resultPendingIntent);
		
	}
	
	private void showToast(final String msg, final int duration)
	{
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(WallpaperActivity.this, msg, duration).show();
			}
		});
	}
	
	@Override
	public void onClick(View v)
	{
		// Toggles the thumbnail list. True for animation. False for no animation.
		toggleThumbnails(true);
	}

	@Override
	public void onThumbClick(int position)
	{
		selectWallpaper(position);
	}

	@Override
	public void onLocalWallpapersLoading()
	{
		// TODO
	}

	@Override
	public void onLocalWallpapersLoaded()
	{
		mWallpapers = mWallpaperManager.getWallpapers();
	}

	@Override
	public void onCloudWallpapersLoading()
	{
		// TODO
	}

	@Override
	public void onCloudWallpapersLoaded()
	{
		mWallpapers = mWallpaperManager.getWallpapers();
	}

	@Override
	public void onCloudWallpapersLoadFailed()
	{
		showToast("Error loading cloud wallpapers. Check your internet connection and try again!", Toast.LENGTH_LONG);
	}

	@Override
	public void onWallpaperDownloading(Wallpaper mWallpaper, int progress)
	{
		mBuilder.setProgress(PkWallpaperManager.MAX_PROGRESS, progress, false).setContentText(getString(R.string.wall_download_progress) + progress + "%");
		mNotificationManager.notify(0, mBuilder.build());
	}

	
	@Override
	public void onWallpaperDownloaded(Wallpaper mWallpaper)
	{
	    mBuilder.setContentText("Download complete").setProgress(0,0,false);
	    mNotificationManager.notify(0, mBuilder.build());
	}

	@Override
	public void onWallpaperDownloadFailed(Wallpaper mWallpaper)
	{
		String errorMSG = "Error downloading wallpaper " + (mWallpaper.getTitle().length() > 0 ? mWallpaper.getTitle() : mWallpapers.indexOf(mWallpaper)) + "!";
		showToast(errorMSG, Toast.LENGTH_LONG);
	}

	@Override
	public void onWallpaperSet(Bitmap wallpaper)
	{
		showToast("Wallpaper set!", Toast.LENGTH_LONG);
	}

	@Override
	public void onWallpaperSetFailed()
	{
		showToast("Error applying wallpaper!", Toast.LENGTH_LONG);
	}
}
