package com.gaoshin.top;

public class BuiltinApp {
    private String pkg;
    private String activity;
    private int icon;
    private int label;

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public BuiltinApp(String pkg, String activity) {
        this.pkg = pkg;
        this.activity = activity;
    }

    public BuiltinApp() {
    }
}
