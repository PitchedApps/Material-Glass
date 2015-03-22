package com.pitchedapps.material.glass.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String
            PREFERENCES_NAME = "DASHBOARD_PREFERENCES",
            ENABLE_FEATURES = "enable_features",
            FIRSTRUN = "firstrun";
    private Context context;

    public Preferences(Context context) {
        this.context = context;

    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return getSharedPreferences().getBoolean(FIRSTRUN, true);
    }

    public boolean isFeaturesEnabled() {
        return getSharedPreferences().getBoolean(ENABLE_FEATURES, true);
    }

    public void setFeaturesEnabled(boolean bool) {
        getSharedPreferences().edit().putBoolean(ENABLE_FEATURES, bool).apply();
    }

    public void setNotFirstrun() {
        getSharedPreferences().edit().putBoolean(FIRSTRUN, false).apply();
    }

}
