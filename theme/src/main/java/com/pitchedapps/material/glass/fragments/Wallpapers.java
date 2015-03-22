package com.pitchedapps.material.glass.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.DetailedWallpaper;
import com.pitchedapps.material.glass.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jahir on 10/03/2015.
 */
public class Wallpapers extends Fragment {

    private static final int DEFAULT_COLUMNS_PORTRAIT = 2;
    private int mColumnCountPortrait = DEFAULT_COLUMNS_PORTRAIT;
    private static final int DEFAULT_COLUMNS_LANDSCAPE = 3;
    private int mColumnCountLandscape = DEFAULT_COLUMNS_LANDSCAPE;
    static String NAME = "name";
    static String AUTHOR = "author";
    static String WALL = "wall";
    JSONObject jsonobject;
    JSONArray jsonarray;
    GridView mGridView;
    WallsGridAdapter mGridAdapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    private ViewGroup root;
    private Context context;
    private ProgressBar mProgress;
    private int mColumnCount;
    private int numColumns = 1;

    public static Fragment newInstance(Context context) {
        Wallpapers f = new Wallpapers();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        root = (ViewGroup) inflater.inflate(R.layout.section_wallpapers, null);

        mProgress = (ProgressBar) root.findViewById(R.id.progress);

        boolean isLandscape = isLandscape();
        int newColumnCount = isLandscape ? mColumnCountLandscape : mColumnCountPortrait;
        if (mColumnCount != newColumnCount) {
            mColumnCount = newColumnCount;
            numColumns = mColumnCount;
        }

        new DownloadJSON().execute();

        return root;

    }

    public boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONParser
                    .getJSONfromURL(getResources().getString(R.string.json_file_url));

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray("wallpapers");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put("name", jsonobject.getString("name"));
                    map.put("author", jsonobject.getString("author"));
                    map.put("wall", jsonobject.getString("url"));
                    // Set the JSON Objects into the array
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Toast.makeText(context, getString(R.string.json_error_toast), Toast.LENGTH_LONG).show();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            mGridView = (GridView) root.findViewById(R.id.gridView);
            mGridView.setNumColumns(numColumns);
            mGridAdapter = new WallsGridAdapter(context, arraylist, numColumns);
            mGridView.setAdapter(mGridAdapter);
            if (mProgress != null) {
                mProgress.setVisibility(View.GONE);
            }
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> data = arraylist.get(position);
                    String wallurl = data.get((Wallpapers.WALL));
                    Intent intent = new Intent(context, DetailedWallpaper.class);
                    intent.putExtra("wall", wallurl);
                    context.startActivity(intent);
                }
            });
        }
    }
}
