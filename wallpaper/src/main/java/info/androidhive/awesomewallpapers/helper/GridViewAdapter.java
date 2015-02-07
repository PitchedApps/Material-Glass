package info.androidhive.awesomewallpapers.helper;

import info.androidhive.awesomewallpapers.R;
import info.androidhive.awesomewallpapers.app.AppController;
import info.androidhive.awesomewallpapers.picasa.model.Wallpaper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class GridViewAdapter extends BaseAdapter {

	private Activity _activity;
	private LayoutInflater inflater;
	private List<Wallpaper> wallpapersList = new ArrayList<Wallpaper>();
	private int imageWidth;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public GridViewAdapter(Activity activity, List<Wallpaper> wallpapersList,
			int imageWidth) {
		this._activity = activity;
		this.wallpapersList = wallpapersList;
		this.imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return this.wallpapersList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.wallpapersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) _activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.grid_item_photo, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		// Grid thumbnail image view
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);

		Wallpaper p = wallpapersList.get(position);

		thumbNail.setScaleType(ImageView.ScaleType.CENTER_CROP);
		thumbNail.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth,
				imageWidth));
		thumbNail.setImageUrl(p.getUrl(), imageLoader);

		return convertView;
	}

}