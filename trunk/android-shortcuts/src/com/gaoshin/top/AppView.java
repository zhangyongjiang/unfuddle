package com.gaoshin.top;

import android.content.Context;

public class AppView extends LabeledImage {
    protected App app;

    public AppView(Context context, App app) {
        super(context, app.getIcon(), app.getLabel());
        this.app = app;
    }

    protected int getLines() {
        return 2;
    }

    public void setApp(App app) {
        this.app = app;
        setDrawable(app.getIcon());
        setCaption(app.getLabel());
        updateView();
    }

    public App getApp() {
        return app;
    }
}
