package com.gaoshin.stock.plugin;

import android.content.Context;
import android.widget.LinearLayout;

public class PluginViewContainer extends LinearLayout {

    public PluginViewContainer(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void addView(PluginWebView view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addView(view, lp);
    }
    
    public void setSymbol(Integer groupItemId) {
        for(int i=0; i<getChildCount(); i++) {
            PluginWebView view = (PluginWebView) getChildAt(i);
            view.setSymbol(groupItemId);
        }
    }

    public void refresh() {
        for(int i=0; i<getChildCount(); i++) {
            PluginWebView view = (PluginWebView) getChildAt(i);
            view.refresh();
        }
    }
}
