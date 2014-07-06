package com.gaoshin.stock.plugin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.StockContentProvider;

public class PluginListView extends LinearLayout {
    private SORMA sorma;
    private List<PluginView> pluginViewList = new ArrayList<PluginView>();
    private int current = 0;

    public PluginListView(Context context) {
        super(context);
        setBackgroundColor(Color.LTGRAY);
        setId(Math.abs(hashCode()));
        sorma = SORMA.getInstance(context);
        setOrientation(HORIZONTAL);
    }

    public void applyData() {
        pluginViewList.clear();
        removeAllViews();
        List<Plugin> plugins = sorma.select(Plugin.class, null, null);
        for(Plugin plugin : plugins) {
            PluginView pv = new PluginView(getContext(), plugin);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 40);
            lp.setMargins(3, 0, 3, 0);
            addView(pv, lp);
            pluginViewList.add(pv);
        }
    }
    
    public Plugin getCurrent() {
        return pluginViewList.get(this.current).getPlugin();
    }

    public void setCurrent(int current) {
        pluginViewList.get(this.current).setSelected(false);
        this.current = current;
        pluginViewList.get(this.current).setSelected(true);
    }
    
    public int getPluginSize() {
        return pluginViewList.size();
    }

    public void setCurrent(Plugin plugin) {
        for(int i=0; i<pluginViewList.size(); i++) {
            if(pluginViewList.get(i).getPlugin().getId() == plugin.getId()) {
                setCurrent(i);
                break;
            }
        }
    }

    public void onPluginDeleted(int pluginId) {
        for(int i=0; i<pluginViewList.size(); i++) {
            PluginView view = pluginViewList.get(i);
            if(view.getPlugin().getId() == pluginId) {
                if(current == i) {
                    if(current == pluginViewList.size() - 1) {
                        current--;
                    }
                }
                
                removeView(view);
                pluginViewList.remove(i);
                
                setCurrent(current);
                break;
            }
        }
    }

    public void addPlugin(Plugin plugin) {
        PluginView pv = new PluginView(getContext(), plugin);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 40);
        lp.setMargins(3, 0, 3, 0);
        addView(pv, lp);
        pluginViewList.add(pv);
        setCurrent(plugin);
    }
}
