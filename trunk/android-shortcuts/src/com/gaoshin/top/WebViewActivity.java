package com.gaoshin.top;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends ShortcutActivity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        web = new WebView(this);
        setContentView(web);
        
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.addJavascriptInterface(new WebViewJavascriptInterface(), "Device");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new GenericWebViewClient());
        
        Intent intent = getIntent();
        String url = intent.getData().toString();
        web.loadUrl(url);
    }

    public class GenericWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    }

    public class WebViewJavascriptInterface {
        public void exit() {
            finish();
        }

        public void email(String to, String subject, String msg) {
            String action = "android.intent.action.SENDTO";
            Uri uri = Uri.parse("mailto:" + to);
            Intent intent = new Intent(action, uri);
            intent.putExtra("android.intent.extra.TEXT", msg);
            WebViewActivity.this.startActivity(intent);
        }

        public String getConfig(String key, String def) {
            return getApp().getConfService().get(key, def).getValue();
        }

        public void setConfig(String key, String value) {
            Configuration conf = getApp().getConfService().get(key, value);
            conf.setValue(value);
            getApp().getConfService().save(conf);
        }

        public String getCronExecutionList(String cronExpression) {
            try {
                StringBuilder sb = new StringBuilder();
                CronExpression cron = new CronExpression("0 " + cronExpression);
                Date after = new Date();
                int i = 0;
                for (; i < 10; i++) {
                    after = cron.getTimeAfter(after);
                    if (after == null)
                        break;
                    sb.append(after.toString()).append("\n");
                }
                if (sb.length() == 0) {
                    return "Invalid format or time has passed";
                } else {
                    return sb.toString() + (i == 10 ? "......" : "");
                }
            } catch (ParseException e) {
                return "Invalid format";
            }
        }
    }
}
