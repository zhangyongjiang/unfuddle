package com.gaoshin.cj.api;

public enum PromotionType {
    Coupon("coupon"),
    Sweepstakes("sweepstakes"),
    Product("product"),
    SaleDiscount("sale/discount"),
    FreeShipping("free shipping"),
    SeasonalLink("seasonal link"),
    ;
    
    private String value;
    private PromotionType(String s) {
        value = s;
    }
    
    public String getValue() {
        return value;
    }
}
