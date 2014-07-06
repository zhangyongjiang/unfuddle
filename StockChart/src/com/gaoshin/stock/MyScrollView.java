package com.gaoshin.stock;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyScrollView extends HorizontalScrollView {

    public MyScrollView(Context context) {
        super(context);
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
