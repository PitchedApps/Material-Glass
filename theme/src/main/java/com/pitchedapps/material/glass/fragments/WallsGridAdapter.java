package com.pitchedapps.material.glass.fragments;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class WallsGridAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    public String wallurl;
    private Context context;
    private int numColumns;
    private ProgressBar mProgress;
    private TextView name;
    private ImageView wall;
    //ImageLoader imageLoader;
    private HashMap<String, String> jsondata = new HashMap<String, String>();

    public WallsGridAdapter(Context context,
                            ArrayList<HashMap<String, String>> arraylist, int numColumns) {
        super();
        this.context = context;
        this.numColumns = numColumns;
        data = arraylist;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FrameLayout card;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.wallpaper_item, parent, false);

        jsondata = data.get(position);

        name = (TextView) itemView.findViewById(R.id.name);
        wall = (ImageView) itemView.findViewById(R.id.wall);

        mProgress = (ProgressBar) itemView.findViewById(R.id.progress);

        name.setText(jsondata.get(Wallpapers.NAME));

        int width = size.x;
        int imageWidth = (int) (width / numColumns);

        if (convertView == null) {
            card = (FrameLayout) itemView.findViewById(R.id.wall_card);
            convertView = card;
            card.setPadding(6, 3, 6, 3);
        } else {
            card = (FrameLayout) convertView;
        }

        wallurl = jsondata.get(Wallpapers.WALL);

        Picasso.with(context)
                .load(wallurl)
//                .error(R.drawable.error);
//                .placeholder(R.drawable.placeholder);
                .resize(imageWidth, imageWidth)
                //TODO fix error above? if error and placeholder aren't there it's fine
                //TODO fix weird loading sizes
                .centerCrop()
                .into(wall);

        return convertView;

    }

}
