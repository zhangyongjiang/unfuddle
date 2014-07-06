package com.gaoshin.stock.bats;

public class Bats {
    public static String getUrl(String sym) {
        return "http://www.batstrading.com/book/" + sym + "/";
    }
}
