package com.gaoshin.shortcut.ads;

import android.widget.LinearLayout;

public enum BarOrientation {
    Vertical(LinearLayout.VERTICAL),
    Horizontal(LinearLayout.HORIZONTAL);

    private int orientation;

    private BarOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }
}
