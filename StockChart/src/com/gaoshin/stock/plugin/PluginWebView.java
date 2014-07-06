package com.gaoshin.stock.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BroadcastAction;
import com.gaoshin.stock.GaoshinMenuItem;
import com.gaoshin.stock.StockApplication;
import com.gaoshin.stock.WebViewJavascriptInterface;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;

public class PluginWebView extends WebView {
    private static final String TAG = PluginWebView.class.getSimpleName();
    private static final List<String> blackList = new ArrayList<String>();
    static {
        blackList.add("teracent.net");
        blackList.add("doubleclick.net");
        blackList.add("wsod.com");
        blackList.add("yieldmanager.net");
        blackList.add("doubleclick.net");
        blackList.add("doubleverify.com");
    };

    private Activity activity;
    private float scale = 0;
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private Plugin plugin;
    private Integer symbolId;
    private String symbol;
    private Handler handler;
    private WebViewEventListener webViewEventListener;
    private ScaleChangedListener scaleChangedListener;
    private boolean newPage = false;
    private PluginParamList paramList;
    private String originalUrl;
    private Runnable scrollRunnable;
    private boolean multiTouching;
    private boolean touching;
    private Long lastTouchDownTime = 0l;
    private boolean allowScrolling = true;

    public PluginWebView(Activity context) {
        super(context);
        this.activity = context;
        handler = new Handler();
        setId(Math.abs(this.hashCode()));
        sorma = SORMA.getInstance(context);
        confService = new ConfigurationServiceImpl(sorma);
        setupView();
    }
    
    public StockApplication getApp() {
        return (StockApplication) activity.getApplication();
    }

    private void setupView() {
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setAllowFileAccess(true);
        WebViewJavascriptInterface javascriptInterface = new WebViewJavascriptInterface() {

            @Override
            protected PluginWebView getWebView() {
                return PluginWebView.this;
            }
            
            public void setAllowScrolling(String allow) {
                try {
                    allowScrolling = Boolean.parseBoolean(allow);
                }
                catch (Exception e) {
                }
            }
            
            public void showLoading() {
                if (webViewEventListener != null) {
                    webViewEventListener.onPageStarted(plugin.getId());
                }
            }
            
            public void hideLoading() {
                if (webViewEventListener != null) {
                    webViewEventListener.onPageFinished(plugin.getId());
                }
            }
            
            public String isAllowScrolling() {
                return allowScrolling+"";
            }

            public void config() {
                Intent intent = new Intent(BroadcastAction.EditPlugin.name());
                intent.putExtra("pluginId", plugin.getId());
                getContext().sendBroadcast(intent);
            }

            public void onDomReady() {
                postAction();
            }

            public boolean isMultiTouching() {
                return multiTouching;
            }

            public boolean isTouching() {
                return touching;
            }

            public void save() {
                PluginWebView.this.save(capturePicture());
            }
            
            public void setCurrentGroupItem(String groupItemId) {
                load(Integer.parseInt(groupItemId), new HashMap<String, String>(), false);
                super.setCurrentGroupItem(groupItemId);
            }
            
            public String getPluginId() {
                return plugin.getId().toString();
            }

        };
        addJavascriptInterface(javascriptInterface, "android");
        WebChromeClient chromeClient = new WebChromeClient();
        setWebChromeClient(chromeClient);
        setWebViewClient(new GenericWebViewClient());
        clearCache(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(false);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
        setAlwaysDrawnWithCacheEnabled(false);
        setInitialScale(0);
        requestFocus(View.FOCUS_DOWN);

        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadUrl("javascript:try{onLongClick();}catch(e){}");
                // Intent intent = new
                // Intent(BroadcastAction.FullScreen.name());
                // getContext().sendBroadcast(intent);
                return true;
            }
        });

        scrollRunnable = new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:try{onScroll();}catch(e){}");
            }
        };
    }

    @Override
    public boolean zoomIn() {
        boolean ret = super.zoomIn();
        float newScale = getScale();
        if (scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        handler.removeCallbacks(scrollRunnable);
        handler.postDelayed(scrollRunnable, 300);
        return ret;
    }

    @Override
    public boolean zoomOut() {
        boolean ret = super.zoomOut();
        float newScale = getScale();
        if (scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        handler.removeCallbacks(scrollRunnable);
        handler.postDelayed(scrollRunnable, 300);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == (MotionEvent.ACTION_MOVE & MotionEvent.ACTION_MASK)) {
            this.multiTouching = ev.getPointerCount() > 1;
            this.touching = true;
        }
        else {
            this.multiTouching = false;
            this.touching = false;
        }

        if (ev.getAction() == (MotionEvent.ACTION_DOWN & MotionEvent.ACTION_MASK)) {
            long now = System.currentTimeMillis();
            long diff = now - lastTouchDownTime;
            if (diff < 400) {
                loadUrl("javascript:try{onDblClick();}catch(e){}");
            }
            lastTouchDownTime = now;
        }

        boolean b = super.onTouchEvent(ev);
        float newScale = getScale();
        if (scaleChangedListener != null) {
            scaleChangedListener.onScaleChange(newScale);
        }
        scale = newScale;
        newPage = false;
        return b;
    }

    public void setSymbol(Integer groupItemId) {
        load(groupItemId, new HashMap<String, String>(), true);
    }

    public void load(Integer groupItemId, Map<String, String> params, boolean load) {
        if (plugin == null) {
            return;
        }
        if(groupItemId == symbolId && originalUrl != null) {
            return;
        }
        
        newPage = true;
        this.symbolId = groupItemId;
        GroupItem groupItem = sorma.get(GroupItem.class, "id=" + groupItemId, null);
        this.symbol = groupItem.getSym();
        Map<String, String> finalParams = new HashMap<String, String>();
        for (PluginParameter pp : paramList.getItems()) {
            finalParams.put(pp.getName(), pp.getDefValue());
        }
        finalParams.putAll(params);

        String url = plugin.getUrl();
        url = url.replaceAll("__SYM__", groupItem.getSym());
        for (PluginParameter pp : paramList.getItems()) {
            String finalValue = finalParams.get(pp.getName());
            if (finalValue == null) {
                finalValue = "";
            }
            if (url.indexOf("__" + pp.getName() + "__") != -1) {
                url = url.replaceAll("__" + pp.getName() + "__", finalValue);
            }
            else {
                if (url.indexOf("?") == -1) {
                    url = url + "?" + pp.getName() + "=" + URLEncoder.encode(finalValue);
                }
                else {
                    if (url.endsWith("&")) {
                        url = url + pp.getName() + "=" + URLEncoder.encode(finalValue);
                    }
                    else {
                        url = url + "&" + pp.getName() + "=" + URLEncoder.encode(finalValue);
                    }
                }
            }
        }

        boolean sameUrl = url.equals(originalUrl);
        originalUrl = url;
        
        if(load || !sameUrl)
            loadUrl(url); 
        else 
            newPage = false;
    }

    public class GenericWebViewClient extends WebViewClient {
        private Runnable postRunnable;

        public GenericWebViewClient() {
            postRunnable = new Runnable() {
                @Override
                public void run() {
                    if (newPage) {
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
            if (scale != 0) {
                view.setInitialScale((int) (scale * 100));
            }
            super.onPageStarted(view, url, favicon);
            if (webViewEventListener != null) {
                webViewEventListener.onPageStarted(plugin.getId());
            }
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            scale = newScale;
            if (scaleChangedListener != null) {
                scaleChangedListener.onScaleChange(newScale);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (webViewEventListener != null) {
                webViewEventListener.onPageFinished(plugin.getId());
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webViewEventListener != null) {
                webViewEventListener.onPageFinished(plugin.getId());
            }
            postAction();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            for (String block : blackList) {
                if (url.indexOf(block) == -1) {
                    super.onLoadResource(view, url);
                }
            }
        }
    }

    private void postAction() {
        if (!newPage) {
            return;
        }
        newPage = false;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollTo(plugin.getOffsetLeft(), plugin.getOffsetTop());
            }
        }, 300);

        if (false) {
            try {
                String script = "";
                if (plugin.getPostAction() != null) {
                    script += " try {" + plugin.getPostAction() + "} catch(e){android.log(e);}";
                }
                else if (plugin.getType() != PluginType.System.ordinal()) {
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
                if (script.length() > 0) {
                    loadUrl("javascript:" + JavascriptLibrary.getInstance().getAllFunctions() + ";" + script);
                }
            }
            catch (Exception e) {
            }
        }

        try {
            String url = plugin.getUrl();
            if (plugin.getType() != PluginType.System.ordinal()) {
                String[] host = url.split("/");
                host = host[2].split("\\.");
                String src = "    IFrame Source: " + host[host.length - 2] + "." + host[host.length - 1];
                url.split("/");
                loadUrl("javascript:"
                        // + JavascriptLibrary.jsCreateElement("iframesource",
                        // "div", src, "style",
                        // "clear:both;text-align:left;margin-top:10px;")
                        // +
                        // "var head= document.getElementsByTagName('body')[0];head.appendChild(iframesource);"
                        + JavascriptLibrary.jsCreateElement("inject", "script", null, "src", "file:///android_asset/script/inject_script.js")
                        + "var head= document.getElementsByTagName('head')[0];head.appendChild(inject);");
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
        if (plugin.getParamsJson() != null) {
            paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
        }
        else {
            paramList = new PluginParamList();
        }
        originalUrl = null;
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

    public boolean isLoading() {
        return newPage;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        handler.removeCallbacks(scrollRunnable);
        handler.postDelayed(scrollRunnable, 300);
    }
    
    private void save(final Picture picture) {
        if (webViewEventListener != null) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    webViewEventListener.onPageStarted(plugin.getId());
                }
            });
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
                    String date = sdf.format(new Date());
                    String fileName = symbol + "_" + plugin.getName() + "_" + date + ".png";

                    int w = picture.getWidth();
                    int h = picture.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    picture.draw(canvas);

                    String path = "/mnt/sdcard/Android/data/com.gaoshin.stock/";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    path += fileName;
                    OutputStream baos = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    baos.close();
                    return path;
                }
                catch (Exception e) {
                    getApp().toast("Failed: " + e.getMessage(), true);
                }
                return null;
            }

            protected void onPostExecute(final String path) {
                if (webViewEventListener != null) {
                    webViewEventListener.onPageFinished(plugin.getId());
                }
                new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Chart saved. Share it with your friends?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/png");
                        share.putExtra(Intent.EXTRA_STREAM,
                          Uri.parse("file://" + path));
                        share.putExtra("android.intent.extra.TEXT", "- Sent from Gaoshin Stock Chart Android Application.");
                        share.putExtra(android.content.Intent.EXTRA_SUBJECT, path);
                        getContext().startActivity(Intent.createChooser(share, "Share Image"));
                    }
                })
                .setNegativeButton("No", null)
                .show();
            };
        }.execute(null);
    }
    
    @Override
    public void scrollTo(int x, int y) {
        Log.d(TAG, "scrollTo");
        if(allowScrolling) {
            super.scrollTo(x, y);
        }
    }
    
    @Override
    public void scrollBy(int x, int y) {
        Log.d(TAG, "scrollBy");
        if(allowScrolling) {
            super.scrollBy(x, y);
        }
    }

    public void onMenu(GaoshinMenuItem menu) {
        loadUrl("javascript:try {onMenu(" + menu.getId() + ", '" + menu.getLabel() + "');} catch (e) {android.log(e);}");
    }

    public void setActive(boolean b) {
        loadUrl("javascript:try {setActive(" + b + ")} catch (e) {android.log(e);}");
    }
}
