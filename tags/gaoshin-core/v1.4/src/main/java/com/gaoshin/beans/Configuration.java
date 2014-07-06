package com.gaoshin.beans;

public class Configuration {
    private Long id;
    private String name;
    private String value;

    public Configuration() {
    }

    public Configuration(String key, String def) {
        name = key;
        value = def;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getValue(Object def) {
        return value == null ? def.toString() : value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getInt() {
        return Integer.parseInt(value);
    }

    public boolean getBoolean() {
        String s = value.toLowerCase();
        return s.startsWith("y") || s.startsWith("t") || s.startsWith("1");
    }

    public float getFloat() {
        return Float.parseFloat(value);
    }

    public double getDouble() {
        return Double.parseDouble(value);
    }

}
