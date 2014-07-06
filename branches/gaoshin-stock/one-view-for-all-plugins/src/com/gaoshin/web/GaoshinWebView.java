package com.gaoshin.web;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.webkit.WebView;

public class GaoshinWebView extends WebView {

    public GaoshinWebView(Context context) {
        super(context);
    }

    public String getAsset(String file) throws IOException {
        InputStream stream = getContext().getAssets().open(file);
        StringBuilder sb = new StringBuilder();
        byte[] buff = new byte[4096];
        while(true) {
            int len = stream.read(buff);
            if(len < 0) {
                break;
            }
            sb.append(new String(buff, 0, len));
        }
        stream.close();
        return sb.toString();
    }
    
}
