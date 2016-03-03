package com.pitchedapps.material.glass.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.models.HomeCard;
import com.pitchedapps.material.glass.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by 7681 on 2016-02-24.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeCardView>{

    private Context c;
    public View view;
    ArrayList<HomeCard> homeCards;

    public HomeListAdapter(ArrayList<HomeCard> homeCards, Context c){
        this.c = c;
        this.homeCards = homeCards;
    }

    public class HomeCardView extends RecyclerView.ViewHolder {
        LinearLayout lly, subLly;
        TextView cardTitle, cardDesc;
        ImageView cardIcon;

        HomeCardView(View itemView) {
            super(itemView);
            view = itemView;
            lly = (LinearLayout)itemView.findViewById(R.id.home_card);
            cardTitle = (TextView)itemView.findViewById(R.id.home_card_text);
            cardDesc = (TextView)itemView.findViewById(R.id.home_card_description);
            cardIcon = (ImageView) itemView.findViewById(R.id.home_card_image);
            subLly = (LinearLayout) itemView.findViewById(R.id.home_card_sub_layout);
        }

    }

    @Override
    public HomeCardView onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_home, viewGroup, true);
        if (v != null) {
            Utils.log("null view");
        } else {
            Utils.log("view " + v.toString());
        }
        final HomeCardView h = new HomeCardView(v);

        Utils.log("count " + getItemCount());
        final int finalI = i;
        v.setOnClickListener(new OnClickListener() { //TODO make clicks work for more than just links
            @Override
            public void onClick(View v) {
                int position = h.getAdapterPosition();
                Utils.log("position " + position);
                Utils.log("i " + finalI);
                if (position != RecyclerView.NO_POSITION) {
                    Utils.log("link " +  homeCards.get(position).onClickLink);
                    Utils.openLink(c, homeCards.get(position).onClickLink);
                }
            }
        });
        HomeCardView hcv = new HomeCardView(v);
        return hcv;
    }

    @Override
    public void onBindViewHolder(HomeCardView homeCardView, int i) {
        homeCardView.cardTitle.setText(homeCards.get(i).title);
        homeCardView.cardDesc.setText(homeCards.get(i).desc);
        if(homeCards.get(i).imgEnabled) {
            homeCardView.cardIcon.setImageDrawable(homeCards.get(i).img);
        } else {
            homeCardView.subLly.removeView(homeCardView.cardIcon);
        }
    }

    @Override
    public int getItemCount() {
        return homeCards.size();
    }
}


