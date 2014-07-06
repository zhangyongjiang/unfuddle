package com.gaoshin.stock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class TopButton extends LinearLayout {

    private int imgSize;
    private ImageView icon;

    public TopButton(Context context, int imgSize, int transparency) {
        super(context);
        this.imgSize = imgSize;
        int width = 80;
        int height = 56;
        LayoutParams lp = new LayoutParams(width, height);
        setLayoutParams(lp);

        icon = new ImageView(context);
        icon.setAlpha(transparency);
        LayoutParams imgLp = new LayoutParams(imgSize, imgSize);
        imgLp.topMargin = (height - imgSize) / 2;
        imgLp.leftMargin = (width - imgSize) / 2;
        icon.setLayoutParams(imgLp);
        icon.setScaleType(ScaleType.FIT_CENTER);
        addView(icon);
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setImageDrawable(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }
}
