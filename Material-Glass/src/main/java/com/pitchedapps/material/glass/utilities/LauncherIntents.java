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

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.pitchedapps.material.glass.R;

public class LauncherIntents {

    public LauncherIntents(Context context, String launcherName) {
        switch (launcherName) {
            case "Action":
                ActionLauncher(context);
                break;
            case "Adw":
                AdwLauncher(context);
                break;
            case "Adwex":
                AdwEXLauncher(context);
                break;
            case "Apex":
                ApexLauncher(context);
                break;
            case "Atom":
                AtomLauncher(context);
                break;
            case "Aviate":
                AviateLauncher(context);
                break;
            case "Cmthemeengine":
                CMThemeEngine(context);
                break;
            case "Go":
                GoLauncher(context);
                break;
            case "Holo":
                HoloLauncher(context);
                break;
            case "Holohd":
                HoloLauncherHD(context);
                break;
            case "KK":
                KkLauncher(context);
                break;
            case "Lghome":
                LgHomeLauncher(context);
                break;
            case "L":
                LLauncher(context);
                break;
            case "Lucid":
                LucidLauncher(context);
                break;
            case "Mini":
                MiniLauncher(context);
                break;
            case "Next":
                NextLauncher(context);
                break;
            case "Nova":
                NovaLauncher(context);
                break;
            case "S":
                SLauncher(context);
                break;
            case "Smart":
                SmartLauncher(context);
                break;
            case "Smartpro":
                SmartLauncherPro(context);
                break;
            case "Solo":
                SoloLauncher(context);
                break;
            case "Tsf":
                TsfLauncher(context);
                break;
            case "Uniconpro":
                Unicon(context);
                break;
            case "Layers":
                Layers(context);
                break;
            default:
                Utils.showLog(context, "No method for: " + launcherName);
                break;
        }
    }

    public void ActionLauncher(Context context) {
        Intent action = context.getPackageManager().getLaunchIntentForPackage("com.actionlauncher.playstore");
        action.putExtra("apply_icon_pack", context.getPackageName());
        context.startActivity(action);
    }

    public void AdwLauncher(Context context) {
        Intent intent = new Intent("org.adw.launcher.SET_THEME");
        intent.putExtra("org.adw.launcher.theme.NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void AdwEXLauncher(Context context) {
        Intent intent = new Intent("org.adwfreak.launcher.SET_THEME");
        intent.putExtra("org.adwfreak.launcher.theme.NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void ApexLauncher(Context context) {
        Intent intent = new Intent("com.anddoes.launcher.SET_THEME");
        intent.putExtra("com.anddoes.launcher.THEME_PACKAGE_NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void AtomLauncher(Context context) {
        Intent atom = new Intent("com.dlto.atom.launcher.intent.action.ACTION_VIEW_THEME_SETTINGS");
        atom.setPackage("com.dlto.atom.launcher");
        atom.putExtra("packageName", context.getPackageName());
        atom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(atom);
    }

    public void AviateLauncher(Context context) {
        Intent aviate = new Intent("com.tul.aviate.SET_THEME");
        aviate.setPackage("com.tul.aviate");
        aviate.putExtra("THEME_PACKAGE", context.getPackageName());
        aviate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(aviate);
    }

    public void CMThemeEngine(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("org.cyanogenmod.theme.chooser",
                "org.cyanogenmod.theme.chooser.ChooserActivity"));
        intent.putExtra("pkgName", context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setComponent(new ComponentName("com.cyngn.theme.chooser",
                    "com.cyngn.theme.chooser.ChooserActivity"));
            intent.putExtra("pkgName", context.getPackageName());
            context.startActivity(intent);
        }
    }

    public void GoLauncher(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.gau.go.launcherex");
        Intent go = new Intent("com.gau.go.launcherex.MyThemes.mythemeaction");
        go.putExtra("type", 1);
        go.putExtra("pkgname", context.getPackageName());
        context.sendBroadcast(go);
        context.startActivity(intent);
    }

    public void HoloLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.mobint.hololauncher", "com.mobint.hololauncher.SettingsActivity"));
        context.startActivity(intent);
    }

    public void HoloLauncherHD(Context context) {
        Intent holohdApply = new Intent(Intent.ACTION_MAIN);
        holohdApply.setComponent(new ComponentName("com.mobint.hololauncher.hd", "com.mobint.hololauncher.SettingsActivity"));
        context.startActivity(holohdApply);
    }

    public void KkLauncher(Context context) {
        Intent kkApply = new Intent("com.kk.launcher.APPLY_ICON_THEME");
        kkApply.putExtra("com.kk.launcher.theme.EXTRA_PKG", context.getPackageName());
        kkApply.putExtra("com.kk.launcher.theme.EXTRA_NAME", context.getResources().getString(R.string.app_name));
        context.startActivity(kkApply);
    }

    public void LgHomeLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.lge.launcher2", "com.lge.launcher2.homesettings.HomeSettingsPrefActivity"));
        context.startActivity(intent);
    }

    public void LLauncher(Context context) {
        Intent l = new Intent("com.l.launcher.APPLY_ICON_THEME", null);
        l.putExtra("com.l.launcher.theme.EXTRA_PKG", context.getPackageName());
        context.startActivity(l);
    }

    public void LucidLauncher(Context context) {
        Intent lucidApply = new Intent("com.powerpoint45.action.APPLY_THEME", null);
        lucidApply.putExtra("icontheme", context.getPackageName());
        context.startActivity(lucidApply);
    }

    public void MiniLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.jiubang.go.mini.launcher", "com.jiubang.go.mini.launcher.setting.MiniLauncherSettingActivity"));
        context.startActivity(intent);
    }

    public void NextLauncher(Context context) {
        Intent nextApply = context.getPackageManager().getLaunchIntentForPackage("com.gtp.nextlauncher");
        if (nextApply == null) {
            nextApply = context.getPackageManager().getLaunchIntentForPackage("com.gtp.nextlauncher.trial");
        }
        Intent next = new Intent("com.gau.go.launcherex.MyThemes.mythemeaction");
        next.putExtra("type", 1);
        next.putExtra("pkgname", context.getPackageName());
        context.sendBroadcast(next);
        context.startActivity(nextApply);
    }

    public void NovaLauncher(Context context) {
        Intent intent = new Intent("com.teslacoilsw.launcher.APPLY_ICON_THEME");
        intent.setPackage("com.teslacoilsw.launcher");
        intent.putExtra("com.teslacoilsw.launcher.extra.ICON_THEME_TYPE", "GO");
        intent.putExtra("com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void SLauncher(Context context) {
        Intent s = new Intent("com.s.launcher.APPLY_ICON_THEME");
        s.putExtra("com.s.launcher.theme.EXTRA_PKG", context.getPackageName());
        s.putExtra("com.s.launcher.theme.EXTRA_NAME", context.getResources().getString(R.string.app_name));
        context.startActivity(s);

    }

    public void SmartLauncher(Context context) {
        Intent smartlauncherIntent = new Intent("ginlemon.smartlauncher.setGSLTHEME");
        smartlauncherIntent.putExtra("package", context.getPackageName());
        context.startActivity(smartlauncherIntent);
    }

    public void SmartLauncherPro(Context context) {
        Intent smartlauncherproIntent = new Intent("ginlemon.smartlauncher.setGSLTHEME");
        smartlauncherproIntent.putExtra("package", context.getPackageName());
        context.startActivity(smartlauncherproIntent);
    }

    public void SoloLauncher(Context context) {
        Intent soloApply = context.getPackageManager().getLaunchIntentForPackage("home.solo.launcher.free");
        Intent solo = new Intent("home.solo.launcher.free.APPLY_THEME");
        solo.putExtra("EXTRA_PACKAGENAME", context.getPackageName());
        solo.putExtra("EXTRA_THEMENAME", context.getString(R.string.app_name));
        context.sendBroadcast(solo);
        context.startActivity(soloApply);
    }

    public void TsfLauncher(Context context) {
        Intent tsfApply = context.getPackageManager().getLaunchIntentForPackage("com.tsf.shell");
        Intent tsf = new Intent("android.intent.action.MAIN");
        tsf.setComponent(new ComponentName("com.tsf.shell", "com.tsf.shell.ShellActivity"));
        context.sendBroadcast(tsf);
        context.startActivity(tsfApply);
    }

    public void Unicon(Context context) {
        Intent unicon = new Intent("android.intent.action.MAIN");
        unicon.addCategory("android.intent.category.LAUNCHER");
        unicon.setPackage("sg.ruqqq.IconThemer");
        context.startActivity(unicon);
    }

    //for theme support
    public void Layers(Context context) {
        Intent layers = new Intent("android.intent.action.MAIN");
        layers.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager",
                "com.lovejoy777.rroandlayersmanager.MainActivity"));
        layers.putExtra("pkgName", context.getPackageName());
        context.startActivity(layers);
    }

}
