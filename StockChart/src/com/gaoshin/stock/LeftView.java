package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LeftView extends LinearLayout {

    public LeftView(Context context) {
        super(context);
        setId(Math.abs(hashCode()));
        setBackgroundColor(Color.LTGRAY);
        setOrientation(VERTICAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(60, RelativeLayout.LayoutParams.FILL_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        setLayoutParams(lp);
    }

    public void addView(View view) {
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        super.addView(view, lp);
    }
}
