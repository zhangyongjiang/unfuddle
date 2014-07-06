package com.gaoshin.stock;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gaoshin.sorma.browser.JsonUtil;

public class GroupedMenu extends ScrollView {
    private MenuList menus;
    private LinearLayout container;

    public GroupedMenu(Context context) {
        super(context);
        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);
    }

    public void setMenuList(MenuList menuList) {
        container.removeAllViews();
        this.menus = menuList;
        if(menus == null || menus.getItems().size() == 0) {
            return;
        }
        for(GaoshinMenuItem gmi : menus.getItems()) {
            MenuItemView menuItemView = new MenuItemView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            container.addView(menuItemView, lp);
            menuItemView.applyData(gmi);
            menuItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MenuItemView thisView = (MenuItemView) v;
                    Intent intent = new Intent(BroadcastAction.MenuClicked.name());
                    intent.putExtra("menu", JsonUtil.toJsonString(thisView.getItem()));
                    getContext().sendBroadcast(intent);
                }
            });
        }
    }
}
