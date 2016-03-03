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

package com.pitchedapps.material.glass.utilities;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.fragments.WallpapersFragment;
import com.pitchedapps.material.glass.models.RequestItem;
import com.pitchedapps.material.glass.models.WallpapersList;
import com.pitchedapps.material.glass.tasks.LoadAppsToRequest;
import com.pitchedapps.material.glass.tasks.LoadIconsLists;
import com.pitchedapps.material.glass.tasks.LoadZooperWidgets;

import java.util.ArrayList;

public class ApplicationBase extends Application {

    private Context context;
    private Preferences mPrefs;

    // Main list off all apps.
    public static ArrayList<RequestItem> allApps;

    // Main list off all apps to request.
    public static ArrayList<RequestItem> allAppsToRequest;

    public static int wallpaper;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = getApplicationContext();

        mPrefs = new Preferences(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new LoadIconsLists(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                loadWallsList();
                new LoadZooperWidgets(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                loadAppsForRequest();
            }
        }).start();

    }

    public Context getAppContext() {
        return this.context;
    }

    private void loadAppsForRequest() {
        if (mPrefs.getAppsToRequestLoaded()) {
            mPrefs.setAppsToRequestLoaded(!mPrefs.getAppsToRequestLoaded());
        }
        new LoadAppsToRequest(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void loadWallsList() {
        if (mPrefs.getWallsListLoaded()) {
            WallpapersList.clearList();
            mPrefs.setWallsListLoaded(!mPrefs.getWallsListLoaded());
        }
        WallpapersFragment.DownloadJSON downloadJSON = new WallpapersFragment.DownloadJSON(new ShowcaseActivity.WallsListInterface() {
            @Override
            public void checkWallsListCreation(boolean result) {
                mPrefs.setWallsListLoaded(result);
                if (WallpapersFragment.mSwipeRefreshLayout != null) {
                    WallpapersFragment.mSwipeRefreshLayout.setEnabled(false);
                    WallpapersFragment.mSwipeRefreshLayout.setRefreshing(false);
                }
                if (WallpapersFragment.mAdapter != null) {
                    WallpapersFragment.mAdapter.notifyDataSetChanged();
                }
            }
        }, context);
        downloadJSON.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}