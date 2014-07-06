package com.gaoshin.stock;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.reflection.ReflectionUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.BuiltInPlugins;
import com.gaoshin.stock.plugin.OneDayFiveMinutesChartPlugin;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginType;
import com.gaoshin.stock.plugin.SixMonthDailyChartPlugin;

public class StockApplication extends Application {
    private SORMA sorma;
    private ViewMode viewMode;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        sorma = SORMA.getInstance(getBaseContext(), StockContentProvider.class);
        seedData();
    }
    
    private void seedData() {
        ConfigurationServiceImpl confService = new ConfigurationServiceImpl(sorma);
        Configuration conf = confService.get(ConfKey.DataSeeded, false);
        
        if(!conf.getBoolean()) {
            StockGroup group = new StockGroup();
            group.setName("Default Portfolio");
            sorma.insert(group);
            
            String[] symList = {"QQQ", "AAPL", "GOOG", "IBM"};
            List<GroupItem> items = new ArrayList<GroupItem>();
            for(String sym : symList) {
                GroupItem item = new GroupItem();
                item.setGroupId(group.getId());
                item.setSym(sym);
                items.add(item);
            }
            sorma.insert(items);
            
            {
                Plugin plugin = ReflectionUtil.copy(Plugin.class, new OneDayFiveMinutesChartPlugin());
                plugin.setEnabled(true);
                plugin.setType(PluginType.Normal.ordinal());
                plugin.setSequence(System.currentTimeMillis());
                sorma.insert(plugin);
            }
            
            {
                Plugin plugin = ReflectionUtil.copy(Plugin.class, new SixMonthDailyChartPlugin());
                plugin.setEnabled(true);
                plugin.setType(PluginType.Normal.ordinal());
                plugin.setSequence(System.currentTimeMillis());
                sorma.insert(plugin);
            }
            
            {
                Plugin yahoo = new Plugin();
                yahoo.setType(PluginType.Normal.ordinal());
                yahoo.setName("Yahoo Finance");
                yahoo.setCountry("US");
                yahoo.setEnabled(true);
                yahoo.setSequence(System.currentTimeMillis());
                yahoo.setUrl("http://m.yahoo.com/w/yfinance/quote/__SYM__");
                sorma.insert(yahoo);
            }

            sorma.insert(BuiltInPlugins.getYahooQuotePlugin());
            
            {
                Plugin setting = new Plugin();
                setting.setName("Settings");
                setting.setType(PluginType.System.ordinal());
                setting.setEnabled(true);
                setting.setSequence(Long.MAX_VALUE);
                setting.setUrl("file:///android_asset/html/settings.html");
                sorma.insert(setting);
            }
        }
        
        conf.setValue(true);
        confService.save(conf);
    }

    public Display getDisplay() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay;
    }
    
    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics myMetrics = new DisplayMetrics();
        getDisplay().getMetrics(myMetrics);
        return myMetrics;
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
    }
    
    public ViewMode getViewMode() {
        return viewMode;
    }
}
