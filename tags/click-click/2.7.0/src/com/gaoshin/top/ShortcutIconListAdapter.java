package com.gaoshin.top;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ShortcutIconListAdapter extends BaseAdapter {
    private List<Shortcut> shortcuts;

    public ShortcutIconListAdapter(List<Shortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }

    @Override
    public int getCount() {
        if (shortcuts == null)
            return 0;
        else
            return shortcuts.size();
    }

    @Override
    public Object getItem(int position) {
        return shortcuts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shortcuts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        ShortcutIconView iv = null;
        if(convertView == null) {
            layout = new LinearLayout(parent.getContext());
            iv = new ShortcutIconView(parent.getContext(), shortcuts.get(position));
            layout.addView(iv);
        } else {
            layout = (LinearLayout) convertView;
            iv = (ShortcutIconView) layout.getChildAt(0);
            iv.setShortcut(shortcuts.get(position));
        }
        return layout;
    }

    public void setShortcuts(List<Shortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

}
