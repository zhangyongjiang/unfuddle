package com.gaoshin.stock;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.gaoshin.stock.model.GroupItem;

public class SymbolView extends TextView {
    private GroupItem item;

    public SymbolView(Context context) {
        super(context);
        setPadding(0, 0, 0, 0);
        setBackgroundColor(Color.LTGRAY);
        setHeight(36);
        setFocusable(false);
        setClickable(false);
        setTextSize(12);
        setTextColor(Color.BLACK);
        setGravity(Gravity.CENTER);
    }

    public void setGroupItem(GroupItem item) {
        this.item = item;
        String sym = item.getSym();
        setText(sym);
    }
    
    public GroupItem getGroupItem() {
        return item;
    }
    
    public void setSelected() {
        setBackgroundColor(Color.WHITE);
    }
    
    public void setUnselected() {
        setBackgroundColor(Color.LTGRAY);
    }
}
