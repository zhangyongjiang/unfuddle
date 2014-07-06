package com.gaoshin.shortcut.ads;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshin.shortcut.R;
import com.gaoshin.top.ApplicationUtil;
import com.gaoshin.top.Shortcut;
import com.gaoshin.top.ShortcutType;

public class ShortcutView extends RelativeLayout {
    protected Shortcut shortcut;
    private ImageView icon;
    private TextView label;

    public ShortcutView(Context context, Shortcut shortcut) {
        super(context);
        this.shortcut = shortcut;

        setLayoutParams(new LayoutParams(getViewWidth(), getViewHeight()));
        setGravity(Gravity.CENTER);

        ImageView background = new ImageView(context);
        background.setImageResource(R.drawable.yellow_white_128x128);
        addView(background);

        try {
            icon = new ImageView(context);
            LayoutParams lp = new LayoutParams(64, 64);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(icon, lp);
        } catch (Exception e) {
        }

        label = new TextView(context);
        label.setPadding(2, 0, 0, 2);
        label.setTypeface(Typeface.MONOSPACE);
        label.setTextColor(0xffffffff);
        label.setTextSize(16);
        label.setSingleLine();
        label.setBackgroundResource(R.drawable.gray_18);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(label, lp);

        applyData();
    }

    public void applyData() {
        try {
            Drawable drawable = ApplicationUtil.getActivityIcon(getContext(), shortcut.getPkg(), shortcut.getActivity());
            icon.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shortcut.getType().equals(ShortcutType.Info) && (shortcut.getLabel() == null || shortcut.getLabel().trim().length() == 0)) {
            shortcut.setLabel("info");
        }
        if (shortcut.getType().equals(ShortcutType.Kill) && (shortcut.getLabel() == null || shortcut.getLabel().trim().length() == 0)) {
            shortcut.setLabel("kill");
        }
        label.setText(shortcut.getLabel());
        if (shortcut.getLabel() != null && shortcut.getLabel().trim().length() > 0) {
            label.setVisibility(View.VISIBLE);
        } else {
            label.setVisibility(View.GONE);
        }
    }

    protected int getViewWidth() {
        return 80;
    }

    protected int getViewHeight() {
        return 80;
    }

    public Shortcut getShortcut() {
        return shortcut;
    }

    public void setShortcut(Shortcut sc) {
        this.shortcut = sc;
        applyData();
    }
}
