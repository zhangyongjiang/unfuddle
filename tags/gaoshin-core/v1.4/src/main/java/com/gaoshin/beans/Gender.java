package com.gaoshin.beans;

public enum Gender {
    Man,
    Woman;

    public static Gender fromString(String s) {
        if (s == null)
            return null;
        if (s.equals(Man.name()))
            return Man;
        if (s.equals(Woman.name()))
            return Woman;
        throw new RuntimeException("invalid gender " + s);
    }
}
