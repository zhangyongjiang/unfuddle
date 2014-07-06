package com.gaoshin.dating;

public enum DimensionPets {
    LikeAnimals("Like Animals", 10),
    DislikeAnimals("Dislike Animals", 20),
    LikeDogs("Like Dogs", 30),
    LikeCats("Like Cats", 40), ;

    private String label;
    private int code;

    private DimensionPets(String label, int code) {
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
