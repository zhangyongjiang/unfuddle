package com.openfeint.game.archaeology.message;

import com.openfeint.game.message.GenericMessage;


public class NextPlayerMessage extends GenericMessage {
    
    public NextPlayerMessage(String currentPlayerId) {
        setFrom(currentPlayerId);
    }
}
