package com.openfeint.game.archaeology.beans;

public enum CardType {
    PotShards("Pot Shards", new int[]{1,2,3,4,5}, 1, 19),
    SandStorm("Sand Storm", null, 4, 0)
    ;
    
    private String name;
    private int[] sellingValues;
    private int rarity;
    private int tradingValue;
    
    private CardType(String name, int[] sellingValues, int rarity, int tradingValue) {
        this.name = name;
        this.sellingValues = sellingValues;
        this.rarity = rarity;
        this.tradingValue = tradingValue;
    }

    public String getName() {
        return name;
    }

    public int[] getSellingValues() {
        return sellingValues;
    }

    public int getRarity() {
        return rarity;
    }

    public int getTradingValue() {
        return tradingValue;
    }
    
    public int getSellingValue(int howmany) {
        return sellingValues[howmany - 1];
    }
}
