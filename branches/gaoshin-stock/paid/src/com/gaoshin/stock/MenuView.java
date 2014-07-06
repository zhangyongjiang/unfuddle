package com.gaoshin.stock;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.LinearLayout;

public class MenuView extends LinearLayout {
    private MenuGroupList groups;
    private ChartBrowser context;

    public MenuView(ChartBrowser context) {
        super(context);
        setId(Math.abs(this.hashCode()));
        this.context = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.BOTTOM);
        setBackgroundColor(0x80000000);
    }

    public void setMenus(MenuGroupList groups) {
        removeAllViews();
        this.groups = groups;
        if(groups == null || groups.getItems().size() == 0) {
            return;
        }
        int cols = groups.getItems().size();
        DisplayMetrics metrics = context.getApp().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        int colWidth = width / cols;
        for(MenuList menuList : groups.getItems()) {
            GroupedMenu gm = new GroupedMenu(getContext());
            gm.setMenuList(menuList);
            LayoutParams lp = new LayoutParams(colWidth, LayoutParams.WRAP_CONTENT);
            addView(gm, lp);
        }
    }
}
