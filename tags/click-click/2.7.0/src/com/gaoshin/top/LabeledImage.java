package com.gaoshin.top;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LabeledImage extends LinearLayout {
    protected ImageView icon;
    protected TextView label;
    private Drawable drawable;
    private String caption;

    public LabeledImage(Context context, Drawable drawable, String caption) {
        super(context);
        this.drawable = drawable;
        this.caption = caption;

        int iconWidth = 80;
        int iconHeight = 80;

        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);

        icon = new ImageView(context);
        icon.setLayoutParams(new LayoutParams(iconWidth, iconHeight));
        icon.setScaleType(ScaleType.FIT_CENTER);
        addView(icon);

        label = new TextView(context);
        label.setLines(getLines());
        label.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        if (!showLabel()) {
            label.setVisibility(View.GONE);
        }
        addView(label);

        updateView();
    }

    protected boolean showLabel() {
        return true;
    }

    protected int getLines() {
        return 1;
    }

    protected void updateView() {
        icon.setImageDrawable(drawable);
        label.setText(caption);
    }

    protected void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    protected Drawable getDrawable() {
        return drawable;
    }

    protected void setCaption(String caption) {
        this.caption = caption;
    }
}
