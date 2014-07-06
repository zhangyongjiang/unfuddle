package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshin.stock.bats.QuoteListener;
import com.gaoshin.stock.model.Quote;

public class QuoteView extends LinearLayout {
    private BatsView batsView;
    private String sym;
    private TextView infoBtn;
    private Handler handler;
    private long lastQuoteTime;
    private boolean autoRefresh;
    private Runnable autoRefreshRunnable;

    public QuoteView(Context context) {
        super(context);
        setId(Math.abs(hashCode()));
        handler = new Handler();
        setupInfoButton();
        setupBatsView();
        setBackgroundColor(Color.BLACK & 0x7fffffff);
        autoRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                if(autoRefresh && (now-lastQuoteTime > 2000)) {
                    batsView.setSymbol(sym);
                }
                handler.postDelayed(autoRefreshRunnable, 2000);
            }
        };
    }
    
    public void onResume() {
        handler.postDelayed(autoRefreshRunnable, 2000);
    }
    
    public void onPause() {
        handler.removeCallbacks(autoRefreshRunnable);
    }

    private void setupBatsView() {
        batsView = new BatsView(getContext());
        batsView.setQuoteListener(new QuoteListener() {
            @Override
            public void quoteChanged() {
                final Quote quote = batsView.getQuote();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String price = "";
                        if(quote.getPrice() > 0.001 && quote.getLastUpdateTime() != null && quote.getLastUpdateTime().trim().length()>0) {
                            price = "   " + quote.getLastUpdateTime() + "   " + quote.getPrice();
                            infoBtn.setText(quote.getSym() + price);
                            lastQuoteTime = System.currentTimeMillis();
                        }
                    }
                });
            }
        });
    }

    private void setupInfoButton() {
        infoBtn = new TextView(getContext());
//        infoBtn.setTextColor(Color.MAGENTA);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRefresh = !autoRefresh;
            }
        });
        addView(infoBtn);
    }
    
    public void setSymbol(String sym) {
        this.sym = sym;
        infoBtn.setText(sym);
        batsView.setSymbol(sym);
    }
}
