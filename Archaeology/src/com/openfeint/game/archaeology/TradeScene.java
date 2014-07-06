package com.openfeint.game.archaeology;

import android.content.Context;

import com.openfeint.game.archaeology.beans.CardSet;
import com.openfeint.game.archaeology.beans.PlayerData;
import com.openfeint.game.archaeology.message.TradeConfirmationMessage;
import com.openfeint.game.archaeology.message.TradeMessage;
import com.openfeint.game.message.GenericListener;
import com.openfeint.game.message.Message;

public class TradeScene extends Node {
    protected PlayerData player;

    public TradeScene(Context context, ArchaellogySession session, PlayerData player) {
        super(context, session);
        this.player = player;
        session.getMessageBus().registerMessageListener(new TradeConfirmationMessageListener());
    }
    
    private void sell(CardSet have) {
    }
    
    private void trade(CardSet have, CardSet need) {
        Message message = new TradeMessage(player.getId(), have, need);
        session.getMessageBus().sendMessage(message );
    }
    
    private class TradeConfirmationMessageListener extends GenericListener<TradeConfirmationMessage> {
        public TradeConfirmationMessageListener() {
            super(player.getId());
        }

        @Override
        public void processMessage(TradeConfirmationMessage msg) {
            CardSet have = msg.getHave();
            CardSet need = msg.getNeed();
            player.addSet(need);
            player.discardSet(have);
        }
    }
}
