package com.gaoshin.top;

import android.os.Bundle;

public class ShortcutEnableActivity extends ShortcutActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getApp().isShortcutBarVisible()) {
            getApp().hideShortcutBars();
        }
        else {
            getApp().displayShortcutBars();
        }
        finish();
    }
}
