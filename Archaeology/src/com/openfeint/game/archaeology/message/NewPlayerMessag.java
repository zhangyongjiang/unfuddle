package com.openfeint.game.archaeology.message;

import com.openfeint.game.message.GenericMessage;

public class NewPlayerMessag extends GenericMessage {
    private String playId;
    
    public NewPlayerMessag(String playId) {
        this.playId = playId;
    }
    
    public String getPlayId() {
        return playId;
    }
}
