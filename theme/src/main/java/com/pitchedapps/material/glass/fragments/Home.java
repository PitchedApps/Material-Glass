package com.pitchedapps.material.glass.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.Donations;

/**
 * Created by Jahir on 28/02/2015.
 */
public class Home extends Fragment {

    private Context context;

    public static Fragment newInstance(Context context) {
        Home f = new Home();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_home, null);

        context = getActivity();

        ActionBar toolbar = ((ActionBarActivity)context).getSupportActionBar();
               

        TextView donatebtn = (TextView) root.findViewById(R.id.donate_button);
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donate = new Intent(getActivity(), Donations.class);
                startActivity(donate);
                //TODO add animations
            }
        });

        TextView playbtn = (TextView) root.findViewById(R.id.play_button);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devPlay = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_dev_link)));
                startActivity(devPlay);
            }
        });

        TextView pitchedglassbtn = (TextView) root.findViewById(R.id.pitchedglass_button);
        pitchedglassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pitchedglass = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.pitched_glass_link)));
                startActivity(pitchedglass);
            }
        });

        TextView xdabtn = (TextView) root.findViewById(R.id.xda_button);
        xdabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent xda = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.xda_link)));
                startActivity(xda);
            }
        });

        return root;
    }

}
