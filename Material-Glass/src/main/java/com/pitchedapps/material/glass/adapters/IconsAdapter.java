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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import com.pitchedapps.material.glass.R;
import com.pitchedapps.material.glass.activities.ShowcaseActivity;
import com.pitchedapps.material.glass.models.IconItem;
import com.pitchedapps.material.glass.utilities.Preferences;
import com.pitchedapps.material.glass.utilities.Utils;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.IconsHolder> {

    private final Context context;
    private boolean inChangelog = false;
    private ArrayList<IconItem> iconsList = new ArrayList<>();
    private Bitmap bitmap;
    private Preferences mPrefs;

    public IconsAdapter(Context context, ArrayList<IconItem> iconsList) {
        this.context = context;
        this.iconsList = iconsList;
        this.inChangelog = false;
        this.mPrefs = new Preferences(context);
    }

    public IconsAdapter(Context context, ArrayList<IconItem> iconsList,
                        boolean inChangelog) {
        this.context = context;
        this.iconsList = iconsList;
        this.inChangelog = inChangelog;
        this.mPrefs = new Preferences(context);
    }

    public void setIcons(ArrayList<IconItem> iconsList) {
        this.iconsList.addAll(iconsList);
        this.notifyItemRangeInserted(0, iconsList.size() - 1);
    }

    public void clearIconsList() {
        this.iconsList.clear();
    }

    @Override
    public IconsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new IconsHolder(inflater.inflate(R.layout.item_icon, parent, false));
    }

    @Override
    public void onBindViewHolder(IconsHolder holder, final int position) {
        int iconResource = iconsList.get(position).getResId();
        if (iconResource != 0) {
            Glide.with(context)
                    .load(iconResource)
                    .dontAnimate()
                    .into(holder.icon);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconClick(v, position);
            }
        });
        if (!inChangelog) {
            setAnimation(holder.icon, position);
        }
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_slide);
        if (position > lastPosition && mPrefs.getAnimationsEnabled()) {
            viewToAnimate.setHasTransientState(true);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return iconsList == null ? 0 : iconsList.size();
    }

    private void iconClick(View v, int position) {
        int resId = iconsList.get(position).getResId();
        String name = iconsList.get(position).getName().toLowerCase(Locale.getDefault());

        if (ShowcaseActivity.iconsPicker) {
            Intent intent = new Intent();
            bitmap = null;

            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            } catch (Exception e) {
                Utils.showLog(context, "Icons Picker error: " + Log.getStackTraceString(e));
            }

            if (bitmap != null) {
                intent.putExtra("icon", bitmap);
                intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", resId);
                String bmUri = "android.resource://" + context.getPackageName() + "/" + String.valueOf(resId);
                intent.setData(Uri.parse(bmUri));
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
            } else {
                ((Activity) context).setResult(Activity.RESULT_CANCELED, intent);
            }

            ((Activity) context).finish();

        } else {
            if (!inChangelog) {
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.dialog_icon, false)
                        .title(Utils.makeTextReadable(name))
                        .positiveText(R.string.close)
                        .show();

                if (dialog.getCustomView() != null) {
                    ImageView dialogIcon = (ImageView) dialog.getCustomView().findViewById(R.id.dialogicon);
                    dialogIcon.setImageResource(resId);
                }
            }
        }
    }

    class IconsHolder extends RecyclerView.ViewHolder {

        final View view;
        final ImageView icon;

        IconsHolder(View v) {
            super(v);
            view = v;
            icon = (ImageView) v.findViewById(R.id.icon_img);
        }
    }

}
