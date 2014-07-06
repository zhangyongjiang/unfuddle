package com.gaoshin.stock.plugin;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.BroadcastAction;
import com.gaoshin.stock.ChartBrowser;
import com.gaoshin.stock.LoadingView;
import com.gaoshin.stock.MotionEventTracker;
import com.gaoshin.stock.MyScrollView;
import com.gaoshin.stock.R;
import com.gaoshin.stock.ViewMode;
import com.gaoshin.stock.YahooQuoteView;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;

public class PluginViewContainer extends RelativeLayout implements WebViewEventListener {
    private SORMA sorma;
    private List<Plugin> plugins;
    private int current;
    private LinearLayout container;
    private ChartBrowser app;
    private MotionEventTracker tracker;
    private Handler handler;
    private Integer groupItemId;
    private HorizontalScrollView scrollView;
    private Button prev;
    private Button next;
    private Runnable hideNavi;
    private YahooQuoteView quoteView;
    private LoadingView loadingView;
    private List<PluginWebView> webViewList = new ArrayList<PluginWebView>();

    public PluginViewContainer(ChartBrowser context) {
        super(context.getBaseContext());
        this.app = context;
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        
        tracker = new MotionEventTracker();
        handler = new Handler();
        
        setupScrollView();
        setupContainer();
        setupNaviButtons();
//        setupQuoteView();
        setupLoadingView();
        
        hideNavi = new Runnable() {
            @Override
            public void run() {
                prev.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
        };
    }

    private void setupLoadingView() {
        loadingView = new LoadingView(getContext());
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loadingView, param);
    }

    private void setupQuoteView() {
        quoteView = new YahooQuoteView(getContext());
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(quoteView, param);
    }

    private void setupNaviButtons() {
        {
            prev = new Button(app);
            Drawable d = getResources().getDrawable(R.drawable.prev);
            d.setAlpha(128);
            prev.setBackgroundDrawable(d);
            RelativeLayout.LayoutParams param = new LayoutParams(60, 60);
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            param.addRule(RelativeLayout.CENTER_VERTICAL);
            addView(prev, param);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prev();
                }
            });
            prev.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectPlugin(0);
                    return true;
                }
            });
        }
        {
            next = new Button(app);
            Drawable d = getResources().getDrawable(R.drawable.next);
            d.setAlpha(128);
            next.setBackgroundDrawable(d);
            RelativeLayout.LayoutParams param = new LayoutParams(60, 60);
            param.addRule(RelativeLayout.CENTER_VERTICAL);
            param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addView(next, param);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next();
                }
            });
            next.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectPlugin(-1);
                    return true;
                }
            });
        }
    }

    private void setupContainer() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        scrollView.addView(container, new HorizontalScrollView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    private void setupScrollView() {
        scrollView = new MyScrollView(getContext());
        scrollView.setHorizontalScrollBarEnabled(false);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(scrollView, param);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        showNavi();
        handler.removeCallbacks(hideNavi);
        handler.postDelayed(hideNavi, 2000);
        return super.onInterceptTouchEvent(ev);
    }
    
    private int getWebViewWidth() {
        DisplayMetrics metrics = app.getApp().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        if(ViewMode.FullScreen.equals(app.getApp().getViewMode())) {
            return width;
        }
        else {
            return width - 60;
        }
    }
    
    public void applyData(int current) {
        plugins = sorma.select(Plugin.class, "enabled=1", null, "sequence");
        if(current < 0) {
            current = plugins.size() - 1;
        }
        
        container.removeAllViews();
        webViewList.clear();
        
        this.current = current;
        showNavi();
        
        for(Plugin plugin : plugins) {
            PluginWebView pwv = new PluginWebView(app);
            webViewList.add(pwv);
            pwv.setPlugin(plugin);
            pwv.setWebViewEventListener(this);
            if(groupItemId != null) {
                pwv.setSymbol(groupItemId);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWebViewWidth(), LinearLayout.LayoutParams.FILL_PARENT);
            container.addView(pwv, lp);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(PluginViewContainer.this.current * getWebViewWidth(), 0);
            }
        }, 200);
    }
    
    public void setSymbol(Integer groupItemId) {
        this.groupItemId = groupItemId;
        if(groupItemId != null) {
            GroupItem groupItem = sorma.get(GroupItem.class, "id=" + groupItemId, null);
            if(quoteView != null) {
                quoteView.setSymbol(groupItem.getSym());
            }
            webViewList.get(current).setSymbol(groupItemId);
            
            new AsyncTask<Integer, Integer, Integer>() {

                @Override
                protected Integer doInBackground(Integer... params) {
                    return params[0];
                }
                
                protected void onPostExecute(Integer itemId) {
                    for(int i=0; i<container.getChildCount(); i++) {
                        PluginWebView view = (PluginWebView) container.getChildAt(i);
                        if(view.getSymbol() != itemId) {
                            view.setSymbol(itemId);
                        }
                    }
                }
            }.execute(groupItemId);
        }
    }
    
    private void showNavi() {
        if(current != 0 && plugins.size() > 1) {
            prev.setVisibility(View.VISIBLE);
        }
        else {
            prev.setVisibility(View.GONE);
        }
        
        if(current != (plugins.size() - 1) && plugins.size() > 1) {
            next.setVisibility(View.VISIBLE);
        }
        else {
            next.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        applyData(0);
    }

    public void prev() {
        if(current == 0) {
            return;
//            current = plugins.size() - 1;
        }
        else {
            current --;
        }
        showNavi();
        scrollView.smoothScrollTo(current * getWebViewWidth(), 0);
        firePluginSwitched();
    }

    public void next() {
        if(current == (plugins.size()-1)) {
            return;
//            current = 0;
        }
        else {
            current ++;
        }
        showNavi();
        scrollView.smoothScrollTo(current * getWebViewWidth(), 0);
        firePluginSwitched();
    }

    public void adjustWidth() {
        for(int i=0; i<container.getChildCount(); i++) {
            PluginWebView view = (PluginWebView) container.getChildAt(i);
            android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = getWebViewWidth();
            view.setLayoutParams(params);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(current * getWebViewWidth(), 0);
            }
        }, 400);
    }
    
    @Override
    public void onPageStarted(int pluginId) {
        if(webViewList.get(current).getPlugin().getId() == pluginId) {
            loadingView.start();
        }
    }

    @Override
    public void onPageFinished(int pluginId) {
        if(webViewList.get(current).getPlugin().getId() == pluginId) {
            loadingView.stop();
        }
    }

    public boolean canGoBack() {
        if(plugins.size() == 0) {
            return false;
        }
        boolean can = webViewList.get(current).canGoBack();
        return can || (current != 0 && current == (plugins.size()-1));
    }

    public void goBack() {
        if(plugins.size() == 0) {
            return;
        }
        if(webViewList.get(current).canGoBack()) {
            webViewList.get(current).goBack();
        }
        else if (current != 0 && current == (plugins.size()-1)) {
            selectPlugin(0);
        }
    }

    public void selectPlugin(int index) {
        int old = current;
        if(index >= 0 && index < plugins.size()) {
            current = index;
        }
        else {
            current = plugins.size() - 1;
        }
        if(old != current) {
            showNavi();
            scrollView.smoothScrollTo(current * getWebViewWidth(), 0);
            firePluginSwitched();
        }
    }
    
    private void firePluginSwitched() {
        Intent intent = new Intent(BroadcastAction.PluginSwitched.name());
        getContext().sendBroadcast(intent);
    }

    public int size() {
        return plugins.size();
    }

    public int current() {
        return current;
    }

    public Plugin getCurrentPlugin() {
        return plugins.get(current);
    }

    public void pluginSwitched() {
        Plugin plugin = getCurrentPlugin();
        if(quoteView != null) {
            if(plugin.getUrl().indexOf("__SYM__") == -1) {
                quoteView.setVisibility(View.GONE);
            }
            else {
                quoteView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void selectPluginByPluginId(int pluginId) {
        for(int i=0; i<plugins.size(); i++) {
            if(pluginId == plugins.get(i).getId()) {
                webViewList.get(i).refresh();
                selectPlugin(i);
                break;
            }
        }
    }

    public void editPlugin(int pluginId) {
        selectPlugin(-1);
        webViewList.get(current).editPlugin(pluginId);
    }
    
}
