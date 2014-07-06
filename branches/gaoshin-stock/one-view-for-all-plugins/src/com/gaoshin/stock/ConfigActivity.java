package com.gaoshin.stock;

import android.app.Activity;
import android.os.Bundle;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.plugin.Plugin;

public class ConfigActivity extends Activity {
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private Plugin plugin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sorma = SORMA.getInstance(this, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        
        int id = getIntent().getIntExtra("pluginId", -1);
        if(id != -1) {
            plugin = sorma.get(Plugin.class, "id=" + id, null);
        }
        else {
            plugin = new Plugin();
        }
        
        SettingsView view = new SettingsView(this);
        setContentView(view);
    }
    
}
