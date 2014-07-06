package com.gaoshin.top;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.gaoshin.shortcut.ads.BarIconOrder;
import com.gaoshin.shortcut.ads.BarOrientation;
import com.gaoshin.sorma.annotation.Column;
import com.gaoshin.sorma.annotation.Ddl;
import com.gaoshin.sorma.annotation.Table;

@Table(
        name = "ShortcutGroup",
        autoId = true,
        create = {
        "create table if not exists ShortcutGroup (" +
                " _id integer PRIMARY KEY autoincrement" +
                ", name varchar(255)" +
                ", x integer" +
                ", y integer" +
                ", orientation varchar(64)" +
                ", iconOrder varchar(64)" +
                ", icon blob" +
                ", iconId integer" +
                ", enabled integer" +
                ")"
        },
        ddls = {
            @Ddl(yyyyMMddHHmmss = "20110430000000",
                    value = "create table if not exists ShortcutGroup (" +
                            " _id integer PRIMARY KEY autoincrement" +
                            ", name varchar(255)" +
                            ", x integer" +
                            ", y integer" +
                            ", orientation varchar(64)" +
                            ", iconOrder varchar(64)" +
                            ", icon blob" +
                            ", iconId integer" +
                            ")"
                ),
                @Ddl(yyyyMMddHHmmss = "20110618000000",
                        value = "alter table ShortcutGroup add column enabled integer"
            )
        },
        keyColumn = "_id")
public class ShortcutGroup {
    @Column(name = "_id")
    private Integer id;

    @Column
    private String name;

    @Column
    private int x;

    @Column
    private int y;

    @Column
    private byte[] icon;

    @Column
    private Integer iconId;

    @Column
    private BarOrientation orientation;

    @Column
    private BarIconOrder iconOrder;

    @Column
    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setOrientation(BarOrientation direction) {
        this.orientation = direction;
    }

    public BarOrientation getOrientation() {
        return orientation;
    }

    public void setIconOrder(BarIconOrder iconOrder) {
        this.iconOrder = iconOrder;
    }

    public BarIconOrder getIconOrder() {
        return iconOrder;
    }

    public Drawable getDrawable(Context context) {
        if (icon != null && icon.length > 0) {
            return new BitmapDrawable(BitmapFactory.decodeByteArray(icon, 0, icon.length));
        }
        if (iconId != null) {
            return context.getResources().getDrawable(iconId);
        }
        return null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
