package com.pitchedapps.material.glass.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.pitchedapps.material.glass.R;
import com.pkmmte.requestmanager.AppInfo;
import com.pkmmte.requestmanager.PkRequestManager;

import java.util.LinkedList;
import java.util.List;

public class RequestFragment extends Fragment {

    // App List
    private List<AppInfo> mApps = new LinkedList<>();
    // Request Manager
    private PkRequestManager mRequestManager;
    // List & Adapter
    private ListView mList;
    private ListAdapter mAdapter;
    private View mProgress;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_icon_request, container, false);

//        showNewAdviceDialog();

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(R.string.section_five);

        // Populate your ListView with your apps
        mList = (ListView) root.findViewById(R.id.appList);
        mList.setVisibility(View.GONE);

        // Progress
        mProgress = root.findViewById(R.id.progress);

        new GrabApplicationsTask().execute();

        // Set basic listener to your ListView
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mark the app as selected
                AppInfo mApp = mApps.get(position);
                mApp.setSelected(!mApp.isSelected());
                mApps.set(position, mApp);

                // Let the adapter know you selected something
                mAdapter.notifyDataSetChanged();
            }
        });

        fab = (FloatingActionButton) root.findViewById(R.id.send_btn);
        fab.hide(true);
        fab.attachToListView(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProgressDialog dialog;
//                dialog = new ProgressDialog(getActivity());
//                dialog.setMessage("Your message..");
//                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                dialog.setMax(100);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
                Toast.makeText(getActivity(), getString(R.string.building_request), Toast.LENGTH_LONG).show();
                mRequestManager.setActivity(getActivity());
                if (mRequestManager.getNumSelected() < 1)
                    mRequestManager.sendAutomaticRequestAsync();
                else
                    mRequestManager.sendRequestAsync();
//                dialog.dismiss();
            }

        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.unfiltered_request), Toast.LENGTH_LONG).show();
                mRequestManager.removeAllAppFilterListeners();
//                mRequestManager.setActivity(getActivity());
//                if (mRequestManager.getNumSelected() < 1)
//                    mRequestManager.sendRequest(true, false);
                //TODO check if this is right
                return false;
            }
        });
        return root;
    }

    private class GrabApplicationsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                mRequestManager = PkRequestManager.getInstance(getActivity());
                mRequestManager.setDebugging(false);
                mRequestManager.loadAppsIfEmpty();
                mApps = mRequestManager.getApps();
            } catch (Exception ex) {
                //could happen that the activity detaches :D
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            mAdapter = new ListAdapter(mApps);
            mList.setAdapter(mAdapter);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
            //TODO switch toggle method? (see below)
            if (mList != null)
                mList.setVisibility(View.VISIBLE);
            if (fab != null)
                fab.show(true);
            if (mProgress != null)
                mProgress.setVisibility(View.GONE);
        }
    }


    // You should probably put this in a separate .java file
    private class ListAdapter extends BaseAdapter {

        private final List<AppInfo> mApps;

        public ListAdapter(List<AppInfo> apps) {
            this.mApps = apps;
        }

        @Override
        public int getCount() {
            return mApps.size();
        }

        @Override
        public AppInfo getItem(int position) {
            return mApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            AppInfo mApp = mApps.get(position);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.request_item, parent, false);

                holder = new ViewHolder();
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
                holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
                holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtName.setText(mApp.getName());
            holder.imgIcon.setImageDrawable(mApp.getImage());
            holder.chkSelected.setChecked(mApp.isSelected());

            return convertView;
        }

        private class ViewHolder {
            public ImageView imgIcon;
            public TextView txtName;
            public CheckBox chkSelected;
        }
    }
}