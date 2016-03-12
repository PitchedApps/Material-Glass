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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.adapters.RequestsAdapter;
import com.pitchedapps.material.glass.dialogs.ISDialogs;
import com.pitchedapps.material.glass.tasks.ZipFilesToRequest;
import com.pitchedapps.material.glass.utilities.ApplicationBase;
import com.pitchedapps.material.glass.utilities.PermissionUtils;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.Utils;
import com.pitchedapps.material.glass.views.GridSpacingItemDecoration;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestsFragment extends Fragment implements PermissionUtils.OnPermissionResultListener {

    public static ProgressBar progressBar;
    public static RecyclerView mRecyclerView;
    public static RecyclerFastScroller fastScroller;
    public static RequestsAdapter requestsAdapter;
    private static FloatingActionButton fab;
    private static int maxApps = 0, hoursLimit = 0;

    static int columnsNumber, gridSpacing;
    static boolean withBorders;
    public static ViewGroup layout;

    private Preferences mPrefs;
    private static Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gridSpacing = getResources().getDimensionPixelSize(R.dimen.lists_padding);
        columnsNumber = getResources().getInteger(R.integer.requests_grid_width);
        withBorders = true;

        maxApps = getResources().getInteger(R.integer.max_apps_to_request);
        hoursLimit = getResources().getInteger(R.integer.limit_request_to_x_hours);

        setHasOptionsMenu(true);
        context = getActivity();

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        try {
            layout = (ViewGroup) inflater.inflate(R.layout.icon_request_section, container, false);
        } catch (InflateException e) {
            // Do nothing
        }

        mPrefs = new Preferences(getActivity());

        fab = (FloatingActionButton) layout.findViewById(R.id.requests_fab);

        if (ApplicationBase.allAppsToRequest == null || ApplicationBase.allAppsToRequest.size() <= 0) {
            fab.hide();
        } else {
            fab.show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.canAccessStorage(getContext())) {
                    PermissionUtils.requestStoragePermission(getActivity(), RequestsFragment.this);
                } else {
                    if (maxApps < 0) {
                        maxApps = 0;
                    }
                    startRequestProcess();
                }
            }
        });

        progressBar = (ProgressBar) layout.findViewById(R.id.requestProgress);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.appsToRequestList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, columnsNumber));
        mRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(columnsNumber,
                        gridSpacing,
                        true));
        fastScroller = (RecyclerFastScroller) layout.findViewById(R.id.rvFastScroller);
        fastScroller.attachRecyclerView(mRecyclerView);
        hideStuff();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupContent();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.requests, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ShowcaseActivity.fab.hide();
        ShowcaseActivity.fab.setVisibility(View.GONE);
        Utils.collapseToolbar(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestsAdapter adapter = ((RequestsAdapter) mRecyclerView.getAdapter());
        if (adapter != null) {
            adapter.stopAppIconFetching();
        }
    }

    public static void setupContent() {
        if (layout != null) {
            if (ApplicationBase.allAppsToRequest != null && ApplicationBase.allAppsToRequest.size() > 0) {
                requestsAdapter = new RequestsAdapter(context, ApplicationBase.allAppsToRequest, maxApps);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(requestsAdapter);
                requestsAdapter.startIconFetching(mRecyclerView);
                showStuff();
            }
        }
    }

    private static void showStuff() {
        if (progressBar.getVisibility() != View.GONE) {
            progressBar.setVisibility(View.GONE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        fastScroller.setVisibility(View.VISIBLE);
        fab.show();
    }

    private void hideStuff() {
        if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.GONE);
        fastScroller.setVisibility(View.GONE);
    }

    public static void showRequestsFilesCreationDialog(Context context) {

        if (requestsAdapter.getSelectedApps() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {

                ISDialogs.showPermissionNotGrantedDialog(context);

            } else {
                final MaterialDialog dialog = ISDialogs.showBuildingRequestDialog(context);
                dialog.show();

                new ZipFilesToRequest((Activity) context, dialog,
                        ((RequestsAdapter) mRecyclerView.getAdapter()).appsList).execute();
            }
        } else {
            ISDialogs.showNoSelectedAppsDialog(context);
        }

    }

    @Override
    public void onStoragePermissionGranted() {
        showRequestsFilesCreationDialog(context);
    }

    public boolean haveHappenedXHoursSinceLastRequest(Context context, int numOfHours) {

        float hoursToDays = numOfHours / 24.0f;
        int hoursToMins = numOfHours * 60;

        boolean hasHappenedTheTime = false;

        Calendar c = Calendar.getInstance();

        String time = "";
        int dayNum = 0;

        if (!mPrefs.getRequestsCreated()) {
            time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", c.get(Calendar.MINUTE));
            String day = String.format("%02d", c.get(Calendar.DAY_OF_YEAR));
            mPrefs.setRequestHour(time);
            mPrefs.setRequestDay(Integer.valueOf(day));
            mPrefs.setRequestsCreated(true);
            hasHappenedTheTime = true;
        } else {
            time = mPrefs.getRequestHour();
            dayNum = mPrefs.getRequestDay();

            String currentTime = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", c.get(Calendar.MINUTE));
            String currentDay = String.format("%02d", c.get(Calendar.DAY_OF_YEAR));

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = null;
            try {
                startDate = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date endDate = null;
            try {
                endDate = simpleDateFormat.parse(currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long difference = endDate.getTime() - startDate.getTime();
            if (difference < 0) {
                Date dateMax = null;
                try {
                    dateMax = simpleDateFormat.parse("24:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date dateMin = null;
                try {
                    dateMin = simpleDateFormat.parse("00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) Integer.valueOf(currentDay) - dayNum;
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            if (days >= hoursToDays) {
                hasHappenedTheTime = true;
            } else if (hours >= numOfHours) {
                hasHappenedTheTime = true;
            } else if (min >= hoursToMins) {
                hasHappenedTheTime = true;
            }

        }

        return hasHappenedTheTime;

    }

    private void startRequestProcess() {
        if (maxApps > 0) {
            if (haveHappenedXHoursSinceLastRequest(context, hoursLimit)) {
                showRequestsFilesCreationDialog(context);
            } else {
                ISDialogs.showRequestLimitDayDialog(context, hoursLimit);
            }
        } else {
            showRequestsFilesCreationDialog(context);
        }
    }

}