package com.openfeint.game.archaeology.message;

import com.openfeint.game.archaeology.beans.CardSet;
import com.openfeint.game.message.GenericMessage;

public class TradeMessage extends GenericMessage {
    private CardSet have;
    private CardSet need;

    /**
     * @param playId The id of the player who is trading with market place
     * @param have A set of cards the player has
     * @param need A set of cards the player wants
     */
    public TradeMessage(String playId, CardSet have, CardSet need) {
        this.have = have;
        this.need = need;
        setFrom(playId);
        setTo("MarketPlace");
    }
    
    public CardSet getHave() {
        return have;
    }
    
    public CardSet getNeed() {
        return need;
    }
}
