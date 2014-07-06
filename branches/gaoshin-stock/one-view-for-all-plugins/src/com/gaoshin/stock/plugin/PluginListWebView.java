package com.gaoshin.stock.plugin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BroadcastAction;
import com.gaoshin.stock.ListWrapper;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.StockContentProvider;

public class PluginListWebView extends WebView {
    private static final String TAG = PluginListWebView.class.getSimpleName();
    
    private float scale = 0;
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private String symbol;
    private Handler handler;
    private PageStartedListener pageStartedListener;
    private PageFinishedListener pageFinishedListener;
    private ScaleChangedListener scaleChangedListener;

    public PluginListWebView(Context context) {
        super(context);
        handler = new Handler();
        setId(Math.abs(this.hashCode()));
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
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
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        requestFocus(View.FOCUS_DOWN);
    }
    
    @Override
    public boolean zoomIn() {
        boolean ret = super.zoomIn();
        float newScale = getScale();
        if(scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        return ret;
    }
    
    @Override
    public boolean zoomOut() {
        boolean ret = super.zoomOut();
        float newScale = getScale();
        if(scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        return ret;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        float newScale = getScale();
        if(scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        return b;
    }
    
    public void setSymbol(String symbol) {
        if(symbol.equals(this.symbol)) {
            clearCache(true);
            loadUrl("javascript:refresh()");
        }
        else {
            this.symbol = symbol;
            loadUrl("file:///android_asset/html/index.html");
        }
    }

    public class GenericWebViewClient extends WebViewClient {
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(scale != 0) {
                view.setInitialScale((int) (scale * 100));
            }
            super.onPageStarted(view, url, favicon);
            if(pageStartedListener != null) {
                pageStartedListener.onPageStarted();
            }
        }
        
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            scale = newScale;
            if(scaleChangedListener != null) {
                scaleChangedListener.onScaleChange(newScale);
            }
        }
        
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(getContext(), "Error loading " + symbol + ". " + description, Toast.LENGTH_SHORT).show();
            if(pageFinishedListener != null) {
                pageFinishedListener.onPageFinished();
            }
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(pageFinishedListener != null) {
                pageFinishedListener.onPageFinished();
            }
        }
    }
    
    public class WebViewJavascriptInterface {
        public String getSymbol() {
            return symbol;
        }

        public void exit() {
        }
        
        public String getUrl(String pluginId) {
            Plugin plugin = sorma.get(Plugin.class, "id=" + pluginId, null);
            PluginParamList paramList = null;
            if(plugin.getParamsJson() != null) {
                paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
            }
            else {
                paramList = new PluginParamList();
            }
            
            Map<String, String> finalParams = new HashMap<String, String>();
            for(PluginParameter pp : paramList.getItems()) {
                finalParams.put(pp.getName(), pp.getDefValue());
            }
                
            String url = plugin.getUrl();
            url = url.replaceAll("__SYM__", getSymbol());
            for(PluginParameter pp : paramList.getItems()) {
                String finalValue = finalParams.get(pp.getName());
                if(finalValue == null) {
                    finalValue = "";
                }
                if(url.indexOf("__" + pp.getName() + "__") != -1) {
                    url = url.replaceAll("__" + pp.getName() + "__", finalValue);
                }
                else {
                    if(url.indexOf("?") == -1) {
                        url = url + "?" + pp.getName() + "=" + URLEncoder.encode(finalValue);
                    }
                    else {
                        if(url.endsWith("&")) {
                            url = url + pp.getName() + "=" + URLEncoder.encode(finalValue);
                        }
                        else {
                            url = url + "&" + pp.getName() + "=" + URLEncoder.encode(finalValue);
                        }
                    }
                }
            }
            
            return url;
        }
        
        public String listPlugins() {
            List<Plugin> list = sorma.select(Plugin.class, null, null, "sequence");
            ListWrapper wrapper = new ListWrapper();
            wrapper.setItems(list);
            return JsonUtil.toJsonString(wrapper);
        }
        
        public String listEnabledPlugins() {
            List<Plugin> list = sorma.select(Plugin.class, "enabled=1", null, "sequence");
            ListWrapper wrapper = new ListWrapper();
            wrapper.setItems(list);
            return JsonUtil.toJsonString(wrapper);
        }
        
        public void enableDisable(String pluginId) {
            Plugin p = sorma.get(Plugin.class, "id=" + pluginId, null);
            p.setEnabled(!Boolean.TRUE.equals(p.isEnabled()));
            sorma.update(p);
        }
        
        public boolean deletePlugin(String pluginId) {
            try {
                sorma.delete(Plugin.class, "id="+pluginId, null);
                Intent intent = new Intent(BroadcastAction.PluginDeleted.name());
                intent.putExtra("data", Integer.parseInt(pluginId));
                getContext().sendBroadcast(intent);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
        
        public void adjustOrder(String pluginId, boolean up) {
            try {
                Plugin plugin = sorma.get(Plugin.class, "id=" + pluginId, null);
                List<Plugin> before = null;
                if(up)
                    before = sorma.select(Plugin.class, "sequence<" + plugin.getSequence(), null, "sequence desc");
                else
                    before = sorma.select(Plugin.class, "sequence>" + plugin.getSequence(), null, "sequence");
                if(before.size() == 0) {
                    return;
                }
                long tmp = plugin.getSequence();
                plugin.setSequence(before.get(0).getSequence());
                sorma.update(plugin);
                before.get(0).setSequence(tmp);
                sorma.update(before.get(0));
            }
            catch (Exception e) {
            }
        }
        
        public boolean addPlugin(String json) {
            try {
                Plugin plugin = JsonUtil.toJavaObject(json, Plugin.class);
                sorma.insert(plugin);
                Intent intent = new Intent(BroadcastAction.PluginAdded.name());
                intent.putExtra("data", plugin.getId());
                getContext().sendBroadcast(intent);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
        
    }
    
    private class PluginList {
        private List<Plugin> list = new ArrayList<Plugin>();

        public List<Plugin> getList() {
            return list;
        }

        public void setList(List<Plugin> list) {
            this.list = list;
        }
    }
    
    public PageStartedListener getPageStartedListener() {
        return pageStartedListener;
    }

    public void setPageStartedListener(PageStartedListener pageStartedListener) {
        this.pageStartedListener = pageStartedListener;
    }

    public PageFinishedListener getPageFinishedListener() {
        return pageFinishedListener;
    }

    public void setPageFinishedListener(PageFinishedListener pageFinishedListener) {
        this.pageFinishedListener = pageFinishedListener;
    }

    public ScaleChangedListener getScaleChangedListener() {
        return scaleChangedListener;
    }

    public void setScaleChangedListener(ScaleChangedListener scaleChangedListener) {
        this.scaleChangedListener = scaleChangedListener;
    }

    public void setScale(float newScale) {
        scale = newScale;
    }

}
