package com.gaoshin.dating;

public enum DimensionLookingFor {
    Man("Man", 10),
    Woman("Woman", 20),
    Friends("Friends", 30),
    Any("Any", 40), ;

    private String label;
    private int code;

    private DimensionLookingFor(String label, int code) {
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
