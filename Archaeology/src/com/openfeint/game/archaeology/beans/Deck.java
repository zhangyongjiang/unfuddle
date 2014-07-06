package com.openfeint.game.archaeology.beans;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<Card>();
    
    public Deck() {
        init();
        shuffle();
    }

    /**
     * for each TreasureCardType, add cards based on the rarity.
     */
    private void init() {
        // todo
    }

    /**
     * after 4 digs, insert sandstorm cards and reshuffle the cards.
     * @return the card on the top
     */
    public Card dig() {
        // todo
        return null;
    }
    
    private void shuffle() {
    }
    
    public int size() {
        return cards.size();
    }
}
