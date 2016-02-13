package com.pitchedapps.material.glass.free.utilities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pitchedapps.material.glass.free.R;

public class ThemeEngineLauncher {
    public ThemeEngineLauncher(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("org.cyanogenmod.theme.chooser", "org.cyanogenmod.theme.chooser.ChooserActivity"));
        intent.putExtra("pkgName", context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager",
                    "com.lovejoy777.rroandlayersmanager.MainActivity"));
            intent.putExtra("pkgName", context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                intent.setComponent(new ComponentName("com.cyngn.theme.chooser",
                        "com.cyngn.theme.chooser.ChooserActivity"));
                intent.putExtra("pkgName", context.getPackageName());
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e3) {
                    new MaterialDialog.Builder(context)
                            .title(R.string.NTED_title)
                            .content(R.string.NTED_message)
                            .positiveText(R.string.understood)
                            .show();
                }
            }
        }
    }
}