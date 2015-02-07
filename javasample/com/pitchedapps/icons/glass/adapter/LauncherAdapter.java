package com.pitchedapps.icons.glass.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pitchedapps.icons.glass.R;
import com.pkmmte.applylauncher.Launcher;

public class LauncherAdapter extends BaseAdapter
{
	// Static Key Constants
	public static final String ICON_KEY = "ICON";
	public static final String INSTALLED_KEY = "INSTALLED";
	
	// Used to populate the list
	private Context mContext;
	private List<Launcher> mLaunchers;
	
	// Special attributes
	private ColorMatrixColorFilter grayscaleFilter;
	private Resources mRes;
	private Typeface tfTitle;

	public LauncherAdapter(Context context, List<Launcher> launchers)
	{
		this.mContext = context;
		this.mLaunchers = launchers;
		
		this.mRes = context.getResources();
		// Sets the color filter for the Apply card when Not Installed
		ColorMatrix matrix = new ColorMatrix();
	    matrix.setSaturation(0); // 0-255 // 0 means grayscale // 255 means fully saturated
	    this.grayscaleFilter = new ColorMatrixColorFilter(matrix);
		
		/* 
		 * Sets the font type for the title
		 * Make sure the font file is in the projects Asset folder
		 * Default for this template is Roboto-Thin (renamed to "themefont.ttf")
		 * themefont.ttf is the font the theme grabs also
		 */
		this.tfTitle = Typeface.createFromAsset(context.getAssets(), "themefont.ttf");
	}
	
	@Override
	public int getCount()
	{
		return mLaunchers.size();
	}

	@Override
	public Launcher getItem(int position)
	{
		return mLaunchers.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		Launcher mLauncher = mLaunchers.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.dialog_launchers_item, parent, false);
			
			holder = new ViewHolder();
			holder.imgBanner = (ImageView) convertView.findViewById(R.id.imgBanner);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtInstalled = (TextView) convertView.findViewById(R.id.txtInstalled);
			
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.txtTitle.setTypeface(tfTitle);
		holder.txtTitle.setText(mLauncher.getName());
		holder.imgBanner.setImageResource(mLauncher.getExtraInt(ICON_KEY));
		
		if(mLauncher.getExtraBoolean(INSTALLED_KEY)) {
			holder.txtInstalled.setText(mRes.getString(R.string.installed));
			holder.txtInstalled.setTextColor(mRes.getColor(R.color.holo_green_light));
			holder.imgBanner.clearColorFilter();
		}
		else {
			holder.txtInstalled.setText(mRes.getString(R.string.not_installed));
			holder.txtInstalled.setTextColor(mRes.getColor(R.color.holo_red_light));
			holder.imgBanner.setColorFilter(grayscaleFilter);
		}
		
		return convertView;
	}
	
	private class ViewHolder
	{
		public ImageView imgBanner;
		public TextView txtTitle;
		public TextView txtInstalled;
	}
}