package com.gaoshin.stock;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class LoadingView extends ImageView {
    private static int[] imgIds = {
            R.drawable.loading_48x48_0,
            R.drawable.loading_48x48_1,
            R.drawable.loading_48x48_2,
            R.drawable.loading_48x48_3,
            R.drawable.loading_48x48_4,
            R.drawable.loading_48x48_5,
            R.drawable.loading_48x48_6,
            R.drawable.loading_48x48_7,
    };
    private int currentLoadingImg = 0;
    private Handler handler;
    private Runnable runnable;
    private boolean run = false;

    public LoadingView(Context context) {
        super(context);
        handler = new Handler();
        setImageResource(imgIds[currentLoadingImg]);
        runnable = new Runnable() {
            @Override
            public void run() {
                if(!run) {
                    return;
                }
                currentLoadingImg++;
                if(currentLoadingImg >= imgIds.length) {
                    currentLoadingImg = 0;
                }
                setImageResource(imgIds[currentLoadingImg]);
                handler.postDelayed(runnable, 150);
            }
        };
    }
    
    public synchronized void start() {
        setVisibility(View.VISIBLE);
        run = true;
        handler.postDelayed(runnable, 150);
    }
    
    public synchronized void stop() {
        setVisibility(View.GONE);
        run = false;
    }
}
