package com.openfeint.game.archaeology.beans;

import java.util.HashMap;
import java.util.Map;

import com.openfeint.game.archaeology.ArchaellogySession;

public class MarketPlaceData {
    private Map<CardType, Integer> cards;
    private int sandStormCardCount;
    private ArchaellogySession session;

    public MarketPlaceData(ArchaellogySession session) {
        cards = new HashMap<CardType, Integer>();
        this.session = session;
        sandStormCardCount = 0;
    }

    public void addCard(Card card) {
    }
    
    public Map<CardType, Integer> getCards() {
        return cards;
    }
    
    public void incSandStormCardCount() {
        sandStormCardCount++;
    }
    
    public int getSandStormCardCount() {
        return sandStormCardCount;
    }
}
