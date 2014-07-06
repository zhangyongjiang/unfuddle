package com.gaoshin.stock;

import android.content.Context;
import android.widget.LinearLayout;

public class SummaryView extends LinearLayout {
    private TimeView timeView;

    public SummaryView(Context context) {
        super(context);
        setId(Math.abs(this.hashCode()));
        setupView();
    }

    private void setupView() {
        setOrientation(LinearLayout.HORIZONTAL);
        timeView = new TimeView(getContext());
        addView(timeView);
    }
}
