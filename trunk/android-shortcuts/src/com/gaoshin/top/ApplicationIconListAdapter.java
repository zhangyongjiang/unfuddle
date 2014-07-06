package com.gaoshin.top;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ApplicationIconListAdapter extends BaseAdapter {

    private List<Drawable> iconList;

    public ApplicationIconListAdapter(List<Drawable> appList) {
        this.iconList = appList;
    }

    @Override
    public int getCount() {
        return iconList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return iconList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup vg) {
        ImageView icon = null;
        if (convertView == null) {
            icon = new ImageView(vg.getContext());
            GridView.LayoutParams lp = new GridView.LayoutParams(80, 80);
            icon.setLayoutParams(lp);
        } else {
            icon = (ImageView) convertView;
        }
        icon.setImageDrawable(iconList.get(pos));

        return icon;
    }
}