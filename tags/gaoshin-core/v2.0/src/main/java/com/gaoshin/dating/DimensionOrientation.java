package com.gaoshin.dating;

public enum DimensionOrientation {
    Heterosexual("Heterosexual", 10),
    Bisexual("Bisexual", 20),
    Homosexual("Homosexual", 30), ;

    private String label;
    private int code;

    private DimensionOrientation(String label, int code) {
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
