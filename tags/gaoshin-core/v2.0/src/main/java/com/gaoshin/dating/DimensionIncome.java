package com.gaoshin.dating;

public enum DimensionIncome {
    L20("< $20K", 10),
    G20("$20K-$40K", 20),
    G40("$40K-$60K", 40),
    G60("$60K-$80K", 50),
    G80("$80K-100K", 60),
    G100("$100-$150K", 70),
    G150("$150-$300K", 80),
    G300(">$300K", 90), ;

    private String label;
    private int code;

    private DimensionIncome(String label, int code) {
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
