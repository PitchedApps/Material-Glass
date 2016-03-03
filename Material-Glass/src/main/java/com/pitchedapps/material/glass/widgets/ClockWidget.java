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

package com.pitchedapps.material.glass.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.utilities.Utils;

import java.util.HashMap;

public class ClockWidget extends AppWidgetProvider {

    public HashMap<String, String> activityMap;
    PackageManager packageManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        packageManager = context.getPackageManager();
        boolean foundApp = false;

        setupHashMap();
        String action = intent.getAction();

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.clock_widget);

            Intent clockAppIntent = new Intent();
            clockAppIntent.setAction(Intent.ACTION_MAIN);
            clockAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            for (String packageName : activityMap.keySet()) {
                if (Utils.isAppInstalled(context, packageName)) {
                    ComponentName cn = new ComponentName(packageName, activityMap.get(packageName));
                    clockAppIntent.setComponent(cn);
                    clockAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    foundApp = true;
                    break;
                }
            }

            if (foundApp) {
                views.setOnClickPendingIntent(R.id.clockWidget,
                        PendingIntent.getActivity(context, 0, clockAppIntent, 0));
            }

            AppWidgetManager.getInstance(context)
                    .updateAppWidget(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);

        }
    }

    private void setupHashMap() {
        activityMap = new HashMap<>();
        activityMap.put("com.android.alarmclock", "com.android.alarmclock.AlarmClock");
        activityMap.put("com.android.deskclock", "com.android.deskclock.DeskClock");
        activityMap.put("com.google.android.deskclock", "com.google.android.deskclock.DeskClock");
        activityMap.put("com.google.android.deskclock", "com.android.deskclock.AlarmClock");
        activityMap.put("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        activityMap.put("com.sonyericsson.alarm", "com.sonyericsson.alarm.Alarm");
        activityMap.put("com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock");
        activityMap.put("com.asus.alarmclock", "com.asus.alarmclock.AlarmClock");
        activityMap.put("com.asus.deskclock", "com.asus.deskclock.DeskClock");
        activityMap.put("com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl");
        activityMap.put("com.motorola.blur.alarmclock", "com.motorola.blur.alarmclock.AlarmClock");
        activityMap.put("com.lge.clock", "com.lge.clock.AlarmClockActivity");
    }

}