package com.openfeint.game.archaeology.message;

import com.openfeint.game.archaeology.beans.CardSet;
import com.openfeint.game.message.GenericMessage;

public class TradeConfirmationMessage extends GenericMessage {
    private CardSet have;
    private CardSet need;
    
    public TradeConfirmationMessage(String playId, CardSet have, CardSet need) {
        this.have = have;
        this.need = need;
        setTo(playId);
        setFrom("MarketPlace");
    }
    
    public CardSet getHave() {
        return have;
    }
    
    public CardSet getNeed() {
        return need;
    }
}
