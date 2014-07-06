package com.gaoshin.stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

public class TimeView extends TextView {
    private TimeZone timeZone;
    private Runnable runnable;
    private SimpleDateFormat sdf;
    private Handler handler;

    public TimeView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        setId(Math.abs(this.hashCode()));
        timeZone = TimeZone.getTimeZone("America/New_York");
        sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(timeZone);
        runnable = new Runnable() {
            @Override
            public void run() {
                setText(sdf.format(new Date()));
                handler.postDelayed(runnable, 1000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    public void setTimeZon(TimeZone timeZone) {
        this.timeZone = timeZone;
        sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(timeZone);
    }
    
}
