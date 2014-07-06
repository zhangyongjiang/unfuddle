package com.gaoshin.top;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.gaoshin.shortcut.R;

public class GroupIconView extends LabeledImage {
    protected ShortcutGroup group;

    public GroupIconView(Context context, ShortcutGroup group) {
        super(context, group.getDrawable(context), group.getName());
        this.group = group;
        if (getDrawable() == null) {
            setDrawable(context.getResources().getDrawable(R.drawable.for_trans_64));
            updateView();
        }
    }

    protected int getLines() {
        return 2;
    }

    protected boolean showLabel() {
        return false;
    }

    public void setShortcutGroup(ShortcutGroup group) {
        this.group = group;
        setDrawable(getGroupIconDrawable());
        setCaption(group.getName());
        updateView();
    }

    public ShortcutGroup getShortcutGroup() {
        return group;
    }

    private Drawable getGroupIconDrawable() {
        Drawable d = group.getDrawable(getContext());
        if (d == null) {
            d = getContext().getResources().getDrawable(R.drawable.for_trans_64);
        }
        return d;
    }
}
