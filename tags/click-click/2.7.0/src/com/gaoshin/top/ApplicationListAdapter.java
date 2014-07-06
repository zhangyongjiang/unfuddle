package com.gaoshin.top;

import java.util.List;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ApplicationListAdapter extends BaseAdapter {

    private List<App> appList;
    private PackageManager pm;

    public ApplicationListAdapter(List<App> appList, PackageManager pm) {
        this.appList = appList;
        this.pm = pm;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return appList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup vg) {
        App app = appList.get(pos);
        if(app.getIcon() == null) {
            ComponentName cn = new ComponentName(app.getPkgName(), app.getActivityName());
            try {
                ActivityInfo info = pm.getActivityInfo(cn, 0);
                app.setIcon(info.loadIcon(pm));
            } catch (NameNotFoundException e) {
            }
        }
        
        AppView appView = null;
        if (convertView == null) {
            appView = new AppView(vg.getContext(), app);
        } else {
            appView = (AppView) convertView;
            appView.setApp(app);
        }

        return appView;
    }
}