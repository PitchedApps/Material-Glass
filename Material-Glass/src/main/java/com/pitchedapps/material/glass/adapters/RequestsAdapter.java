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

package com.pitchedapps.material.glass.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.dialogs.ISDialogs;
import com.pitchedapps.material.glass.models.RequestItem;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsHolder> {

    public interface ClickListener {

        void onClick(int index);
    }

    public ArrayList<RequestItem> appsList;
    Context context;
    private int limit;
    private final ClickListener mCallback;
    AppIconFetchingQueue mAppIconFetchingQueue;

    public RequestsAdapter(final Context context, final ArrayList<RequestItem> appsList,
                           final int limit) {
        this.context = context;
        this.appsList = appsList;
        this.limit = limit;
        this.mCallback = new ClickListener() {
            @Override
            public void onClick(int position) {
                if (limit <= 0) {
                    changeAppSelectedState(position);
                } else {
                    if (getSelectedApps() < limit) {
                        changeAppSelectedState(position);
                    } else {
                        if (isSelected(position)) {
                            changeAppSelectedState(position);
                        } else {
                            ISDialogs.showRequestLimitDialog(context, limit);
                        }
                    }
                }
            }
        };
    }

    @Override
    public RequestsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                context.getResources().getBoolean(R.bool.request_cards) ?
                        R.layout.card_app_to_request :
                        R.layout.item_app_to_request, parent, false);
        return new RequestsHolder(v);
    }

    @Override
    public void onBindViewHolder(RequestsHolder holder, int position) {
        RequestItem requestsItem = appsList.get(position);
        holder.txtName.setText(requestsItem.getAppName());
        holder.imgIcon.setImageDrawable(requestsItem.getIcon());
        holder.chkSelected.setChecked(requestsItem.isSelected());
        if (context.getResources().getBoolean(R.bool.request_cards)) {
            holder.cardView.setTag(position);
        } else {
            holder.view.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return appsList == null ? 0 : appsList.size();
    }

    public class RequestsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout view = null;
        CardView cardView = null;
        final ImageView imgIcon;
        final TextView txtName;
        final CheckBox chkSelected;

        public RequestsHolder(View v) {
            super(v);
            imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
            txtName = (TextView) v.findViewById(R.id.txtName);
            chkSelected = (CheckBox) v.findViewById(R.id.chkSelected);
            if (context.getResources().getBoolean(R.bool.request_cards)) {
                cardView = (CardView) v.findViewById(R.id.requestCard);
                cardView.setOnClickListener(this);
            } else {
                view = (LinearLayout) v.findViewById(R.id.requestCard);
                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getTag() != null) {
                int index = (Integer) v.getTag();
                if (mCallback != null)
                    mCallback.onClick(index);
            }
        }

    }

    public void selectOrDeselectAll(boolean select) {
        boolean showDialog = false;

        for (int i = 0; i < appsList.size(); i++) {
            if (select) {
                if (limit <= 0) {
                    selectApp(i);
                } else {
                    if (getSelectedApps() < limit) {
                        selectApp(i);
                    } else {
                        showDialog = true;
                        break;
                    }
                }
            } else {
                deselectApp(i);
            }
        }

        if (showDialog) {
            ISDialogs.showRequestLimitDialog(context, limit);
        }

    }

    public void selectApp(int position) {
        RequestItem requestsItem = appsList.get(position);
        if (!requestsItem.isSelected()) {
            requestsItem.setSelected(true);
            appsList.set(position, requestsItem);
            notifyItemChanged(position);
        }
    }

    public void deselectApp(int position) {
        RequestItem requestsItem = appsList.get(position);
        if (requestsItem.isSelected()) {
            requestsItem.setSelected(false);
            appsList.set(position, requestsItem);
            notifyItemChanged(position);
        }
    }

    public void changeAppSelectedState(int position) {
        RequestItem requestsItem = appsList.get(position);
        requestsItem.setSelected(!requestsItem.isSelected());
        appsList.set(position, requestsItem);
        notifyItemChanged(position);
    }

    public int getSelectedApps() {
        int selected = 0;
        for (int i = 0; i < appsList.size(); i++) {
            if (appsList.get(i).isSelected()) {
                selected += 1;
            }
        }
        return selected;
    }

    public boolean isSelected(int i) {
        return appsList.get(i).isSelected();
    }

    public void startIconFetching(RecyclerView view) {
        mAppIconFetchingQueue = new AppIconFetchingQueue(view);
    }

    public void stopAppIconFetching() {
        if (mAppIconFetchingQueue != null) {
            mAppIconFetchingQueue.stop();
        }
    }

    public class AppIconFetchingQueue {

        int mIconsRemaining;
        RecyclerView mRecyclerView;

        AppIconFetchingQueue(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mIconsRemaining = appsList != null ? appsList.size() : 0;
        }

        public void stop() {
            // Avoids calling stop on thread, which will cause crash.
            mIconsRemaining = 0;
        }

    }

}