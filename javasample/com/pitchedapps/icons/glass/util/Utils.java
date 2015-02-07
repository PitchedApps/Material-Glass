package com.pitchedapps.icons.glass.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.pitchedapps.icons.glass.MainActivity;
import com.pitchedapps.icons.glass.R;
import com.pitchedapps.icons.glass.model.ChangelogItem;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * This class miscellaneous convenience methods.
 * They may be random utils you use throughout your app multiple times.
 */
public class Utils
{
	public static final String PREFS_NAME = "App Preferences";
	
	/**
	 * Checks to see if the specified package is installed.<br>
	 * Return true if it is or false if it's not installed.
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean isPackageInstalled(String packageName, Context context)
	{
		PackageManager pm = context.getPackageManager();
		
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return true;
		}
		catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Deletes the specified directory.
	 * Returns true if successful, false if not.
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir)
	{
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				if (!deleteDir(new File(dir, children[i])))
					return false;
			}
		}
		return dir.delete();
	}
	
	/**
	 * Enables this app's icon to show in launchers.
	 */
	public static void enableLauncherIcon(Context context)
	{
		ComponentName componentName = new ComponentName(context, MainActivity.class);
		context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}
	
	/**
	 * Disables this app's icon to show in launchers.
	 */
	public static void disableLauncherIcon(Context context)
	{
		ComponentName componentName = new ComponentName(context, MainActivity.class);
		context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
	
	public static List<ChangelogItem> getChangelogContent(Context context)
	{
		List<ChangelogItem> content = new ArrayList<ChangelogItem>();
		
		try {
			// Initialize XmlPullParser and set changelog.xml (from res/raw) as the input
			XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
			XmlPullParser mParser = xmlFactoryObject.newPullParser();
			InputStream input = context.getResources().openRawResource(R.raw.changelog);
			mParser.setInput(input, null);
			
			// Keep track of the event type. Also, reuse one string for memory purposes
			ChangelogItem cItem = null;
			int eventType = mParser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String elementName= mParser.getName();
					if (elementName.equals("changelogitem")) {
						try	{
							cItem = new ChangelogItem();
							
							cItem.setDate(mParser.getAttributeValue(null, "changeDate"));
							cItem.setVersion(mParser.getAttributeValue(null, "versionName"));
						}
						catch(Exception e) {
							// Print and ignore whatever individual error was found. 
							e.printStackTrace();
						}
					}
					else if(elementName.equals("changelogtext") && cItem != null) {
						cItem.addContent(mParser.nextText());
					}
				}
				else if(eventType == XmlPullParser.END_TAG) {
					String elementName= mParser.getName();
					if (elementName.equals("changelogitem") && cItem != null) {
						// Add new item to our ArrayList and reset the object.
						content.add(cItem);
						cItem = null;
					}
				}
				eventType = mParser.next();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static boolean clearCache(Context context)
	{
		boolean result = false;
		
		LruCache pCache = getPicassoCache(context);
		if(pCache != null) {
			pCache.clear();
			result = true;
		}
		
		File cacheDir = context.getCacheDir();
		if(cacheDir != null && cacheDir.isDirectory())
			result = deleteDir(cacheDir);
		
		return result;
	}
	
	public static long getAppCacheSize(Context context)
	{
		long size = 0;
		
		File[] fileList = context.getCacheDir().listFiles();
		for (File mFile : fileList)
		    size = size + mFile.length();
		
		return size;
	}
	
	/**
	 * For some reason, Picasso doesn't allow us to access its 
	 * cache. I guess we'll just have to force our way in through
	 * Java reflection. Returns null if unsuccessful.
	 * <br>
	 * This may stop working in future versions of the Picasso library.
	 * 
	 * @param context
	 * @return
	 */
	public static LruCache getPicassoCache(final Context context)
	{
		try {
			Field cacheField = Picasso.class.getDeclaredField("cache");
			cacheField.setAccessible(true);
			LruCache cache = (LruCache) cacheField.get(Picasso.with(context));
			return cache;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static long getTotalCacheSize(Context context)
	{
		try {
			return (getAppCacheSize(context) + getPicassoCache(context).size());
		}
		catch(Exception e) {
			return getAppCacheSize(context);
		}
	}
	
	@SuppressLint("DefaultLocale")
	public static String humanReadableByteCount(long bytes, boolean si)
	{
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static void viewExpandCollapse(final View v, boolean expand)
	{
		TranslateAnimation anim = null;
		if(expand) {
			anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
			v.setVisibility(View.VISIBLE);
		}
		else {
			anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
			AnimationListener collapselistener= new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					v.setVisibility(View.GONE);
				}
			};
			
			anim.setAnimationListener(collapselistener);
		}
		
		anim.setFillEnabled(true);
		anim.setFillAfter(true);
		anim.setDuration(300);
		anim.setInterpolator(new AccelerateInterpolator(0.5f));
		v.startAnimation(anim);
	}
	
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context)
	{
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    
	    return px;
	}
	
	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context)
	{
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    
	    return dp;
	}
	
	public static Uri resToUri(Context context, int resId)
	{
		return Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
	}
	
	public static String getApacheLicense()
	{
		return getApacheLicense(null);
	}
	
	public static String getApacheLicense(String header)
	{
		StringBuilder builder = new StringBuilder();
		
		if(header != null)
			builder.append(header);
		builder.append("Licensed under the Apache License, Version 2.0 (the \"License\");");
		builder.append("you may not use this file except in compliance with the License.");
		builder.append("You may obtain a copy of the License at\n\n");
		builder.append("   http://www.apache.org/licenses/LICENSE-2.0\n\n");
		builder.append("Unless required by applicable law or agreed to in writing, software");
		builder.append("distributed under the License is distributed on an \"AS IS\" BASIS,");
		builder.append("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
		builder.append("See the License for the specific language governing permissions and");
		builder.append("limitations under the License.");
		
		return builder.toString();
	}
	
	public static String getMITLicense()
	{
		return getMITLicense(null);
	}
	
	public static String getMITLicense(String header)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("The MIT License (MIT)\n\n");
		if(header != null)
			builder.append(header);
		builder.append("Permission is hereby granted, free of charge, to any person obtaining a copy");
		builder.append("of this software and associated documentation files (the \"Software\"), to deal");
		builder.append("in the Software without restriction, including without limitation the rights");
		builder.append("to use, copy, modify, merge, publish, distribute, sublicense, and/or sell");
		builder.append("copies of the Software, and to permit persons to whom the Software is");
		builder.append("furnished to do so, subject to the following conditions:\n\n");
		builder.append("The above copyright notice and this permission notice shall be included in all");
		builder.append("copies or substantial portions of the Software.\n\n");
		builder.append("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR");
		builder.append("IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,");
		builder.append("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE");
		builder.append("AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER");
		builder.append("LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,");
		builder.append("OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE");
		builder.append("SOFTWARE.");
		
		return builder.toString();
	}
}