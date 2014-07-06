package com.openfeint.game.archaeology.message;

import com.openfeint.game.archaeology.beans.Card;
import com.openfeint.game.message.GenericMessage;

public class CardLostMessage extends GenericMessage {
    private Card card;
    
    public CardLostMessage(Card card) {
        this.card = card;
    }
    
    public Card getCard() {
        return card;
    }
}
