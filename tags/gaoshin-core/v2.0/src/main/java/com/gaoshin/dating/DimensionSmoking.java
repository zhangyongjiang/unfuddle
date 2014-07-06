package com.gaoshin.dating;

public enum DimensionSmoking {
    No("No", 10),
    Yes("Yes", 20),
    Sometimes("Sometimes", 30),
    WhenDrinking("When Drinking", 40),
    TryingToQuit("Trying to Quit", 50), ;

    private String label;
    private int code;

    private DimensionSmoking(String label, int code) {
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
