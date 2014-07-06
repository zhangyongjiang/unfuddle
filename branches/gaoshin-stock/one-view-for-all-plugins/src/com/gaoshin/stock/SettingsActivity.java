package com.gaoshin.stock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.plugin.Plugin;

public class SettingsActivity extends Activity {
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
    
    public class SettingsView extends WebView {

        public SettingsView(Context context) {
            super(context);
            setupView();
        }

        private void setupView() {
            setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            getSettings().setJavaScriptEnabled(true);
            getSettings().setAllowFileAccess(true);
            WebViewJavascriptInterface javascriptInterface = new WebViewJavascriptInterface();
            addJavascriptInterface(javascriptInterface, "android");
            WebChromeClient chromeClient = new WebChromeClient();
            setWebChromeClient(chromeClient);
            setWebViewClient(new GenericWebViewClient());
            getSettings().setBuiltInZoomControls(true);

            getSettings().setUseWideViewPort(true);
            setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            setScrollbarFadingEnabled(true);
            
            loadUrl("file:///android_asset/html/edit_plugin.html");
        }
        
        public class GenericWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }

        public class WebViewJavascriptInterface {
            public void exit() {
                finish();
            }
            
            public void delete() {
                if(plugin.getId() != null) {
                    sorma.delete(plugin);
                    finish();
                }
            }
            
            public String get() {
                return JsonUtil.toJsonString(plugin);
            }
            
            public String save(String strPlugin) {
                try {
                    Plugin save = JsonUtil.toJavaObject(strPlugin, Plugin.class);
                    plugin.setName(save.getName());
                    
                    if(save.getUrl() != null && save.getUrl().trim().length() > 0) {
                        plugin.setUrl(save.getUrl());
                    }
                    
                    if(save.getDescription() != null && save.getDescription().trim().length() > 0) {
                        plugin.setDescription(save.getDescription());
                    }
                    
                    if(plugin.getId() == null) {
                        sorma.insert(plugin);
                    }
                    else {
                        sorma.update(plugin);
                    }
                    return null;
                }
                catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
    }
}
