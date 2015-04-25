package com.pitchedapps.material.glass.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pitchedapps.material.glass.R;

import java.util.ArrayList;


public class IconsFragment extends Fragment {

    private String[] iconsnames;

    public static IconsFragment newInstance(int iconsArray) {
        IconsFragment fragment = new IconsFragment();
        Bundle args = new Bundle();
        args.putInt("iconsArrayId", iconsArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.icons_grid, container, false);
        GridView gridview = (GridView) view.findViewById(R.id.icons_grid);
        final IconAdapter icAdapter = new IconAdapter();
        gridview.setAdapter(icAdapter);
        return view;
    }

    private class IconAdapter extends BaseAdapter {
        private ArrayList<Integer> mThumbs;

        public IconAdapter() {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            IconsHolder holder;
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.icon, parent, false);
                //added for support
                convertView.setLayoutParams(new GridView.LayoutParams(120, 120));
                holder = new IconsHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (IconsHolder) convertView.getTag();
            }

            holder.icon.startAnimation(anim);
            holder.icon.setImageResource(mThumbs.get(position));


            return convertView;
        }

        private void loadIcon() {
            mThumbs = new ArrayList<>();

            final Resources resources = getResources();
            final String packageName = getActivity().getApplication().getPackageName();
            addIcon(resources, packageName, getArguments().getInt("iconsArrayId", 0));

        }

        private void addIcon(Resources resources, String packageName, int list) {
            iconsnames = resources.getStringArray(list);
            for (String extra : iconsnames) {
                int res = resources.getIdentifier(extra, "drawable", packageName);
                if (res != 0) {
                    final int thumbRes = resources.getIdentifier(extra, "drawable", packageName);
                    if (thumbRes != 0)
                        mThumbs.add(thumbRes);
                }
            }
        }

        class IconsHolder {
            ImageView icon;

            IconsHolder(View v) {
                icon = (ImageView) v.findViewById(R.id.icon_img);
            }
        }
    }
//    }
}