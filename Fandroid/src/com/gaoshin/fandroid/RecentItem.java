package com.gaoshin.fandroid;

import android.app.PendingIntent;
import android.graphics.Bitmap;

public class RecentItem {
    private String text;
    private Bitmap icon;
    private PendingIntent intent;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIntent(PendingIntent intent) {
        this.intent = intent;
    }

    public PendingIntent getIntent() {
        return intent;
    }
}
