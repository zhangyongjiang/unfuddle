package com.gaoshin.top;

import android.os.Bundle;

public class ShortcutDisableActivity extends ShortcutActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().hideShortcutBars();
        finish();
    }
}
