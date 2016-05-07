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

package jahirfiquitiva.iconshowcase.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import jahirfiquitiva.iconshowcase.R;
import jahirfiquitiva.iconshowcase.models.WallpaperItem;
import jahirfiquitiva.iconshowcase.utilities.Preferences;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.WallsHolder> {

    public interface ClickListener {

        void onClick(WallsHolder view, int index, boolean longClick);
    }

    private final Activity context;
    private final Preferences mPrefs;

    private ArrayList<WallpaperItem> wallsList;

    private boolean USE_PALETTE = true, USE_PALETTE_IN_TEXTS = false;
    private String PALETTE_STYLE = "VIBRANT";

    private final ClickListener mCallback;

    public WallpapersAdapter(Activity context, ClickListener callback) {
        this.context = context;
        this.mCallback = callback;
        this.mPrefs = new Preferences(context);

        USE_PALETTE = context.getResources().getBoolean(R.bool.use_palette_api);
        USE_PALETTE_IN_TEXTS = context.getResources().getBoolean(R.bool.use_palette_api_in_texts);
        switch (context.getResources().getInteger(R.integer.palette_swatch)) {
            case 1:
                PALETTE_STYLE = "VIBRANT";
                break;
            case 2:
                PALETTE_STYLE = "VIBRANT_LIGHT";
                break;
            case 3:
                PALETTE_STYLE = "VIBRANT_DARK";
                break;
            case 4:
                PALETTE_STYLE = "MUTED";
                break;
            case 5:
                PALETTE_STYLE = "MUTED_LIGHT";
                break;
            case 6:
                PALETTE_STYLE = "MUTED_DARK";
                break;
        }
    }

    public void setData(ArrayList<WallpaperItem> wallsList) {
        this.wallsList = wallsList;
        notifyDataSetChanged();
    }

    @Override
    public WallsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new WallsHolder(inflater.inflate(R.layout.item_wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(final WallsHolder holder, int position) {
        WallpaperItem wallItem = wallsList.get(position);

        ViewCompat.setTransitionName(holder.wall, "transition" + position);

        holder.name.setText(wallItem.getWallName());
        holder.authorName.setText(wallItem.getWallAuthor());

        final String wallUrl = wallItem.getWallURL();

        Glide.with(context)
                .load(wallUrl)
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.wall) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (mPrefs.getAnimationsEnabled()) {
                            TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                            holder.wall.setImageDrawable(td);
                            td.startTransition(250);
                        } else {
                            holder.wall.setImageBitmap(resource);
                        }

                        if (USE_PALETTE) {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch swatch;
                                    switch (PALETTE_STYLE) {
                                        case "VIBRANT":
                                            swatch = palette.getVibrantSwatch();
                                            break;
                                        case "VIBRANT_LIGHT":
                                            swatch = palette.getLightVibrantSwatch();
                                            break;
                                        case "VIBRANT_DARK":
                                            swatch = palette.getDarkVibrantSwatch();
                                            break;
                                        case "MUTED":
                                            swatch = palette.getMutedSwatch();
                                            break;
                                        case "MUTED_LIGHT":
                                            swatch = palette.getLightMutedSwatch();
                                            break;
                                        case "MUTED_DARK":
                                            swatch = palette.getDarkMutedSwatch();
                                            break;
                                        default:
                                            swatch = palette.getVibrantSwatch();
                                            break;
                                    }

                                    if (swatch == null) return;

                                    if (mPrefs.getAnimationsEnabled()) {
                                        TransitionDrawable td = new TransitionDrawable(
                                                new Drawable[]{holder.titleBg.getBackground(),
                                                        new ColorDrawable(swatch.getRgb())});
                                        holder.titleBg.setBackground(td);
                                        td.startTransition(250);
                                    } else {
                                        holder.titleBg.setBackgroundColor(swatch.getRgb());
                                    }

                                    if (USE_PALETTE_IN_TEXTS) {
                                        holder.name.setTextColor(swatch.getTitleTextColor());
                                        holder.authorName.setTextColor(swatch.getBodyTextColor());
                                    }
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return wallsList == null ? 0 : wallsList.size();
    }

    public class WallsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;
        public final ImageView wall;
        public final TextView name, authorName;
        public final LinearLayout titleBg;

        WallsHolder(View v) {
            super(v);
            view = v;
            wall = (ImageView) view.findViewById(R.id.wall);
            name = (TextView) view.findViewById(R.id.name);
            authorName = (TextView) view.findViewById(R.id.author);
            titleBg = (LinearLayout) view.findViewById(R.id.titleBg);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, false);
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, true);
            return false;
        }
    }
}