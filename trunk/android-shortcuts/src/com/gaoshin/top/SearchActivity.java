package com.gaoshin.top;

import android.os.Bundle;

public class SearchActivity extends ShortcutActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().longSearch();
        finish();
    }
}
