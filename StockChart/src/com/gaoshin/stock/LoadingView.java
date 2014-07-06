package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import common.android.Resource;

public class LoadingView extends LinearLayout {
    private static int[] imgIds = new int[8];
    private int currentLoadingImg = 0;
    private Handler handler;
    private Runnable runnable;
    private ImageView imageView;
    private TextView label;
    private long delay = 0;

    public LoadingView(Context context) {
        super(context);
        handler = new Handler();
        setOrientation(HORIZONTAL);

        for(int i=0; i<8; i++) {
            imgIds[i] = Resource.drawable(getContext(), "loading_48x48_"+i);
        }
        
        imageView = new ImageView(context);
        imageView.setImageResource(imgIds[currentLoadingImg]);
        runnable = new Runnable() {
            @Override
            public void run() {
                if(delay <= 0) {
                    setVisibility(View.GONE);
                    return;
                }
//                delay -= 150;
                currentLoadingImg++;
                if(currentLoadingImg >= imgIds.length) {
                    currentLoadingImg = 0;
                }
                imageView.setImageResource(imgIds[currentLoadingImg]);
                handler.postDelayed(runnable, 150);
            }
        };
        addView(imageView);
        
        label = new TextView(context);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setTextColor(Color.BLACK);
        addView(label);
    }
    
    public synchronized void start(String msg) {
        //label.setText(msg);
        setVisibility(View.VISIBLE);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 150);
        delay = 2000;
    }
    
    public synchronized void stop() {
        delay = 0;
    }
}
