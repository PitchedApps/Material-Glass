package com.pitchedapps.material.glass.iconfragments;

import android.content.Context;
import android.content.res.Resources;
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


public class GamesIcons extends Fragment {

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

            iconSource = mThumbs.get(position);

            icon = (FrameLayout) iconView.findViewById(R.id.icon);
            iconImg = (ImageView) iconView.findViewById(R.id.icon_img);

            if (convertView == null) {
                icon.setLayoutParams(new GridView.LayoutParams(120, 120));
            } else {
                icon = (FrameLayout) convertView;
            }

            iconImg.setImageResource(iconSource);
            return icon;
        }

        private void loadIcon() {
            mThumbs = new ArrayList<Integer>();

            final Resources resources = getResources();
            final String packageName = getActivity().getApplication().getPackageName();
            addIcon(resources, packageName, R.array.games);

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
        }

    }


}