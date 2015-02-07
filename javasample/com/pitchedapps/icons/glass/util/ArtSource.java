package com.pitchedapps.icons.glass.util;

import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.data.Constants;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.pk.wallpapermanager.PkWallpaperManager;
import com.pk.wallpapermanager.Wallpaper;
import com.pk.wallpapermanager.WallpaperSettings;

public class ArtSource extends RemoteMuzeiArtSource {

    private static final String LOG_TAG = "ArtSource";
    private static final String SOURCE_NAME = "ArtSource";
    private static final int ROTATE_TIME_MILLIS = 60 * 1000;//3 * 60 * 60 * 1000; // rotate every 3 hours
    private static final int RETRY_TIME_MILLIS = 10 * 1000; // retry loading every 10 seconds...
	
    private PkWallpaperManager mWallpaperManager;
    private List<Wallpaper> mWallpapers;
    
    public ArtSource()
    {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;
        if(mWallpaperManager == null)
        	initWallpaperManager();
        
        mWallpapers = mWallpaperManager.getWallpapers();

        //if (response == null) {
        //    throw new RetryException();
        //}

        if (mWallpapers.size() == 0) {
            Log.w(LOG_TAG, "No photos returned from API.");
            try {
				mWallpaperManager.fetchWallpapers();
			} catch (Exception e) {
				e.printStackTrace();
			}
            scheduleUpdate(System.currentTimeMillis() + RETRY_TIME_MILLIS);
            //throw new RetryException();
            return;
        }

        Random random = new Random();
        Wallpaper mWall = null;
        String token;
        while (true) {
            mWall = mWallpapers.get(random.nextInt(mWallpapers.size()));
            token = String.valueOf(mWall.hashCode());
			
			// Test
			//Log.d(LOG_TAG, "URI: " + mWall.getFullUri());
			
            if ((mWallpapers.size() <= 1 || !TextUtils.equals(token, currentToken)) && !mWall.isLocal()) {
            	Log.d(LOG_TAG, "Broke from loop");
                break;
            }
        }
        publishArtwork(new Artwork.Builder()
                .title(mWall.getTitle())
                .byline(mWall.getByLine())
                .imageUri(mWall.getFullUri())
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.google.com")))
                .build());

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }
    
    private void initWallpaperManager()
	{
		// Create global instance
		PkWallpaperManager.createInstance(this);
		PkWallpaperManager mWallpaperManager = PkWallpaperManager.getInstance();
		
		// Enable debugging. Disable this during production!
		mWallpaperManager.setDebugging(Constants.DEBUG_WALLPAPER_MANAGER);
		
		// Set your custom settings
		mWallpaperManager.setSettings(new WallpaperSettings.Builder()
		.addLocalWallpapers(getResources().getStringArray(R.array.wallpapers))
		.packageName(Constants.PACKAGE_NAME)
		.saveLocation(Constants.CLOUD_WALLPAPER_DOWNLOAD_DIR)
		.thumbSuffix(Constants.THUMB_SUFFIX)
		.build());
	}
}
