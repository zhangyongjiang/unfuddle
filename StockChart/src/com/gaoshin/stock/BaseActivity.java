package com.gaoshin.stock;

import android.app.Activity;

public class BaseActivity extends Activity {
    public StockApplication getApp() {
        return (StockApplication) getApplication();
    }
}
