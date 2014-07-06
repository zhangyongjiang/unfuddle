package com.gaoshin.stock.plugin;

import android.app.Activity;
import android.widget.RelativeLayout;

public class MultiWebView extends RelativeLayout {
    private PluginWebView master;
    private PluginWebView slave;

    public MultiWebView(Activity context) {
        super(context);
        
        master = new PluginWebView(context);
        master.setId(Math.abs(master.hashCode()));
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(master);
        
        slave = new PluginWebView(context);
        slave.setId(Math.abs(slave.hashCode()));
        LayoutParams lpslave = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lpslave.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lpslave.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpslave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpslave.addRule(RelativeLayout.ABOVE, master.getId());
        addView(slave);
        
    }

}
