package com.gaoshin.top;

import android.content.Context;

import com.gaoshin.shortcut.ads.ShortcutView;

public class ShortcutIconView extends ShortcutView {

    public ShortcutIconView(Context context, Shortcut shortcut) {
        super(context, shortcut);
    }

    @Override
    protected int getViewWidth() {
        return 64;
    }

    @Override
    protected int getViewHeight() {
        return 64;
    }
}
