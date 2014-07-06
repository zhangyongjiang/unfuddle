package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YahooQuoteView extends LinearLayout {
    private String sym;
    private TextView infoBtn;
    private Handler handler;
    private long lastQuoteTime;

    public YahooQuoteView(Context context) {
        super(context);
        setId(Math.abs(hashCode()));
        handler = new Handler();
        setupInfoButton();
        setBackgroundColor(Color.WHITE & 0xbfffffff);
    }
    
    private void setupInfoButton() {
        infoBtn = new TextView(getContext());
        infoBtn.setMaxLines(1);
        infoBtn.setLines(1);
        infoBtn.setTextColor(Color.BLACK);
        infoBtn.setVisibility(View.GONE);
        addView(infoBtn);
    }
    
    public void setSymbol(String symb) {
        this.sym = symb;
        infoBtn.setText(sym);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = YahooQuote.getQuote(sym);
                    Log.d(null, result);
                    if(result != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String[] items = result.split("[\n\r]+");
                                    if(items.length > 3 && items[2].indexOf(sym) != -1) {
                                        String info = items[3];
                                        int pos = info.indexOf(" EST");
                                        if(pos != -1) {
//                                        info = info.substring(0,  pos);
                                        }
                                        pos = info.indexOf("Real Time");
                                        if(pos != -1) {
                                            info = info.substring(9);
                                        }
                                        if(info.indexOf("0.00")!=-1 && items.length>4) {
                                            info = items[4];
                                        }
                                        if(info.endsWith("Change:")) {
                                            info = info.substring(0, info.length() - 7);
                                        }
                                        pos = info.indexOf("Prev");
                                        if(pos != -1) {
                                            info = info.substring(0,  pos);
                                        }
                                        infoBtn.setText(info);
                                        if(info.toLowerCase().indexOf("after hours") == -1) {
                                            Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                catch (Exception e) {
                                }
                            }
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        /*
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    return YahooQuote.getQuote(sym);
                }
                catch (Exception e) {
                    return null;
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if(result != null) {
                    String[] items = result.split("[\n\r]+");
                    if(items.length == 4)
                        infoBtn.setText(items[3]);
                    if(items.length == 5)
                        infoBtn.setText(items[3] + "\n" + items[4]);
                }
            }
            
        }.execute(null);
        */
    }
}
