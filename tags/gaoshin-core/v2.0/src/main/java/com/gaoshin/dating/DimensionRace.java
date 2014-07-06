package com.gaoshin.dating;

public enum DimensionRace {
    Asian("Asian", 10),
    Black("Black", 20),
    HispanicLatin("Hispanic / Latin", 30),
    Indian("Indian", 40),
    MiddleEastern("Middle Eastern", 50),
    NativeAmerican("Native American", 60),
    PacificIslander("Pacific Islander", 70),
    White("White", 80),
    Other("Other", 90);

    private String label;
    private int code;

    private DimensionRace(String label, int code) {
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
