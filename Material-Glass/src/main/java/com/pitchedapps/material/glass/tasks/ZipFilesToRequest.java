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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.models.RequestItem;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.Utils;

public class ZipFilesToRequest extends AsyncTask<Void, String, Boolean> {

    private MaterialDialog dialog;
    public ArrayList<RequestItem> appsListFinal = new ArrayList<>();

    private static final int BUFFER = 2048;
    private String zipLocation, zipFilePath;
    private Context context;

    public static String filesLocation;

    public static ArrayList<String> appsNames = new ArrayList<>();
    public static ArrayList<String> appsPackages = new ArrayList<>();
    public static ArrayList<String> appsClasses = new ArrayList<>();
    public static ArrayList<Drawable> appsIcons = new ArrayList<>();

    private StringBuilder emailContent = new StringBuilder();

    private WeakReference<Activity> wrActivity;

    private Activity activity;

    public ZipFilesToRequest(Activity activity, MaterialDialog dialog, ArrayList<RequestItem> appsListFinal) {
        this.wrActivity = new WeakReference<>(activity);
        this.dialog = dialog;
        this.appsListFinal = appsListFinal;
    }

    private SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.getDefault());

    @Override
    protected void onPreExecute() {

        final Activity act = wrActivity.get();
        if (act != null) {
            this.context = act.getApplicationContext();
            this.activity = act;
        }

        zipLocation = context.getString(R.string.request_save_location,
                Environment.getExternalStorageDirectory().getAbsolutePath());
        filesLocation = zipLocation + "Files/";

        String appNameCorrected = context.getResources().getString(R.string.app_name).replace(" ", "");

        zipFilePath = zipLocation + appNameCorrected
                + "_" + date.format(new Date()) + ".zip";

        appsNames.clear();
        appsPackages.clear();
        appsClasses.clear();
        appsIcons.clear();

        for (int i = 0; i < appsListFinal.size(); i++) {
            RequestItem icon = appsListFinal.get(i);
            if (icon.isSelected()) {
                appsNames.add(icon.getAppName());
                appsPackages.add(icon.getPackageName());
                appsClasses.add(icon.getClassName());
                appsIcons.add(icon.getIcon());
            }
        }

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean worked;
        try {
            final File zipFolder = new File(zipLocation);
            final File filesFolder = new File(filesLocation + "/");

            deleteDirectory(zipFolder);
            deleteDirectory(filesFolder);

            zipFolder.mkdirs();
            filesFolder.mkdirs();

            StringBuilder sb = new StringBuilder();
            StringBuilder appFilterBuilder = new StringBuilder();
            StringBuilder appMapBuilder = new StringBuilder();
            StringBuilder themeResourcesBuilder = new StringBuilder();

            int appsCount = 0;
            sb.append("These apps have no icons, please add some for them. Thanks in advance.\n\n");

            for (int i = 0; i < appsNames.size(); i++) {

                if (context.getResources().getBoolean(R.bool.request_tool_comments)) {
                    appFilterBuilder.append("<!-- " + appsNames.get(i) +
                            " -->\n");

                    appMapBuilder.append("<!-- " + appsNames.get(i) +
                            " -->\n");

                    themeResourcesBuilder.append("<!-- " + appsNames.get(i) +
                            " -->\n");
                }

                appFilterBuilder.append("<item component=\"ComponentInfo{" +
                        appsPackages.get(i) + "/" + appsClasses.get(i) + "}\"" +
                        " drawable=\"" + appsNames.get(i).replace(" ", "_").toLowerCase() + "\"/>" + "\n");

                appMapBuilder.append("<item name=\"" + appsNames.get(i).replace(" ", "_").toLowerCase() +
                        "\" class=\"" + appsClasses.get(i) + "\" />" + "\n");

                themeResourcesBuilder.append("<AppIcon name=\"" +
                        appsPackages.get(i) + "/" + appsClasses.get(i) +
                        "\" image=\"" + appsNames.get(i).replace(" ", "_").toLowerCase() + "\"/>" + "\n");

                sb.append("App Name: " + appsNames.get(i) + "\n");
                sb.append("App Info: " + appsPackages.get(i) + "/" + appsClasses.get(i) + "\n");
                sb.append("App Link: " + "https://play.google.com/store/apps/details?id=" + appsPackages.get(i) + "\n");
                sb.append("\n");
                sb.append("\n");

                Bitmap bitmap = ((BitmapDrawable) (appsIcons.get(i))).getBitmap();

                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(filesLocation + "/" + appsNames.get(i).replace(" ", "_").toLowerCase() + ".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    //Do nothing
                }

                appsCount++;
            }

            sb.append("\nOS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")");
            sb.append("\nOS API Level: " + Build.VERSION.SDK_INT);
            sb.append("\nDevice: " + Build.DEVICE);
            sb.append("\nManufacturer: " + Build.MANUFACTURER);
            sb.append("\nModel (and Product): " + Build.MODEL + " (" + Build.PRODUCT + ")");
            if (Utils.isAppInstalled(context, "org.cyanogenmod.theme.chooser")) {
                sb.append("\nCMTE is installed");
            }
            if (Utils.isAppInstalled(context, "com.cyngn.theme.chooser")) {
                sb.append("\nCyngn theme engine is installed");
            }
            if (Utils.isAppInstalled(context, "com.lovejoy777.rroandlayersmanager")) {
                sb.append("\nLayers Manager is installed");
            }

            try {
                PackageInfo appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                sb.append("\nApp Version Name: " + appInfo.versionName);
                sb.append("\nApp Version Code: " + appInfo.versionCode);
            } catch (Exception e) {
                //Do nothing
            }

            if (appsCount != 0) {

                try {
                    FileWriter fileWriter1 = new FileWriter(filesLocation + "/" + date + "_appfilter.xml");
                    BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                    bufferedWriter1.write(appFilterBuilder.toString());
                    bufferedWriter1.close();
                } catch (Exception e) {
                    return null;
                }

                try {
                    FileWriter fileWriter2 = new FileWriter(filesLocation + "/" + date + "_appmap.xml");
                    BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
                    bufferedWriter2.write(appMapBuilder.toString());
                    bufferedWriter2.close();
                } catch (Exception e) {
                    return null;
                }

                try {
                    FileWriter fileWriter3 = new FileWriter(filesLocation + "/" + date + "_theme_resources.xml");
                    BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter3);
                    bufferedWriter3.write(themeResourcesBuilder.toString());
                    bufferedWriter3.close();
                } catch (Exception e) {
                    return null;
                }

                createZipFile(filesLocation, true, zipFilePath);
                deleteDirectory(filesFolder);

            }

            worked = true;
            emailContent = sb;

        } catch (Exception e) {
            worked = false;
            emailContent = null;
            e.getLocalizedMessage();
        }
        return worked;

    }

    @Override
    protected void onPostExecute(Boolean worked) {

        if (worked) {

            if (emailContent != null) {
                dialog.dismiss();
                final Uri uri = Uri.parse("file://" + zipFilePath);

                String[] recipients = new String[]{context.getString(R.string.email_id)};

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("application/zip");
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendIntent.putExtra("android.intent.extra.EMAIL", recipients);
                sendIntent.putExtra("android.intent.extra.SUBJECT",
                        context.getString(R.string.request_title));
                sendIntent.putExtra("android.intent.extra.TEXT", emailContent.toString());
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    activity.startActivityForResult(Intent.createChooser(sendIntent, "Send mail..."), 2);
                    Calendar c = Calendar.getInstance();
                    Preferences mPrefs = new Preferences(context);
                    String time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                            String.format("%02d", c.get(Calendar.MINUTE));
                    String day = String.format("%02d", c.get(Calendar.DAY_OF_YEAR));
                    mPrefs.setRequestHour(time);
                    mPrefs.setRequestDay(Integer.valueOf(day));
                    mPrefs.setRequestsCreated(true);
                } catch (ActivityNotFoundException e) {
                    //Do nothing
                }
            }

        } else {
            dialog.setContent(R.string.error);
        }

    }

    public static void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                    String[] children = dir.list();
                    for (String aChildren : children) {
                        new File(dir, aChildren).delete();
                    }
                } else {
                    file.delete();
                }
            }

        }
    }

    public static void createZipFile(final String path, final boolean keepDirectoryStructure, final String outputFile) {
        final File filesFolder = new File(path);

        if (!filesFolder.canRead() || !filesFolder.canWrite()) {
            return;
        }

        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(outputFile), BUFFER));
            if (keepDirectoryStructure) {
                zipFile(path, zipOutputStream, "");
            } else {
                final File files[] = filesFolder.listFiles();
                for (final File file : files) {
                    zipFolder(file, zipOutputStream);
                }
            }
            zipOutputStream.close();
        } catch (FileNotFoundException e) {
            Utils.showLog("File not found" + e.getMessage());
        } catch (IOException e) {
            Utils.showLog("IOException" + e.getMessage());
        }
    }

    public static void zipFile(final String zipFilesPath, final ZipOutputStream zipOutputStream, final String zipPath) throws IOException {
        final File file = new File(zipFilesPath);

        if (!file.exists()) {
            return;
        }

        final byte[] buf = new byte[1024];
        final String[] files = file.list();

        if (file.isFile()) {
            FileInputStream in = new FileInputStream(file.getAbsolutePath());
            try {
                zipOutputStream.putNextEntry(new ZipEntry(zipPath + file.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    zipOutputStream.write(buf, 0, len);
                }
                zipOutputStream.closeEntry();
                in.close();
            } catch (ZipException e) {
                //Do nothing
            } finally {
                if (zipOutputStream != null) zipOutputStream.closeEntry();
                in.close();
            }
        } else if (files.length > 0) {
            for (String file1 : files) {
                zipFile(zipFilesPath + "/" + file1, zipOutputStream, zipPath + file.getName() + "/");
            }
        }
    }

    private static void zipFolder(File file, ZipOutputStream zipOutputStream) throws IOException {
        byte[] data = new byte[BUFFER];
        int read;

        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream(file));
            while ((read = bufferedInputStream.read(data, 0, BUFFER)) != -1)
                zipOutputStream.write(data, 0, read);
            zipOutputStream.closeEntry();
            bufferedInputStream.close();
        } else if (file.isDirectory()) {
            String[] list = file.list();
            for (String aList : list)
                zipFolder(new File(file.getPath() + "/" + aList), zipOutputStream);
        }
    }

}