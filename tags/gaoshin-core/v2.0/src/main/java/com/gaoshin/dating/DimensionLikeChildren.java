package com.gaoshin.dating;

public enum DimensionLikeChildren {
    LikeChildren("Like Children", 10),
    DislikeChildren("Dislike Children", 20),
    NoChildren("Doesn't Want Children", 30), ;

    private String label;
    private int code;

    private DimensionLikeChildren(String label, int code) {
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
