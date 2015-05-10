package com.pitchedapps.material.glass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;

public class Info extends Fragment
{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.info_page, container, false);
        TextView contentTextView = (TextView) view.findViewById(R.id.info_content);
        contentTextView.setText(getArguments().getString("content"));
        return view;

    }

    public static Info newInstance(String content) {
        Info fragment = new Info();
        Bundle args = new Bundle();
        args.putString("content", content);
        fragment.setArguments(args);
        return fragment;
    }

}