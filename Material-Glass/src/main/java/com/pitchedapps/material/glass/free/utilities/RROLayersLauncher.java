package com.pitchedapps.material.glass.free.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class RROLayersLauncher {
    public RROLayersLauncher(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager", "com.lovejoy777.rroandlayersmanager.menu"));
        intent.putExtra("pkgName", "com.pitchedapps.material.glass.free/.Overlay");
        //original:         intent.putExtra("pkgName", context.getPackageName());
        // tried package name with ".Overlay" or "/.Overlay". All don't work
        //Failure retrieving resources for com.pitchedapps.material.glass.free: Resource ID #0x0
        //Not sure if this is related to my theme or the way the layers manager gets the drawable
        context.startActivity(intent);
    }
}