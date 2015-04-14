package com.pitchedapps.material.glass.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;

/**
 * Created by Jahir on 08/03/2015.
 */
public class Credits extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.section_credits, null);

        context = getActivity();

        ActionBar toolbar = ((ActionBarActivity)context).getSupportActionBar();
        toolbar.setTitle(R.string.section_six);
        toolbar.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));

        TextView authordesc = (TextView) root.findViewById(R.id.dashauthor_info);
        authordesc.setText(Html.fromHtml(getString(R.string.dashboard_author_desc)));

        TextView designerdesc = (TextView) root.findViewById(R.id.dev_card_content);
        designerdesc.setText(Html.fromHtml(getString(R.string.themer_desc)));

        TextView ivondesc = (TextView) root.findViewById(R.id.ivon_card_content);
        ivondesc.setText(Html.fromHtml(getString(R.string.ivon_desc)));

        TextView shapeviewlib = (TextView) root.findViewById(R.id.libone_content);
        shapeviewlib.setText(Html.fromHtml(getString(R.string.shapeview_desc)));

        TextView fablib = (TextView) root.findViewById(R.id.libtwo_content);
        fablib.setText(Html.fromHtml(getString(R.string.fab_desc)));

        TextView materialdialogslib = (TextView) root.findViewById(R.id.libthree_content);
        materialdialogslib.setText(Html.fromHtml(getString(R.string.materialdialogs_desc)));

        TextView materialdrawerlib = (TextView) root.findViewById(R.id.libfour_content);
        materialdrawerlib.setText(Html.fromHtml(getString(R.string.materialdrawer_desc)));

        TextView picassolib = (TextView) root.findViewById(R.id.libfive_content);
        picassolib.setText(Html.fromHtml(getString(R.string.picasso_desc)));

        TextView okhttplib = (TextView) root.findViewById(R.id.libeight_content);
        okhttplib.setText(Html.fromHtml(getString(R.string.okhttp_desc)));

        TextView donatelib = (TextView) root.findViewById(R.id.libdonate_content);
        donatelib.setText(Html.fromHtml(getString(R.string.libdonate_desc)));

        CardView libtwocard = (CardView) root.findViewById(R.id.libtwocard);
        libtwocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libtwoweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.fab_web)));
                startActivity(libtwoweb);
            }
        });

        CardView libthreecard = (CardView) root.findViewById(R.id.libthreecard);
        libthreecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libthreeweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.materialdialogs_web)));
                startActivity(libthreeweb);
            }
        });

        CardView libfourcard = (CardView) root.findViewById(R.id.libfourcard);
        libfourcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libfourweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.materialdrawer_web)));
                startActivity(libfourweb);
            }
        });

        CardView libfivecard = (CardView) root.findViewById(R.id.libfivecard);
        libfivecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libfiveweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.picasso_web)));
                startActivity(libfiveweb);
            }
        });

        CardView libeightcard = (CardView) root.findViewById(R.id.libeightcard);
        libeightcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libeightweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.okhttp_web)));
                startActivity(libeightweb);
            }
        });

        CardView libdonatecard = (CardView) root.findViewById(R.id.libdonatecard);
        libdonatecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libdonateweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.libdonate_web)));
                startActivity(libdonateweb);
            }
        });

        TextView dashauthorweb = (TextView) root.findViewById(R.id.dashauthor_web_button);
        dashauthorweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashauthorweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dashboard_author_link)));
                startActivity(dashauthorweb);
            }
        });

        TextView dashauthorgoogleplus = (TextView) root.findViewById(R.id.dashauthor_gplus_button);
        dashauthorgoogleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashauthorgplus = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dashboard_author_gplus)));
                startActivity(dashauthorgplus);
            }
        });

        TextView web = (TextView) root.findViewById(R.id.play_button);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devplay = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_store_dev_link)));
                startActivity(devplay);
            }
        });

        TextView googleplus = (TextView) root.findViewById(R.id.gplus_button);
        googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent devgplus = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_gplus_link)));
                startActivity(devgplus);
            }
        });

        TextView ivonweb = (TextView) root.findViewById(R.id.ivon_play_button);
        ivonweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ivonweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.ivon_play)));
                startActivity(ivonweb);
            }
        });

        TextView ivongoogleplus = (TextView) root.findViewById(R.id.ivon_gplus_button);
        ivongoogleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ivongplus = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.ivon_gplus_link)));
                startActivity(ivongplus);
            }
        });

        return root;
    }

}
