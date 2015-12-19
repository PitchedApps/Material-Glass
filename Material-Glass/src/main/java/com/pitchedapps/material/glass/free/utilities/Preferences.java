package com.pitchedapps.material.glass.free.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String
            PREFERENCES_NAME = "DASHBOARD_PREFERENCES",
            ROTATE_MINUTE = "rotate_time_minute",
            ROTATE_TIME = "muzei_rotate_time";

    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isRotateMinute() {
        return getSharedPreferences().getBoolean(ROTATE_MINUTE, false);
    }

    public void setRotateMinute(boolean bool) {
        getSharedPreferences().edit().putBoolean(ROTATE_MINUTE, bool).apply();
    }

    public int getRotateTime() {
        return getSharedPreferences().getInt(ROTATE_TIME, 900000);
    }

    public void setRotateTime(int time) {
        getSharedPreferences().edit().putInt(ROTATE_TIME, time).apply();
    }
}