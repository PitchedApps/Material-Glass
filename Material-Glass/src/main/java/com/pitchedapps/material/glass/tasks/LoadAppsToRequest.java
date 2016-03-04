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

package com.pitchedapps.material.glass.tasks;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.fragments.RequestsFragment;
import com.pitchedapps.material.glass.models.RequestItem;
import com.pitchedapps.material.glass.utilities.ApplicationBase;
import com.pitchedapps.material.glass.utilities.ThemeUtils;
import com.pitchedapps.material.glass.utilities.Utils;
import com.pitchedapps.material.glass.utilities.color.ToolbarColorizer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LoadAppsToRequest extends AsyncTask<Void, String, ArrayList<RequestItem>> {

    private static PackageManager mPackageManager;
    private static ArrayList<String> components = new ArrayList<>();

    final static ArrayList<RequestItem> appsList = new ArrayList<>();

    private Context context;

    long startTime, endTime;

    @SuppressLint("PrivateResource")
    public LoadAppsToRequest(Context context) {
        startTime = System.currentTimeMillis();
        this.context = context;
        mPackageManager = context.getPackageManager();

        ArrayList<ResolveInfo> rAllActivitiesList =
                (ArrayList<ResolveInfo>) context.getPackageManager().queryIntentActivities(
                        getAllActivitiesIntent(), 0);

        for (ResolveInfo info : rAllActivitiesList) {

            if (info.activityInfo.packageName.equals(context.getApplicationContext().getPackageName())) {
                continue;
            }

            Drawable icon;
            try {
                icon = info.loadIcon(mPackageManager);
            } catch (Resources.NotFoundException e) {
                try {
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_na_launcher);
                    } catch (Resources.NotFoundException e1) {
                        icon = ThemeUtils.darkTheme ? ToolbarColorizer.getTintedIcon(
                            ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_on_mtrl),
                            ContextCompat.getColor(context, R.color.drawable_tint_dark))
                            : ToolbarColorizer.getTintedIcon(
                            ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_on_mtrl),
                            ContextCompat.getColor(context, R.color.drawable_tint_light));
                    }
            }

            RequestItem appInfo = new RequestItem(
                    info.loadLabel(mPackageManager).toString(),
                    info.activityInfo.packageName,
                    info.activityInfo.name,
                    icon);

            appsList.add(appInfo);
        }

        ApplicationBase.allApps = appsList;

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<RequestItem> doInBackground(Void... params) {

        final ArrayList<RequestItem> list = ApplicationBase.allApps;

        list.removeAll(createListFromXML(context));

        Collections.sort(list, new Comparator<RequestItem>() {
            @Override
            public int compare(RequestItem a, RequestItem b) {
                return a.getAppName().compareToIgnoreCase(b.getAppName());
            }
        });

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<RequestItem> list) {
        ApplicationBase.allAppsToRequest = list;
        RequestsFragment.setupContent();
        endTime = System.currentTimeMillis();
        if (components != null) {
            showDuplicatedComponentsInLog(components, context);
        }
        Utils.showLog(context, "Apps to Request Task completed in: " + String.valueOf((endTime - startTime) / 1000) + " secs.");
    }

    private static ResolveInfo getResolveInfo(String componentString, Context context,
                                              String iconName) {
        Intent intent = new Intent();

        // Example format:
        //intent.setComponent(new ComponentName("com.myapp", "com.myapp.launcher.settings"));

        if (componentString != null) {
            String[] split = null;
            try {
                split = componentString.split("/");
            } catch (ArrayIndexOutOfBoundsException e) {
                //Do nothing
            }
            if (split != null) {
                try {
                    components.add(componentString);
                    intent.setComponent(new ComponentName(split[0], split[1]));
                } catch (ArrayIndexOutOfBoundsException e1) {
                    //Do nothing
                }
            }
            return mPackageManager.resolveActivity(intent, 0);
        } else {
            return null;
        }
    }

    private static String gComponentString(XmlPullParser xmlParser, Context context) {

        boolean halfEmptyPack = false, halfEmptyComp = false;

        try {

            final String initialComponent = xmlParser.getAttributeValue(null, "component").split("/")[1];
            final String finalComponent = initialComponent.substring(0, initialComponent.length() - 1);
            final String initialComponentPackage = xmlParser.getAttributeValue(null, "component").split("/")[0];
            final String finalComponentPackage = initialComponentPackage.substring(14, initialComponentPackage.length());

            if (finalComponentPackage.equals("")) {
                halfEmptyPack = true;
            } else if (finalComponent.equals("")) {
                halfEmptyComp = true;
            }

            final String iconName = getIconName(xmlParser);

            String emptyComponent = finalComponentPackage + finalComponent;
            String completeComponent = finalComponentPackage + "/" + finalComponent;

            if (emptyComponent.equals("")) {
                Utils.showAppFilterLog(context, "Found empty ComponentInfo for icon: \'" + iconName + "\'");
            } else if (halfEmptyPack) {
                Utils.showAppFilterLog(context, "Found empty component package for icon: \'" + iconName + "\'");
                return null;
            } else if (halfEmptyComp) {
                Utils.showAppFilterLog(context, "Found empty component for icon: \'" + iconName + "\'");
                return null;
            } else if (iconName.equals("")) {
                Utils.showAppFilterLog(context, "Found empty drawable for component: \'" + completeComponent + "\'");
                return null;
            } else {
                int iconID = getIconResId(context, iconName);
                if (iconID == 0) {
                    Utils.showAppFilterLog(context, "Icon \'" + iconName + "\' is mentioned in appfilter.xml but could not be found in the app resources.");
                }
                return completeComponent;
            }

        } catch (Exception e) {
            //Do nothing
        }

        return null;

    }

    public static String getIconName(XmlPullParser xmlParser) {
        return xmlParser.getAttributeValue(null, "drawable");
    }

    public Intent getAllActivitiesIntent() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return mainIntent;
    }

    @SuppressLint("PrivateResource")
    private ArrayList<RequestItem> createListFromXML(Context context) {

        ArrayList<RequestItem> activitiesToRemove = new ArrayList<>();

        try {

            XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlParser = xmlFactory.newPullParser();
            InputStream inputStream = context.getResources().openRawResource(R.raw.appfilter);
            xmlParser.setInput(inputStream, null);

            int activity = xmlParser.getEventType();

            while (activity != XmlPullParser.END_DOCUMENT) {

                String name = xmlParser.getName();

                switch (activity) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:

                        if (name.equals("item")) {

                            ResolveInfo info = getResolveInfo(
                                    gComponentString(xmlParser, context),
                                    context,
                                    getIconName(xmlParser));

                            if (info != null) {
                                Drawable icon;
                                try {
                                    icon = info.loadIcon(mPackageManager);
                                } catch (Resources.NotFoundException e) {
                                    try {
                                            icon = ContextCompat.getDrawable(context, R.drawable.ic_na_launcher);
                                        } catch (Resources.NotFoundException e1) {
                                            icon = ThemeUtils.darkTheme ? ToolbarColorizer.getTintedIcon(
                                                ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_on_mtrl),
                                                ContextCompat.getColor(context, R.color.drawable_tint_dark))
                                                : ToolbarColorizer.getTintedIcon(
                                                ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_on_mtrl),
                                                ContextCompat.getColor(context, R.color.drawable_tint_light));
                                        }
                                }
                                RequestItem appInfo = new RequestItem(
                                        info.loadLabel(mPackageManager).toString(),
                                        info.activityInfo.packageName,
                                        info.activityInfo.name,
                                        icon);

                                activitiesToRemove.add(appInfo);
                            }
                        }

                        break;
                }

                activity = xmlParser.next();
            }

        } catch (IOException | XmlPullParserException e) {
            //Do nothing
        }

        return activitiesToRemove;
    }

    private static int getIconResId(Context context, String name) {
        Resources r = context.getResources();
        String p = context.getPackageName();
        int res = r.getIdentifier(name, "drawable", p);
        if (res != 0) {
            return res;
        } else {
            return 0;
        }
    }

    private static void showDuplicatedComponentsInLog(ArrayList<String> components,
                                                      Context context) {

        String[] componentsArray = new String[components.size()];
        componentsArray = components.toArray(componentsArray);

        Map<String, Integer> occurrences = new HashMap<>();

        Integer count = 0;

        for (String word : componentsArray) {
            count = occurrences.get(word);
            if (count == null) {
                count = 0;
            }
            occurrences.put(word, count + 1);
        }

        for (String word : occurrences.keySet()) {
            Utils.showAppFilterLog(context, "Duplicated component: \'" + word + "\' - " + String.valueOf(count) + " times.");
        }

    }

}