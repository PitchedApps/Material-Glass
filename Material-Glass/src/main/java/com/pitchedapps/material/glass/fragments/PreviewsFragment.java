/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package com.pitchedapps.material.glass.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mikepenz.materialize.util.UIUtils;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.fragments.base.FragmentStatePagerAdapter;
import com.pitchedapps.material.glass.models.IconsCategory;
import com.pitchedapps.material.glass.tasks.LoadIconsLists;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.color.ToolbarColorizer;
import com.pitchedapps.material.glass.views.CustomCoordinatorLayout;

import java.util.ArrayList;
import java.util.Locale;

public class PreviewsFragment extends Fragment {

    private int mLastSelected = 0;
    private ViewPager mPager;
    private String[] tabs;
    private ViewGroup layout;
    public TabLayout mTabs;
    private SearchView mSearchView;
    public ArrayList<IconsCategory> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        try {
            layout = (ViewGroup) inflater.inflate(R.layout.icons_preview_section, container, false);
        } catch (InflateException e) {
            //Do nothing
        }

        categories = LoadIconsLists.getIconsCategories();
        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        setupToolbar();

        if (mPager == null) {
            mPager = (ViewPager) layout.findViewById(R.id.pager);
            mPager.setOffscreenPageLimit(6);
            mPager.setAdapter(new IconsPagerAdapter(getChildFragmentManager()));
            createTabs();
        }
    }

    private void setupToolbar() {
        // Are you ready for the ugliest fix in the history of the universe?
        // Set custom offset for AppBar. This makes both toolbar and tabs visible

        AppBarLayout appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        CustomCoordinatorLayout.LayoutParams params = (CustomCoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) getActivity().findViewById(R.id.mainCoordinatorLayout);

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        Integer toolbarCollapsedHeight = 0;
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            toolbarCollapsedHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        Integer toolbarExpandedHeight = getActivity().getResources().getDimensionPixelOffset(R.dimen.toolbar_expanded);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float extra = (metrics.densityDpi * 0.05f);

        Integer statusbarHeight = UIUtils.getStatusBarHeight(getActivity());

        // Set toolbarCollapsedHeight as offset so tabs are shown
        if (behavior != null) {
            behavior.setTopAndBottomOffset(-toolbarExpandedHeight + statusbarHeight + (toolbarCollapsedHeight * 2) - Math.round(extra));
        }

        TextView title = (TextView) getActivity().findViewById(R.id.title);
        if (title != null) {
            title.setVisibility(View.VISIBLE);
        }

        // Lock CoordinatorLayout so the toolbar can't be scrolled away
        coordinatorLayout.setScrollAllowed(false);
    }

    private void createTabs() {
        mTabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        mTabs.setVisibility(View.VISIBLE);
        mTabs.setupWithViewPager(mPager);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                if (mLastSelected > -1) {
                    IconsFragment frag = (IconsFragment) getChildFragmentManager().findFragmentByTag("page:" + mLastSelected);
                    if (frag != null)
                        frag.performSearch(null);
                }
                mLastSelected = tab.getPosition();
                if (mSearchView != null && getActivity() != null)
                    mSearchView.setQueryHint(getString(R.string.search_x, tabs[mLastSelected]));
                if (getActivity() != null)
                    getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TextView title = (TextView) getActivity().findViewById(R.id.title);
        title.setVisibility(View.GONE);
        if (title != null) title.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        MenuItem mSearchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.setQueryHint(getString(R.string.search_x, tabs[mLastSelected]));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }

            private void search(String s) {
                IconsFragment frag = (IconsFragment) getChildFragmentManager().findFragmentByTag("page:" + mPager.getCurrentItem());
                if (frag != null)
                    frag.performSearch(s);
            }
        });

        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(getActivity(), R.color.toolbar_text_dark) :
                ContextCompat.getColor(getActivity(), R.color.toolbar_text_light);

        if (getActivity() != null) {
            ToolbarColorizer.tintSearchView(getActivity(), ShowcaseActivity.toolbar, mSearchItem,
                    mSearchView, iconsColor);
        }
    }

    class IconsPagerAdapter extends FragmentStatePagerAdapter {

        public IconsPagerAdapter(FragmentManager fm) {
            super(fm);
            String[] tabsNames = new String[categories.size()];
            for (int i = 0; i < tabsNames.length; i++) {
                tabsNames[i] = categories.get(i).getCategoryName();
            }
            tabs = tabsNames;
        }

        @Override
        public Fragment getItem(int position) {
            return IconsFragment.newInstance(categories.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position].toUpperCase(Locale.getDefault());
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save current position
        savedInstanceState.putInt("lastSelected", mLastSelected);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last selected position
            mLastSelected = savedInstanceState.getInt("lastSelected", 0);
        }
    }
}