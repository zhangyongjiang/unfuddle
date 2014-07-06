package com.gaoshin.coupon.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.gaoshin.sorma.SORMA;

public class CouponActivity extends Activity {
    protected Handler handler;
    
    protected SORMA getSorma() {
        return getApp().getSorma();
    }
    
    protected CouponApplication getApp() {
        CouponApplication app = (CouponApplication) getApplication();
        return app;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        getApp().activityPaused(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        getApp().activityResumed(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
}