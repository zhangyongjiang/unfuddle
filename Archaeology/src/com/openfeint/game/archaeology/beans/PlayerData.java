package com.openfeint.game.archaeology.beans;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private String id;
    private int money;
    private Map<CardType, Integer> cards = new HashMap<CardType, Integer>();
    
    public PlayerData(String id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    
    public String getId() {
        return id;
    }
    
    public void addCard(Card card) {
    }
    
    public int cardSize() {
        return cards.size();
    }

    public void discard(Card card) {
    }

    public void addSet(CardSet need) {
        // TODO Auto-generated method stub
        
    }

    public void discardSet(CardSet have) {
        // TODO Auto-generated method stub
        
    }
}
