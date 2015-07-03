package com.pitchedapps.material.glass.free.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class RROLayersLauncher {
    public RROLayersLauncher(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager", "com.lovejoy777.rroandlayersmanager.menu"));
        intent.putExtra("pkgName", "com.pitchedapps.material.glass.free.Overlay");
        context.startActivity(intent);
    }
}