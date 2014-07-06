package com.gaoshin.stock.plugin;

import java.net.URLEncoder;
import java.util.HashMap;
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
import com.gaoshin.stock.WebViewJavascriptInterface;
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
    private Integer symbolId;
    private String symbol;
    private Handler handler;
    private WebViewEventListener webViewEventListener;
    private ScaleChangedListener scaleChangedListener;
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
        WebViewJavascriptInterface javascriptInterface = new WebViewJavascriptInterface(){
            @Override
            protected Context getContext() {
                return PluginWebView.this.getContext();
            }
            
            public void config(){
                Intent intent = new Intent(BroadcastAction.EditPlugin.name());
                intent.putExtra("pluginId", plugin.getId());
                getContext().sendBroadcast(intent);
            }
            
            public void onDomReady() {
                postAction();
            }
        };
        addJavascriptInterface(javascriptInterface, "android");
        WebChromeClient chromeClient = new WebChromeClient();
        setWebChromeClient(chromeClient);
        setWebViewClient(new GenericWebViewClient());
        getSettings().setBuiltInZoomControls(true);
        getSettings().setUseWideViewPort(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
        requestFocus(View.FOCUS_DOWN);
        
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(BroadcastAction.FullScreen.name());
                getContext().sendBroadcast(intent);
                return true;
            }
        });
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
        newPage = false;
        return b;
    }
    
    public void setSymbol(Integer groupItemId) {
        load(groupItemId, new HashMap<String, String>());
    }

    private void load(Integer groupItemId, Map<String, String> params) {
        if(plugin == null) {
            return;
        }
        this.symbolId = groupItemId;
        GroupItem groupItem = sorma.get(GroupItem.class, "id=" + groupItemId, null);
        this.symbol = groupItem.getSym();
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
        private Runnable postRunnable;

        public GenericWebViewClient() {
            postRunnable = new Runnable() {
                @Override
                public void run() {
                    if(newPage) {
                        loadUrl("javascript:try{if((typeof android != 'undefined') && 'loaded|complete'.indexOf(document.readyState) != -1){android.onDomReady();}}catch(e){android.log(e);}");
                        handler.postDelayed(postRunnable, 100);
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
            Log.d(TAG, "onPageStarted: " + url); 
            newPage = true;
            if(scale != 0) {
                view.setInitialScale((int) (scale * 100));
            }
            super.onPageStarted(view, url, favicon);
            if(webViewEventListener != null) {
                webViewEventListener.onPageStarted(plugin.getId());
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
            Toast.makeText(getContext(), "Error loading " + symbolId + ". " + description, Toast.LENGTH_SHORT).show();
            if(webViewEventListener != null) {
                webViewEventListener.onPageFinished(plugin.getId());
            }
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(webViewEventListener != null) {
                webViewEventListener.onPageFinished(plugin.getId());
            }
            postAction();
        }
        
        @Override
        public void onLoadResource(WebView view, String url) {
//            if(newPage && !url.equals(getUrl())) {
//                postAction();
//            }
            super.onLoadResource(view, url);
        }
    }
    
    private void postAction() {
        if(!newPage) {
            return;
        }
        newPage = false;
        try {
            String script = "";
            if(plugin.getPostAction() != null) {
                script += " try {" + plugin.getPostAction() + "} catch(e){android.log(e);}";
            }
            else if(plugin.getType() != PluginType.System.ordinal()) {
                script += " try {" +
                "if(document.getElementById('gaoshin_header') == null ) { " +
                "var head= document.getElementsByTagName('body')[0];" +
                "var link= document.createElement('a');" +
                "link.setAttribute('id', 'gaoshin_header');" +
                "link.setAttribute('style', 'margin-left:10px;text-decoration:none;');" +
                "link.setAttribute('href', 'javascript:void(0)');" +
                "link.setAttribute('onClick', 'android.config()');" +
                "link.innerHTML = '" + plugin.getName() + "';" +
                "head.insertBefore(link, head.firstChild);" +
                "}} catch (e) {android.log(e);}";
            }
            if(script.length() > 0) {
                loadUrl("javascript:" + JavascriptLibrary.getInstance().getAllFunctions() + ";" + script);
            }
        }
        catch (Exception e) {
        }
        
        try {
            String url = plugin.getUrl();
            if(plugin.getType() != PluginType.System.ordinal()) {
                String[] host = url.split("/");
                host = host[2].split("\\.");
                String src = "    IFrame Source: " + host[host.length-2] + "." + host[host.length-1];
                url.split("/");
                loadUrl("javascript:" +
                        JavascriptLibrary.jsCreateElement("iframesource", "div", src, "style", "clear:both;text-align:left;margin-top:10px;") +
                		"var head= document.getElementsByTagName('body')[0];head.appendChild(iframesource);");
            }
        }
        catch (Exception e) {
        }
    }
    
    public Integer getSymbol() {
        return symbolId;
    }

    public WebViewEventListener getWebViewEventListener() {
        return webViewEventListener;
    }

    public void setWebViewEventListener(WebViewEventListener webViewEventListener) {
        this.webViewEventListener = webViewEventListener;
    }

    public void setPlugin(Plugin plugin2) {
        this.plugin = plugin2;
        if(plugin.getParamsJson() != null) {
            paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
        }
        else {
            paramList = new PluginParamList();
        }
        getSettings().setUseWideViewPort(plugin.getType() != PluginType.System.ordinal());
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

    public void editPlugin(int pluginId) {
        loadUrl("file:///android_asset/html/plugin.html?" + pluginId);
    }
    
}
