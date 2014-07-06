package com.gaoshin.coupon.db;

public class UserContextHolder {
    private static ThreadLocal<String> userState = new ThreadLocal<String>();
    
    public static void setUserState(String state) {
        userState.set(state);
    }
    
    public static String getUserState() {
        String value  = userState.get();
        return value;
    }
    
    public static void clearUserState() {
        userState.remove();
    }
}
