package com.openfeint.game.archaeology.message;

import com.openfeint.game.message.GenericMessage;


public class SandStormMessage extends GenericMessage {
    /**
     * @param playerId. id of the player who found the sand storm card
     */
    public SandStormMessage(String playerId) {
        setFrom(playerId);
    }
}
