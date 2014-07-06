package com.gaoshin.top;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GroupListAdapter extends BaseAdapter {

    private List<ShortcutGroup> groupList;

    public GroupListAdapter(List<ShortcutGroup> groupList) {
        this.groupList = groupList;
    }

    public void setGroupList(List<ShortcutGroup> groupList) {
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return groupList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return groupList.get(arg0).getId();
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup vg) {
        GroupIconView view = null;
        if (convertView == null) {
            view = new GroupIconView(vg.getContext(), groupList.get(arg0));
        } else {
            view = (GroupIconView) convertView;
            view.setShortcutGroup(groupList.get(arg0));
        }

        return view;
    }
}