package com.gaoshin.dating;

public enum DimensionDrinking {
    NoAtAll("No At All", 10),
    Rarely("Rarely", 20),
    Socially("Socially", 30),
    Often("Often", 40),
    VeryOften("Very Often", 50), ;

    private String label;
    private int code;

    private DimensionDrinking(String label, int code) {
        this.label = label;
        this.code = code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
