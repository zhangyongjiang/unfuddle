package com.gaoshin.stock;

import android.content.Context;
import android.widget.Button;

public class StockGroupItemView extends Button {
    private String name;

    public StockGroupItemView(Context context) {
        super(context);
        setTextSize(12);
        setHeight(40);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setText(name);
    }

}
