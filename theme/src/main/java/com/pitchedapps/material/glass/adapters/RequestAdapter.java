package com.pitchedapps.material.glass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pitchedapps.material.glass.R;
import com.pkmmte.requestmanager.AppInfo;

import java.util.List;

public class RequestAdapter extends BaseAdapter
{
    private Context mContext;
    private List<AppInfo> mApps;

    public RequestAdapter(Context context, List<AppInfo> apps)
    {
        this.mContext = context;
        this.mApps = apps;
    }

    @Override
    public int getCount()
    {
        return mApps.size();
    }

    @Override
    public AppInfo getItem(int position)
    {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

//    @Override
//    public String getIndicatorForPosition(int childposition, int groupposition)
//    {
//        return Character.toString(mApps.get(childposition).getName().charAt(0)).toUpperCase(Locale.getDefault());
//    }
//
//    @Override
//    public int getScrollPosition(int childposition, int groupposition)
//    {
//        return childposition;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        AppInfo mApp = mApps.get(position);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_item, parent, false);

            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);

            convertView.setTag(holder);
        }
        else {
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