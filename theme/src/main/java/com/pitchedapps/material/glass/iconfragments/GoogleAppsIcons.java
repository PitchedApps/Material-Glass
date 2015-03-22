package com.pitchedapps.material.glass.iconfragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.pitchedapps.material.glass.R;

import java.util.ArrayList;


public class GoogleAppsIcons extends Fragment {

    public View iconDialog;
    public ImageView dialogIcon;
    public Drawable icono;
    public String AppName;
    LayoutInflater inflater;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.icons_grid, container, false);

        context = getActivity();
        int iconSize = getResources().getDimensionPixelSize(R.dimen.allapps_icon_preview);
        GridView gridview = (GridView) view.findViewById(R.id.icons_grid);
        final IconAdapter icAdapter = new IconAdapter(getActivity(), iconSize);
        gridview.setAdapter(icAdapter);
        return view;

    }

    private class IconAdapter extends BaseAdapter {
        private Context mContext;
        private int iconSource;
        private ArrayList<Integer> mThumbs;
        private ArrayList<String> mThemedApps;

        public IconAdapter(Context mContext, int iconsize) {
            this.mContext = mContext;
            loadIcon();
        }

        @Override
        public int getCount() {
            return mThumbs.size();
        }

        @Override
        public Object getItem(int position) {
            return mThumbs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FrameLayout icon;
            ImageView iconImg;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View iconView = inflater.inflate(R.layout.icon, parent, false);
            iconDialog = inflater.inflate(R.layout.dialog_icon, null);

            iconSource = mThumbs.get(position);
            // AppName = "AppName";

            icono = getResources().getDrawable(iconSource);

            icon = (FrameLayout) iconView.findViewById(R.id.icon);
            iconImg = (ImageView) iconView.findViewById(R.id.icon_img);
            // dialogIcon = (ImageView) iconDialog.findViewById(R.id.dialogicon);

            if (convertView == null) {
                icon.setLayoutParams(new GridView.LayoutParams(120, 120));
            } else {
                icon = (FrameLayout) convertView;
            }

            iconImg.setImageResource(iconSource);

            /*
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogIcon.setImageDrawable(null);
                    dialogIcon.setImageDrawable(icono);
                    new MaterialDialog.Builder(context)
                            .title(AppName)
                            .customView(iconDialog)
                            .positiveText(R.string.close)
                            .show();
                }
            });
            */

            return icon;
        }

        private void loadIcon() {
            mThumbs = new ArrayList<Integer>();
            // mThemedApps = new ArrayList<String>();

            final Resources resources = getResources();
            final String packageName = getActivity().getApplication().getPackageName();
            addIcon(resources, packageName, R.array.google);
            // addApps(resources, R.array.google_apps);

        }

        private void addIcon(Resources resources, String packageName, int list) {
            final String[] extras = resources.getStringArray(list);
            for (String extra : extras) {
                int res = resources.getIdentifier(extra, "drawable", packageName);
                if (res != 0) {
                    final int thumbRes = resources.getIdentifier(extra, "drawable", packageName);
                    if (thumbRes != 0) {
                        mThumbs.add(thumbRes);
                    }
                }
            }
        }/*

        private void addApps(Resources resources, int list) {
            final String[] extras = resources.getStringArray(list);
            for (String extra : extras) {
                mThemedApps.add(extra);
            }
        }*/
    }

}