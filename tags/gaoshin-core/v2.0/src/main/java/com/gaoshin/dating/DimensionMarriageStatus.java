package com.gaoshin.dating;

public enum DimensionMarriageStatus {
    NeverMarried("Never Married", 10),
    MarriedBefore("Married But Now Single", 20),
    Married("Married", 30), ;

    private String label;
    private int code;

    private DimensionMarriageStatus(String label, int code) {
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
