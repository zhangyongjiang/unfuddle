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
import com.gaoshin.stock.plugin.ChartPlugin;
import com.gaoshin.stock.plugin.Plugin;

public class StockApplication extends Application {
    private SORMA sorma;
    
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
            group.setName("Untitled");
            sorma.insert(group);
            
            String[] symList = {"DJIA", "NASDAQ", "QQQ", "AAPL", "GOOG"};
            List<GroupItem> items = new ArrayList<GroupItem>();
            for(String sym : symList) {
                GroupItem item = new GroupItem();
                item.setGroupId(group.getId());
                item.setSym(sym);
                items.add(item);
            }
            sorma.insert(items);
            
            Plugin plugin = ReflectionUtil.copy(Plugin.class, new ChartPlugin());
            plugin.setEnabled(true);
            plugin.setSequence(System.currentTimeMillis());
            sorma.insert(plugin);
            plugin.setId(null);
            plugin.setName(plugin.getName()+"2");
            plugin.setSequence(System.currentTimeMillis());
            sorma.insert(plugin);
            
            Plugin yahoo = new Plugin();
            yahoo.setName("Yahoo");
            yahoo.setCountry("US");
            yahoo.setEnabled(true);
            yahoo.setSequence(System.currentTimeMillis());
            yahoo.setUrl("http://m.yahoo.com/w/yfinance/quote/__SYM__");
            sorma.insert(yahoo);
//            sorma.insert(BuiltInPlugins.getChartOneYearPlugin());
//            sorma.insert(BuiltInPlugins.getYahooQuotePlugin());
//            sorma.insert(BuiltInPlugins.getMarketWatchPlugin());
//            sorma.insert(BuiltInPlugins.getCnnMarketMove());
//            sorma.insert(BuiltInPlugins.getYahooPortfolio());
        }
        
        conf.setValue(true);
        confService.save(conf);
    }

    public Display getDisplay() {
        DisplayMetrics myMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay;
    }
}
