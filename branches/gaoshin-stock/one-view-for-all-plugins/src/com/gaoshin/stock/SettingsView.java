package com.gaoshin.stock;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginParamList;
import com.gaoshin.stock.plugin.PluginParameter;

public class SettingsView extends WebView {
    public static interface SettingListener {
        void exit();
        void addSymbol();
        void addGroup();
        void removeGroup();
        void renameGroup();
        void selectGroup(Integer groupId);
    }
    
    private SettingListener exitListener;
    private SORMA sorma;
    
    public SettingsView(Context context) {
        super(context);
        sorma = SORMA.getInstance(getContext(), StockContentProvider.class); 
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
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
        
        loadUrl("file:///android_asset/html/settings.html");
    }
    
    public class GenericWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class WebViewJavascriptInterface {
        public void email(String to, String subject, String msg) {
            String action = "android.intent.action.SENDTO";
            Uri uri = Uri.parse("mailto:" + to);
            Intent intent = new Intent(action, uri);
            intent.putExtra("android.intent.extra.TEXT", msg);
            getContext().startActivity(intent);
        }
        
        public String getCurrentGroup() {
            Configuration conf = sorma.get(Configuration.class, "_key=?", new String[]{ConfKey.CurrentGroup.name()});
            if(conf == null) {
                return null;
            }
            StockGroup current = sorma.get(StockGroup.class, "id=" + conf.getValue(), null);
            return JsonUtil.toJsonString(current);
        }
        
        public void exit() {
            if(exitListener != null) {
                exitListener.exit();
            }
        }
        
        public void rename(String pluginId, String name) {
            Plugin plugin = sorma.get(Plugin.class, "id=" + pluginId, null);
            if(plugin != null) {
                plugin.setName(name);
                sorma.update(plugin);
            }
        }
        
        public void addSymbol() {
            if(exitListener != null) {
                exitListener.addSymbol();
            }
        }
        
        public void addGroup() {
            if(exitListener != null) {
                exitListener.addGroup();
            }
        }
        
        public void renameGroup() {
            if(exitListener != null) {
                exitListener.renameGroup();
            }
        }
        
        public void removeGroup() {
            if(exitListener != null) {
                exitListener.removeGroup();
            }
        }
        
        public void selectGroup(String groupId) {
            if(exitListener != null) {
                exitListener.selectGroup(Integer.parseInt(groupId));
            }
        }
        
        public String getGroups() {
            List<StockGroup> list = sorma.select(StockGroup.class, null, null);
            ListWrapper wrapper = new ListWrapper();
            wrapper.setItems(list);
            return JsonUtil.toJsonString(wrapper);
        }
        
        public String saveAs(String pluginId, String name, String setting) {
            Plugin plugin = sorma.get(Plugin.class, "id=" + pluginId, null);
            if(plugin.getName().equals(name)) {
                return null;
            }
            plugin.setId(null);
            plugin.setName(name);
            sorma.insert(plugin);
            saveChartSetting(String.valueOf(plugin.getId()), setting);
            return String.valueOf(plugin.getId());
        }
        
        public boolean saveChartSetting(String pluginId, String setting) {
            try {
                Plugin plugin = sorma.get(Plugin.class, "id=" + pluginId, null);
                MapWrapper map = JsonUtil.toJavaObject(setting, MapWrapper.class);
                PluginParamList paramList;
                if(plugin.getParamsJson() != null) {
                    paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
                }
                else {
                    paramList = new PluginParamList();
                }
                for(PluginParameter pp : paramList.getItems()) {
                    pp.setDefValue((String) map.getMap().get(pp.getName()));
                }
                plugin.setParamsJson(JsonUtil.toJsonString(paramList));
                SORMA sorma = SORMA.getInstance(getContext(), StockContentProvider.class); 
                sorma.update(plugin);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        public String listPlugins() {
            List<Plugin> list = sorma.select(Plugin.class, null, null, "enabled desc, country, lower(name)");
            ListWrapper wrapper = new ListWrapper();
            wrapper.setItems(list);
            return JsonUtil.toJsonString(wrapper);
        }
        
        public String getPlugin(String pluginId) {
            Plugin p = sorma.get(Plugin.class, "id=" + pluginId, null);
            return JsonUtil.toJsonString(p);
        }
        
        public void enableDisable(String pluginId) {
            Plugin p = sorma.get(Plugin.class, "id=" + pluginId, null);
            p.setEnabled(!Boolean.TRUE.equals(p.isEnabled()));
            sorma.update(p);
        }
        
        public void deletePlugin(String pluginId) {
            Plugin p = sorma.get(Plugin.class, "id=" + pluginId, null);
            sorma.delete(p);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            if(this.canGoBack()) {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public SettingListener getExitListener() {
        return exitListener;
    }

    public void setExitListener(SettingListener exitListener) {
        this.exitListener = exitListener;
    }
}
