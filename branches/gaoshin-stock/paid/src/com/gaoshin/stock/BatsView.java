package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.bats.Bats;
import com.gaoshin.stock.bats.QuoteListener;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.Quote;
import com.gaoshin.stock.plugin.JavascriptLibrary;
import com.gaoshin.web.GaoshinWebView;
import common.android.Resource;

public class BatsView extends GaoshinWebView {
    private Quote quote;
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private QuoteListener quoteListener;
    private Handler handler;

    public BatsView(Context context) {
        super(context);
        handler = new Handler();
        setId(Math.abs(this.hashCode()));
        sorma = SORMA.getInstance(context);
        confService = new ConfigurationServiceImpl(sorma);
        quote = new Quote();
        setupView();
    }
    
    public Quote getQuote() {
        return quote;
    }

    private void setupView() {
        setBackgroundResource(Resource.drawable(getContext(), "white"));
        getBackground().setAlpha(160);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setAllowFileAccess(true);
        WebViewJavascriptInterface javascriptInterface = new WebViewJavascriptInterface();
        addJavascriptInterface(javascriptInterface, "android");
        WebChromeClient chromeClient = new WebChromeClient();
        setWebChromeClient(chromeClient);
        setWebViewClient(new GenericWebViewClient());

        getSettings().setUseWideViewPort(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);
    }
    
    public void setSymbol(String sym) {
        quote = new Quote();
        quote.setSym(sym);
        applyData();
    }

    public class GenericWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getQuote();
                }
            }, 500);
        }
        
        private void getQuote() {
            StringBuilder sb = new StringBuilder();
            sb.append("javascript:");
            sb.append(JavascriptLibrary.getInstance().getFunctions("getElementByTagWithClass"));
//            Javascript.funElementByTagWithClass(sb);
            sb.append("nodes = getElementByTagWithClass('td', 'price');");
            sb.append("for(var i=0;i<nodes.length;i++){if(nodes[i].textContent.trim().length>0){android.priceFound(nodes[i].textContent);break;}};");
            sb.append("var ts = document.getElementById('bkTimestamp').textContent;android.lastUpdateTimeFound(ts);");
            loadUrl(sb.toString());
        }
    }
    
    public class WebViewJavascriptInterface {
        public void exit() {
        }
        
        public void priceFound(String price) {
            try {
                quote.setPrice(Float.parseFloat(price));
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        public void lastUpdateTimeFound(String time) {
            try {
                quote.setLastUpdateTime(time.trim());
                if(quoteListener != null) {
                    quoteListener.quoteChanged();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void toast(String msg) {
            System.out.println(msg);
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }
    
    public void applyData() {
        String url = Bats.getUrl(quote.getSym());
        loadUrl(url);
    }
    
    public void setQuoteListener(QuoteListener listener) {
        quoteListener = listener;
    }
}
