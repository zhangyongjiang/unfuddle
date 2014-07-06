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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BroadcastAction;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;

public class PluginWebView extends WebView {
    private static final String TAG = PluginWebView.class.getSimpleName();
    
    private float scale = 0;
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private boolean firstLoad = true;
    private Plugin plugin;
    private Integer symbol;
    private Handler handler;
    private PageStartedListener pageStartedListener;
    private PageFinishedListener pageFinishedListener;
    private ScaleChangedListener scaleChangedListener;
    private Runnable postActionRunnable;
    private boolean newPage = false;
    private PluginParamList paramList;

    public PluginWebView(Context context) {
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
        getSettings().setUseWideViewPort(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
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
    
    public void setSymbol(Integer groupItemId) {
        load(groupItemId, new HashMap<String, String>());
    }

    private void load(Integer groupItemId, Map<String, String> params) {
        if(plugin == null) {
            return;
        }
        this.symbol = groupItemId;
        GroupItem groupItem = sorma.get(GroupItem.class, "id=" + groupItemId, null);
        Map<String, String> finalParams = new HashMap<String, String>();
        for(PluginParameter pp : paramList.getItems()) {
            finalParams.put(pp.getName(), pp.getDefValue());
        }
        finalParams.putAll(params);
            
        String url = plugin.getUrl();
        url = url.replaceAll("__SYM__", groupItem.getSym());
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
        
        loadUrl(url);
    }

    public class GenericWebViewClient extends WebViewClient {
        
        public GenericWebViewClient() {
            postActionRunnable = new Runnable() {
                @Override
                public void run() {
                    newPage = false;
                    String postAction = PluginWebView.this.plugin.getPostAction();
                    if(postAction != null && postAction.trim().length() > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("javascript:").append(JavascriptLibrary.getInstance().getAllFunctions()).append(PluginWebView.this.plugin.getPostAction());
                        String url = sb.toString();
                        loadUrl(url);
                        if(pageFinishedListener != null) {
                            pageFinishedListener.onPageFinished();
                        }
                    }
                    else {
                        if(pageFinishedListener != null) {
                            pageFinishedListener.onPageFinished();
                        }
                    }
                }
            };
        }
        
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            newPage = true;
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
/*            if(firstLoad) {
                Toast.makeText(getContext(), "Double tap for zooming", Toast.LENGTH_LONG).show();
                firstLoad = false;
            }
            if(newPage) {
                newPage = false;
                handler.removeCallbacks(postActionRunnable);
                handler.post(postActionRunnable);
            }*/
        }
    }
    
    public Integer getSymbol() {
        return symbol;
    }

    public class WebViewJavascriptInterface {
        public void exit() {
        }
        
        public String getPlugins() {
            List<Plugin> list = sorma.select(Plugin.class, null, null);
            PluginList pl = new PluginList();
            pl.setList(list);
            return JsonUtil.toJsonString(pl);
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
        
        public void onDomReady() {
            Log.d(TAG, "dom ready");
            newPage = false;
            handler.removeCallbacks(postActionRunnable);
            handler.post(postActionRunnable);
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

    public void setPlugin(Plugin plugin2) {
        this.plugin = plugin2;
        if(plugin.getParamsJson() != null) {
            paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
        }
        else {
            paramList = new PluginParamList();
        }
    }
    
    public Plugin getPlugin() {
        return plugin;
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

    public void refresh() {
        plugin = sorma.get(Plugin.class, "id=" + plugin.getId(), null);
        setPlugin(plugin);
        setSymbol(getSymbol());
    }
    
}
