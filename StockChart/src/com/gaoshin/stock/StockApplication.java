package com.gaoshin.stock;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.reflection.ReflectionUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.OneDayFiveMinutesChartPlugin;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginType;
import com.gaoshin.stock.plugin.SixMonthDailyChartPlugin;

public class StockApplication extends Application {
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private ToastRunnable toastRunnable;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        toastRunnable = new ToastRunnable();
        sorma = SORMA.getInstance(getBaseContext());
        confService = new ConfigurationServiceImpl(sorma);
        seedData();
    }
    
    private void seedData() {
        Configuration conf = confService.get(ConfKey.DataSeeded, false);
        
        if(!conf.getBoolean()) {
            StockGroup group = new StockGroup();
            group.setName("Default Portfolio");
            sorma.insert(group);
            { // current group
                Configuration currGroupConf = new Configuration();
                currGroupConf.setKey(ConfKey.CurrentGroup.name());
                currGroupConf.setValue(group.getId());
                confService.save(currGroupConf);
            }
            
            String[] symList = {"QQQ", "AAPL", "GOOG", "IBM"};
            List<GroupItem> items = new ArrayList<GroupItem>();
            long sequence = System.currentTimeMillis();
            for(String sym : symList) {
                GroupItem item = new GroupItem();
                item.setSequence(sequence);
                sequence += 10;
                item.setGroupId(group.getId());
                item.setSym(sym);
                items.add(item);
            }
            sorma.insert(items);
            
            {
                Plugin setting = new Plugin();
                setting.setName("List View");
                setting.setType(PluginType.Normal.ordinal());
                setting.setEnabled(true);
                setting.setSequence(System.currentTimeMillis());
                setting.setUrl("file:///android_asset/html/list-view/index.html");
                sorma.insert(setting);
            }
            
            if(false){
                Plugin plugin = ReflectionUtil.copy(Plugin.class, new OneDayFiveMinutesChartPlugin());
                plugin.setEnabled(true);
                plugin.setType(PluginType.Normal.ordinal());
                plugin.setSequence(System.currentTimeMillis());
                sorma.insert(plugin);
            }
            
            {
                Plugin yahoo = new Plugin();
                yahoo.setType(PluginType.Normal.ordinal());
                yahoo.setName("Profile View");
                yahoo.setCountry("US");
                yahoo.setEnabled(true);
//                yahoo.setOffsetLeft(0);
//                yahoo.setOffsetTop(130);
                yahoo.setSequence(System.currentTimeMillis());
                yahoo.setUrl("file:///android_asset/html/profile-view/index.html?__SYM__");
                yahoo.setUrl("http://m.yahoo.com/w/yfinance/quote/__SYM__");
                sorma.insert(yahoo);
            }
            
            if(false){
                Plugin plugin = ReflectionUtil.copy(Plugin.class, new SixMonthDailyChartPlugin());
                plugin.setEnabled(true);
                plugin.setType(PluginType.Normal.ordinal());
                plugin.setSequence(System.currentTimeMillis());
                sorma.insert(plugin);
            }

//            sorma.insert(BuiltInPlugins.getYahooQuotePlugin());

            {
                Plugin plugin = ReflectionUtil.copy(Plugin.class, new SixMonthDailyChartPlugin());
                String url = plugin.getUrl();
                int pos = url.indexOf("?");
                
                Plugin setting = new Plugin();
                setting.setName("6M Chart");
                setting.setType(PluginType.Normal.ordinal());
                setting.setEnabled(true);
                setting.setSequence(System.currentTimeMillis());
                setting.setParamsJson(plugin.getParamsJson());
                setting.setUrl("file:///android_asset/html/draw/index.html?" + url.substring(pos + 1));
                sorma.insert(setting);
            }
            
            if(false){
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

    public Configuration getMeta(String keyName, Object defaultValue) {
        Configuration conf = new Configuration();
        conf.setKey(keyName);
        conf.setValue(defaultValue);
        try {
            ApplicationInfo appi = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);

            Bundle bundle = appi.metaData;
            if (bundle != null){
                Object value = bundle.get(keyName);
                if(value != null) {
                    conf.setValue(value);
                }
            }
        }
        catch (Exception ex) {
        }
        return conf;
    }

    public void toast(String msg, boolean longTime) {
        toastRunnable.toast(msg, longTime);
    }
    
    private class ToastRunnable implements Runnable {
        private String msg;
        private boolean longtime;
        private Handler handler;
        
        public ToastRunnable() {
            handler = new Handler();
        }
        
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), msg, longtime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isLongtime() {
            return longtime;
        }

        public void setLongtime(boolean longtime) {
            this.longtime = longtime;
        }
        
        public void toast(String msg, boolean longTime) {
            this.msg = msg;
            this.longtime = longTime;
            handler.removeCallbacks(this);
            handler.postDelayed(this, 1000);
        }
    }
}
