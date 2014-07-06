package com.gaoshin.top;

import android.content.Context;
import android.view.Gravity;

import com.gaoshin.shortcut.ads.BarIconOrder;
import com.gaoshin.shortcut.ads.BarOrientation;
import com.gaoshin.shortcut.ads.ShortcutBar;

public enum ShortcutGroupPosition {
    TopRight(
            "top right corner", 1, 0,
            BarOrientation.Vertical,
            BarIconOrder.IconFirst,
            Gravity.TOP | Gravity.RIGHT),
    RightCenter(
            "right side", 1, 0.5f,
            BarOrientation.Horizontal,
            BarIconOrder.ShortcutFirst,
            Gravity.RIGHT | Gravity.CENTER_VERTICAL),
    BottomRight(
            "bottom right corner", 1, 1,
            BarOrientation.Vertical,
            BarIconOrder.ShortcutFirst,
            Gravity.BOTTOM | Gravity.RIGHT),
    BottomCenter(
            "bottom center", 0.5f, 1,
            BarOrientation.Vertical,
            BarIconOrder.ShortcutFirst,
            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL),
    BottomLeft(
            "bottom left corner", 0, 1,
            BarOrientation.Vertical,
            BarIconOrder.ShortcutFirst,
            Gravity.BOTTOM | Gravity.LEFT),
    LeftCenter(
            "left side", 0, 0.5f,
            BarOrientation.Horizontal,
            BarIconOrder.IconFirst,
            Gravity.CENTER_VERTICAL | Gravity.LEFT),
    TopLeft(
            "top left corner", 0, 0,
            BarOrientation.Vertical,
            BarIconOrder.IconFirst,
            Gravity.TOP | Gravity.LEFT),
    TopCenter(
            "top center", 0.5f, 0,
            BarOrientation.Vertical,
            BarIconOrder.IconFirst,
            Gravity.TOP | Gravity.CENTER_HORIZONTAL), ;

    private String label;
    private BarOrientation orientation;
    private BarIconOrder iconOrder;
    private int gravity;
    private float offsetx;
    private float offsety;

    private ShortcutGroupPosition(String label, float offsetx, float offsety, BarOrientation orientation,
            BarIconOrder iconOrder, int gravity) {
        this.label = label;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.orientation = orientation;
        this.iconOrder = iconOrder;
        this.gravity = gravity;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return ordinal();
    }

    public float getOffsetX() {
        return offsetx;
    }

    public float getOffsetY() {
        return offsety;
    }

    public BarOrientation getOrientation() {
        return orientation;
    }

    public BarIconOrder getIconOrder() {
        return iconOrder;
    }

    public int getGravity() {
        return gravity;
    }

    public ShortcutGroupPosition next() {
        ShortcutGroupPosition[] constants = ShortcutGroupPosition.class.getEnumConstants();
        for (int i = 0; i < constants.length; i++) {
            if (constants[i].equals(this)) {
                return i == (constants.length - 1) ? ShortcutGroupPosition.TopRight : constants[i + 1];
            }
        }
        return null;
    }

    public static ShortcutGroupPosition getPositionById(int id) {
        for (ShortcutGroupPosition pos : ShortcutGroupPosition.class.getEnumConstants()) {
            if (pos.getId() == id) {
                return pos;
            }
        }
        return null;
    }

    public ShortcutBar createBar(Context context) {
        ShortcutGroup sg = new ShortcutGroup();
        sg.setId(getId());
        sg.setOrientation(getOrientation());
        sg.setIconOrder(getIconOrder());
        return new ShortcutBar(context, sg, 125);
    }
}
