package com.gaoshin.dating;

public enum DimensionEducation {
    PostDoctoral("Post Doctoral", 10),
    Doctoral("Doctoral", 20),
    Professional("Professional", 30),
    Master("Master", 40),
    Bachelor("Bachelor", 50),
    Associate("Associate", 60),
    HighSchool("High School", 70),
    BelowHighSchool("Below High School", 80),
    Unknown("Unknown", 90);

    private String label;
    private int code;

    private DimensionEducation(String label, int code) {
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
