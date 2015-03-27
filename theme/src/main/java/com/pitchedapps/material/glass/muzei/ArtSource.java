package com.pitchedapps.material.glass.muzei;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.utils.JSONParser;
import com.pitchedapps.material.glass.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ArtSource extends RemoteMuzeiArtSource {

    private WallsDatabase wdb;
    ArrayList<WallpaperInfo> wallslist;
    private static final String ARTSOURCE_NAME = "Material Glass";
    JSONObject jsonobject;

    private static final String JSON_URL = "https://raw.githubusercontent.com/jahirfiquitiva/MinDesigns-Wallpapers/master/JSON-Files/walls_test.json";
//    private static final String JSON_URL = "https://raw.githubusercontent.com/asdfasdfvful/Pitched-Wallpapers/master/Material_Glass/0wallpapers.json";

    public static final int COMMAND_ID_SHARE = 1337;

    private Preferences mPrefs;

    public ArtSource() {
        super(ARTSOURCE_NAME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getExtras().getString("service");
        if (command!=null) {
            try {
                onTryUpdate(UPDATE_REASON_USER_NEXT);
            } catch (RetryException e) {
                Log.d("MuzeiArtSource", Log.getStackTraceString(e));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wdb = new WallsDatabase(getApplicationContext());
        wallslist = new ArrayList<WallpaperInfo>();

        mPrefs = new Preferences(ArtSource.this);

        ArrayList<UserCommand> commands = new ArrayList<UserCommand>();
        commands.add(new UserCommand(BUILTIN_COMMAND_ID_NEXT_ARTWORK));
        commands.add(new UserCommand(COMMAND_ID_SHARE, getString(R.string.justshare)));

        setUserCommands(commands);

    }

    @Override
    public void onCustomCommand(int id){
        super.onCustomCommand(id);

        if (id == COMMAND_ID_SHARE){
            Artwork currentArtwork = getCurrentArtwork();
            Intent shareWall = new Intent (Intent.ACTION_SEND);
            shareWall.setType("text/plain");

            Uri artUrl = currentArtwork.getImageUri();
            String wallName = currentArtwork.getTitle();
            String authorName = currentArtwork.getByline();
            String storeUrl = getString(R.string.play_store_link);
            String iconPackName = getString(R.string.app_name);

            shareWall.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.partone) + wallName +
                            getString(R.string.parttwo) + authorName +
                            getString(R.string.partthree) + iconPackName + getString(R.string.partfour) +
                            getString(R.string.partfive) + storeUrl );

            shareWall = Intent.createChooser(shareWall, getString(R.string.share_title));
            shareWall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(shareWall);

        }

    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {

        String currentToken = (getCurrentArtwork() != null)? getCurrentArtwork().getToken() : null;

        if (wallslist.size()==0) {
            getWallpaperURL(JSON_URL);
        }

        int i = getRandomInt();

        String token = wallslist.get(i).getWallURL();
        publishArtwork(new Artwork.Builder()
                .title(wallslist.get(i).getWallName())
                .byline(wallslist.get(i).getWallAuthor())
                .imageUri(Uri.parse(wallslist.get(i).getWallURL()))
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,Uri.parse(wallslist.get(i).getWallURL())))
                .build());

        scheduleUpdate(System.currentTimeMillis() + mPrefs.getRotateTime());

    }

    private int getRandomInt() {
        Random random = new Random();
        return random.nextInt(wallslist.size());
    }

    private void getWallpaperURL(String URL) {

        wallslist.clear();
        wallslist = wdb.getAllWalls();
        if (wallslist.size() == 0) {
            try {
                HttpGet httppost = new HttpGet(URL);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    jsonobject = JSONParser
                            .getJSONfromURL(getResources().getString(R.string.json_file_url));

                    JSONObject jsonobject = new JSONObject(data);
                    JSONArray jsonarray = jsonobject.getJSONArray("wallpapers");

                    wallslist.clear();
                    wdb.deleteAllWallpapers();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject  = jsonarray.getJSONObject(i);

                        WallpaperInfo jsondata = new WallpaperInfo(
                                jsonobject.getString("name"),
                                jsonobject.getString("author"),
                                jsonobject.getString("url")

                        );
                        wdb.addWallpaper(jsondata);
                        wallslist.add(jsondata);

                    }
                }
            } catch (IOException | JSONException e) {
                Log.d("Wallpapers", Log.getStackTraceString(e));
            }
        }
    }

}
