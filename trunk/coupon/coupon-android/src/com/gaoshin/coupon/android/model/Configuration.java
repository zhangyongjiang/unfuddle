package com.gaoshin.coupon.android.model;

import com.gaoshin.sorma.annotation.Column;
import com.gaoshin.sorma.annotation.Table;

@Table (
        name="Configuration",
        keyColumn="_id",
        autoId=true,
        create={
                "create table Configuration (" +
                        "_id INTEGER primary key autoincrement " +
                        ", _key text " +
                        ", _value text " +
                ")"
        },
        columns = {
                @Column(field="id",     name="_id"),
                @Column(field="key",    name="_key"),
                @Column(field="value",  name="_value"),
        }
)
public class Configuration {
    private Integer id;
    private String key;
    private String value;

    public Configuration() {
    }

    public Configuration(String key, Object obj) {
        this.key = key;
        this.value = obj.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value == null ? null : value.toString();
    }

    public int getIntValue(int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public long getLongValue(long def) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return def;
        }
    }

    public long getLongValue() {
        return Long.parseLong(value);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean getBoolean() {
        return value != null && (value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"));
    }
}
