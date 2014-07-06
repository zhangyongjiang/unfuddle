package com.gaoshin.top;

import android.graphics.drawable.Drawable;

public class App {
    private String label;
    private String pkgName;
    private String activityName;
    private Drawable icon;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public boolean isActivity(String pkgName, String activityName) {
        return pkgName.equals(this.pkgName) && activityName.equals(this.activityName);
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o.getClass().equals(App.class)))
            return false;
        App app = (App) o;
        return activityName.equals(app.getActivityName());
    }
}
