package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

public class MoneyView extends RelativeLayout {

    public MoneyView(Context context) {
        super(context);
        setId(Math.abs(hashCode()));
        setBackgroundColor(Color.WHITE);
    }
    
    public LayoutParams getLayoutParams() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 80);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return lp;
    }

}
