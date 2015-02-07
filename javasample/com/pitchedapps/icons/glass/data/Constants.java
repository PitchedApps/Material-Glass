package com.pitchedapps.icons.glass.data;

import android.app.Activity;
import android.os.Environment;

public class Constants
{
	public static final String APP_NAME = "Pitched Glass";
	
	public static final String PACKAGE_ID = "Pitchedapps";
	public static final String PACKAGE_NAME = "com.pitchedapps.icons.glass";
	
	public static final String SERVER_URL = "http://www.the1template.com";

	public static final String THUMB_SUFFIX = "_thumb";
	
	public static final String CLOUD_STORAGE_URL = "http://storage.the1template.com";
	public static final String CLOUD_STORAGE_WALLPAPER_URL = CLOUD_STORAGE_URL + "/wallpapers/" + PACKAGE_ID + "PitchedWalls";
	public static final String CLOUD_WALLPAPER_PATH = "wallpapers/" + PACKAGE_ID + "PitchedWalls";
	public static final String CLOUD_WALLPAPER_DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_NAME;
	
	public static final boolean DEBUG_WALLPAPER_MANAGER = true;
	public static final boolean CLOUD_WALLPAPERS_ENABLED = true;

	public static final String WALLPAPER_RESOURCE_URI_BASE = "android.resource://" + PACKAGE_NAME + "/drawable/";

	public static final String REQUEST_ICONS_ACTIVITY = "com.pitchedapps.icons.glass.RequestIconsMain";
	
	
	/** MUZEI STUFF **/
	// Received intents
    public static final String ACTION_SUBSCRIBE = "com.pitchedapps.icons.glass.action.SUBSCRIBE";
    public static final String EXTRA_SUBSCRIBER_COMPONENT = "com.pitchedapps.icons.glass.extra.SUBSCRIBER_COMPONENT";
    public static final String EXTRA_TOKEN = "com.pitchedapps.icons.glass.extra.TOKEN";

    public static final String ACTION_HANDLE_COMMAND = "com.pitchedapps.icons.glass.action.HANDLE_COMMAND";
    public static final String EXTRA_COMMAND_ID = "com.pitchedapps.icons.glass.extra.COMMAND_ID";
    public static final String EXTRA_SCHEDULED = "com.pitchedapps.icons.glass.extra.SCHEDULED";

    public static final String ACTION_NETWORK_AVAILABLE = "com.pitchedapps.icons.glass.action.NETWORK_AVAILABLE";

    // Sent intents
    public static final String ACTION_PUBLISH_STATE = "com.pitchedapps.icons.glass.action.PUBLISH_UPDATE";
    public static final String EXTRA_STATE = "com.pitchedapps.icons.glass.extra.STATE";
	
	/** SETTINGS **/
	private static final String KEY_THEME = "theme";
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static void applyDarkThemeSetting(Activity activity, int dark) {
        if (!"com.pitchedapps.icons.glass".equals(activity.getPackageName()))
            return;
        try {
            //if (getTheme(activity) == THEME_DARK)
                activity.setTheme(dark);
        }
        catch (Exception e) {
        }
    }
    
    /** ABOUT DEV **/
    public static final String LINK_GITHUB_THE1DYNASTY = "https://github.com/the1dynasty";
    public static final String LINK_GITHUB_PK = "https://github.com/Pkmmte";
    
    public static final String LINK_TWITTER_THE1DYNASTY = "https://twitter.com/the1dynasty";
    public static final String LINK_TWITTER_PK = "https://twitter.com/Pkmmte";

    public static final String LINK_GPLUS_THE1DYNASTY = "https://plus.google.com/u/0/b/110748421773388678236/+The1dynasty/posts";
    public static final String LINK_GPLUS_PK = "https://plus.google.com/u/0/b/110748421773388678236/102226057091361048952/posts";
    public static final String LINK_GPLUS_RUFFLEZ = "https://plus.google.com/+AndrewRuffolo/posts";
    public static final String LINK_GPLUS_BOBBY = "https://plus.google.com/+BobbyMcKenzie/posts";
    public static final String LINK_GPLUS_ARANDOM = "https://plus.google.com/+AlexMiller/posts";
    public static final String LINK_GPLUS_ALIM = "https://plus.google.com/+AlimHaque/posts";
    public static final String LINK_GPLUS_TOXIC = "https://plus.google.com/+NitishSaxena/posts";
    public static final String LINK_GPLUS_LANCE = "https://plus.google.com/+LanceStratton/posts";

    public static final String LINK_PLAYSTORE_THE1DYNASTY = "https://play.google.com/store/apps/developer?id=the1dynasty";
    public static final String LINK_PLAYSTORE_PK = "https://play.google.com/store/apps/developer?id=Pkmmte";
    public static final String LINK_PLAYSTORE_RUFFLEZ = "https://play.google.com/store/apps/developer?id=Rufflez";
    public static final String LINK_PLAYSTORE_BOBBY = "https://play.google.com/store/apps/developer?id=Bobby+McKenzie";
    public static final String LINK_PLAYSTORE_ARANDOM = "https://play.google.com/store/apps/developer?id=arandompackage";
    public static final String LINK_PLAYSTORE_ALIM = "https://play.google.com/store/apps/developer?id=Alim+Haque";
    public static final String LINK_PLAYSTORE_TOXIC = "https://play.google.com/store/apps/developer?id=ToxicThunder";
    
    public static final String LINK_YOUTUBE_RUFFLEZ = "https://www.youtube.com/user/gatorman55";
    public static final String LINK_DRIBBBLE_ARANDOM = "http://dribbble.com/arandompackage";
    public static final String LINK_FACEBOOK_THE1DYNASTY = "https://www.facebook.com/pages/The1dynasty/428692913887012";
    
    /**
    //TODO finish adding theme support
    public static final int getTheme(Context context) {
        return getInt(context, KEY_THEME, THEME_LIGHT);
    }
    
    public static final void setTheme(Context context, int theme) {
        setInt(context, KEY_THEME, theme);
    }
    **/
}
