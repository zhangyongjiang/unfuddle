package com.openfeint.game.archaeology.message;

import com.openfeint.game.message.GenericMessage;


public class PlayerSettledMessage extends GenericMessage {

    public PlayerSettledMessage(String playerId) {
        setFrom(playerId);
    }
}
