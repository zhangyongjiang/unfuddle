package com.gaoshin.stock;

import android.util.Log;
import android.view.MotionEvent;

public class MotionEventTracker {
    private static final String TAG = MotionEventTracker.class.getSimpleName();
    
    private long lastEventTime = 0;
    private float lastX;
    private float lastY;

    public float processMotionEvent(MotionEvent event) {
        Log.i(TAG, "processMotionEvent " + event.getAction() + ", " + event.getX() + ", " + event.getY());
        
        float ratio = 0;
        float deltaX = 0;
        float deltaY = 0;
        
        long now = System.currentTimeMillis();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            lastEventTime = now;
            lastX = event.getX();
            lastY = event.getY();
        }
        else {
            deltaX = event.getX() - lastX;
            deltaY = event.getY() - lastY;
            if(Math.abs(deltaX) > 0.001 && Math.abs(deltaY) > 0.001) {
                ratio = deltaX / deltaY;
                lastEventTime = now;
                lastX = event.getX();
                lastY = event.getY();
            }
        }

        Log.i(TAG, "x: " + event.getX() + ", y: " + event.getY() + ", deltaX " + deltaX + ", deltaY " + deltaY + ", ratio: " + ratio);
        return ratio;
    }
}
