package com.pitchedapps.material.glass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.views.SlidingTabLayout;

public class Info extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_info, null);

        Context context = getActivity();

        ActionBar toolbar = ((ActionBarActivity) context).getSupportActionBar();
        toolbar.setTitle(R.string.section_eight);

        ViewPager mPager = (ViewPager) root.findViewById(R.id.info_pager);
        mPager.setAdapter(new InfoAdapter(getActivity().getSupportFragmentManager()));
        SlidingTabLayout mTabs = (SlidingTabLayout) root.findViewById(R.id.info_tabs);
        mTabs.setViewPager(mPager);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accent);
            }
        });

        return root;
    }

    class InfoAdapter extends FragmentPagerAdapter {

        String[] tabs;
        String[] content;

        public InfoAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.info_sections);
            content = getResources().getStringArray(R.array.info_contents);
        }

        @Override
        public Fragment getItem(int position) {
            return InfoFragment.newInstance(content[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
        //change if tabs change
    }

}
