package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshin.stock.model.StockGroup;

public class StockGroupHeaderView extends LinearLayout {
    private StockGroup stockGroup;
    
    private TextView button;
    private View.OnClickListener onClickListener;

    public StockGroupHeaderView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setupButton();
    }

    private void setupButton() {
        button = new Button(getContext());
        button.setText("Indexs");
        button.setTextSize(14);
        button.setTextColor(Color.BLACK);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER;
        addView(button, param);
    }
    
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public StockGroup getStockGroup() {
        return stockGroup;
    }

    public void setStockGroup(StockGroup stockGroup) {
        this.stockGroup = stockGroup;
    }

    public void applyData() {
        button.setText(stockGroup.getName());
    }
}
