package com.gaoshin.stock;

import android.content.Context;
import android.widget.Button;

public class MenuItemView extends Button {
    private GaoshinMenuItem item;

    public MenuItemView(Context context) {
        super(context);
    }

    public void applyData(GaoshinMenuItem item) {
        this.item = item;
        String extra = "";
        if(Boolean.TRUE.equals(item.getDisabled())) {
            extra = "";
        }
        if(item.getColor() != null) {
            int bcolor = item.getColor() | 0xff000000;
            int fcolor = ((~item.getColor()) & 0x00ffffff) | 0xff000000;
            if(bcolor == fcolor) {
                fcolor = 0xffffffff;
            }
            setBackgroundColor(bcolor);
            setTextColor( 0xffffffff );
        }
        setText(item.getLabel() + extra);
    }
    
    public GaoshinMenuItem getItem() {
        return item;
    }
}
