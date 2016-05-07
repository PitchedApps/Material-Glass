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

package jahirfiquitiva.iconshowcase.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jahirfiquitiva.iconshowcase.R;
import jahirfiquitiva.iconshowcase.activities.ShowcaseActivity;
import jahirfiquitiva.iconshowcase.activities.ViewerActivity;
import jahirfiquitiva.iconshowcase.adapters.WallpapersAdapter;
import jahirfiquitiva.iconshowcase.models.WallpaperItem;
import jahirfiquitiva.iconshowcase.models.WallpapersList;
import jahirfiquitiva.iconshowcase.tasks.ApplyWallpaper;
import jahirfiquitiva.iconshowcase.utilities.JSONParser;
import jahirfiquitiva.iconshowcase.utilities.Preferences;
import jahirfiquitiva.iconshowcase.utilities.ThemeUtils;
import jahirfiquitiva.iconshowcase.utilities.Utils;
import jahirfiquitiva.iconshowcase.views.GridSpacingItemDecoration;

public class WallpapersFragment extends Fragment {

    private static ViewGroup layout;
    private static ProgressBar mProgress;
    private static RecyclerView mRecyclerView;
    private static RecyclerFastScroller fastScroller;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static WallpapersAdapter mAdapter;
    public static ImageView noConnection;
    private static Activity context;
    private static GridSpacingItemDecoration gridSpacing;

    private static boolean worked;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        context = getActivity();

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        try {
            layout = (ViewGroup) inflater.inflate(R.layout.wallpapers_section, container, false);
        } catch (InflateException e) {
            // Do nothing
        }

        int light = ContextCompat.getColor(context, R.color.drawable_tint_dark);
        int dark = ContextCompat.getColor(context, R.color.drawable_tint_light);

        noConnection = (ImageView) layout.findViewById(R.id.no_connected_icon);
        mProgress = (ProgressBar) layout.findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.wallsGrid);
        fastScroller = (RecyclerFastScroller) layout.findViewById(R.id.rvFastScroller);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);

        if (!ShowcaseActivity.wallsPicker) {
            showWallsAdviceDialog(getActivity());
        }

        noConnection.setImageDrawable(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_cloud_off)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(144));
        noConnection.setVisibility(View.GONE);

        showProgressBar();

        setupRecyclerView(false, 0);

        mRecyclerView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ThemeUtils.darkTheme ? dark : light);

        mSwipeRefreshLayout.setColorSchemeResources(
                ThemeUtils.darkTheme ? R.color.dark_theme_accent : R.color.light_theme_accent,
                ThemeUtils.darkTheme ? R.color.dark_theme_accent : R.color.light_theme_accent,
                ThemeUtils.darkTheme ? R.color.dark_theme_accent : R.color.light_theme_accent);

        mSwipeRefreshLayout.setEnabled(false);

        setupLayout(false, getActivity(), noConnection);

        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.collapseToolbar(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wallpapers, menu);
    }

    private static void setupLayout(final boolean fromTask, final Activity context,
                                    final ImageView noConnection) {

        if (WallpapersList.getWallpapersList() != null && WallpapersList.getWallpapersList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new WallpapersAdapter(context,
                            new WallpapersAdapter.ClickListener() {
                                @Override
                                public void onClick(WallpapersAdapter.WallsHolder view,
                                                    int position, boolean longClick) {
                                    if ((longClick && !ShowcaseActivity.wallsPicker) || ShowcaseActivity.wallsPicker) {
                                        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                                                .content(R.string.downloading_wallpaper)
                                                .progress(true, 0)
                                                .cancelable(false)
                                                .show();

                                        WallpaperItem wallItem = WallpapersList.getWallpapersList().get(position);

                                        Glide.with(context)
                                                .load(wallItem.getWallURL())
                                                .asBitmap()
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        if (resource != null) {
                                                            new ApplyWallpaper(
                                                                    context,
                                                                    dialog, resource,
                                                                    ShowcaseActivity.wallsPicker,
                                                                    layout).execute();
                                                        }
                                                    }
                                                });
                                    } else {
                                        final Intent intent = new Intent(context, ViewerActivity.class);

                                        intent.putExtra("item", WallpapersList.getWallpapersList().get(position));
                                        intent.putExtra("transitionName", ViewCompat.getTransitionName(view.wall));

                                        Bitmap bitmap;

                                        if (view.wall.getDrawable() != null) {
                                            bitmap = Utils.drawableToBitmap(view.wall.getDrawable());

                                            try {
                                                String filename = "temp.png";
                                                FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                stream.close();
                                                intent.putExtra("image", filename);
                                            } catch (Exception e) {
                                                Utils.showLog(context, "Error getting drawable " + e.getLocalizedMessage());
                                            }

                                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view.wall, ViewCompat.getTransitionName(view.wall));
                                            context.startActivity(intent, options.toBundle());
                                        } else {
                                            context.startActivity(intent);
                                        }
                                    }
                                }
                            });

                    mAdapter.setData(WallpapersList.getWallpapersList());

                    mRecyclerView.setAdapter(mAdapter);

                    fastScroller = (RecyclerFastScroller) layout.findViewById(R.id.rvFastScroller);

                    fastScroller.attachRecyclerView(mRecyclerView);

                    if (fastScroller.getVisibility() != View.VISIBLE) {
                        fastScroller.setVisibility(View.VISIBLE);
                    }

                    if (Utils.hasNetwork(context)) {
                        hideProgressBar();
                        noConnection.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        fastScroller.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setEnabled(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        hideStuff(noConnection);
                    }
                }
            });
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (layout != null) {
                        noConnection.setVisibility(View.GONE);
                        showProgressBar();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideStuff(noConnection);
                                    }
                                });
                            }
                        }, 7500);
                    }
                }
            });
        }
    }

    private static void hideStuff(ImageView noConnection) {
        if (mRecyclerView.getAdapter() != null) {
            fastScroller = (RecyclerFastScroller) layout.findViewById(R.id.rvFastScroller);
            fastScroller.attachRecyclerView(mRecyclerView);
        }
        hideProgressBar();
        if (noConnection != null) {
            noConnection.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.GONE);
        fastScroller.setVisibility(View.GONE);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private static void showProgressBar() {
        if (mProgress != null) {
            if (mProgress.getVisibility() != View.VISIBLE) {
                mProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    private static void hideProgressBar() {
        if (mProgress != null) {
            if (mProgress.getVisibility() != View.GONE) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    private static void setupRecyclerView(boolean updating, int newColumns) {

        Preferences mPrefs = new Preferences(context);
        if (updating && gridSpacing != null) {
            mPrefs.setWallsColumnsNumber(newColumns);
            mRecyclerView.removeItemDecoration(gridSpacing);
        }

        int columnsNumber = mPrefs.getWallsColumnsNumber();
        if (context.getResources().getConfiguration().orientation == 2) {
            columnsNumber += 2;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(context,
                columnsNumber));
        gridSpacing = new GridSpacingItemDecoration(columnsNumber,
                context.getResources().getDimensionPixelSize(R.dimen.lists_padding),
                true);
        mRecyclerView.addItemDecoration(gridSpacing);
        mRecyclerView.setHasFixedSize(true);

        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        if (mRecyclerView.getAdapter() != null) {
            fastScroller.attachRecyclerView(mRecyclerView);
            if (fastScroller.getVisibility() != View.VISIBLE) {
                fastScroller.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void updateRecyclerView(int newColumns) {
        mRecyclerView.setVisibility(View.GONE);
        fastScroller.setVisibility(View.GONE);
        showProgressBar();
        setupRecyclerView(true, newColumns);
        hideProgressBar();
    }

    public static void refreshWalls(Activity context) {
        hideProgressBar();
        mRecyclerView.setVisibility(View.GONE);
        fastScroller.setVisibility(View.GONE);
        if (Utils.hasNetwork(context)) {
            Utils.showSimpleSnackbar(context, layout,
                    context.getResources().getString(R.string.refreshing_walls));
        } else {
            Utils.showSimpleSnackbar(context, layout,
                    context.getResources().getString(R.string.no_conn_title));
        }
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    // DownloadJSON AsyncTask
    public static class DownloadJSON extends AsyncTask<Void, Void, Boolean> {

        final ShowcaseActivity.WallsListInterface wi;
        private final ImageView noConnection;
        private final ArrayList<WallpaperItem> walls = new ArrayList<>();
        private WeakReference<Context> taskContext;
        private WeakReference<Activity> wrActivity;

        long startTime, endTime;

        public DownloadJSON(ShowcaseActivity.WallsListInterface wi, Context context,
                            ImageView noConnection) {
            this.wi = wi;
            this.taskContext = new WeakReference<>(context);
            this.noConnection = noConnection;
        }

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();

            if (wrActivity != null) {
                final Activity a = wrActivity.get();
                if (a != null) {
                    this.taskContext = new WeakReference<>(a.getApplicationContext());
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean worked = false;

            JSONObject json = JSONParser.getJSONFromURL(taskContext.get(),
                    Utils.getStringFromResources(taskContext.get(),
                            R.string.json_file_url));

            if (json != null) {
                try {
                    // Locate the array name in JSON
                    JSONArray jsonarray = json.getJSONArray("wallpapers");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        json = jsonarray.getJSONObject(i);
                        // Retrieve JSON Objects

                        String dimens, copyright;

                        try {
                            dimens = json.getString("dimensions");
                        } catch (JSONException e) {
                            dimens = "null";
                        }

                        try {
                            copyright = json.getString("copyright");
                        } catch (JSONException e1) {
                            copyright = "null";
                        }

                        walls.add(new WallpaperItem(
                                json.getString("name"),
                                json.getString("author"),
                                json.getString("url"),
                                dimens,
                                copyright));

                    }

                    WallpapersList.createWallpapersList(walls);

                    worked = true;
                } catch (JSONException e) {
                    worked = false;
                }
            } else {
                worked = false;
            }

            return worked;
        }

        @Override
        protected void onPostExecute(Boolean worked) {

            endTime = System.currentTimeMillis();
            Utils.showLog("Walls Task completed in: " +
                    String.valueOf((endTime - startTime) / 1000) + " secs.");

            if (layout != null) {
                setupLayout(true, (Activity) taskContext.get(), noConnection);
            }

            if (wi != null)
                wi.checkWallsListCreation(worked);
        }
    }

    private void showWallsAdviceDialog(Context context) {
        final Preferences mPrefs = new Preferences(context);
        if (!mPrefs.getWallsDialogDismissed()) {
            new MaterialDialog.Builder(context)
                    .title(R.string.advice)
                    .content(R.string.walls_advice)
                    .positiveText(R.string.close)
                    .neutralText(R.string.dontshow)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPrefs.setWallsDialogDismissed(false);
                        }
                    })
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPrefs.setWallsDialogDismissed(true);
                        }
                    })
                    .show();
        }
    }

    private static void showLoadPictureSnackbar(View layout, Context context) {
        Utils.showSimpleSnackbar(context, layout,
                Utils.getStringFromResources(context, R.string.wait_for_walls));
    }
}