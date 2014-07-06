package com.openfeint.game.archaeology;

import android.content.Context;

import com.openfeint.game.archaeology.beans.MarketPlaceData;
import com.openfeint.game.archaeology.message.CardLostMessage;
import com.openfeint.game.archaeology.message.SandStormMessage;
import com.openfeint.game.message.GenericListener;

public class MarketPlace extends Node {
    private MarketPlaceData marketPlace;

    public MarketPlace(Context context, ArchaellogySession session) {
        super(context, session);
        marketPlace = session.getMarketPlace();
        session.getMessageBus().registerMessageListener(new DiscardMessageListener());
        session.getMessageBus().registerMessageListener(new SandStormMessageListener());
    }

    private class DiscardMessageListener extends GenericListener<CardLostMessage> {
        public DiscardMessageListener() {
            super("MarketPlace");
        }

        @Override
        public void processMessage(CardLostMessage msg) {
            marketPlace.addCard(msg.getCard());
        }
    }
    
    private class SandStormMessageListener extends GenericListener<SandStormMessage> {
        public SandStormMessageListener() {
            super("MarketPlace");
        }

        @Override
        public void processMessage(SandStormMessage msg) {
            marketPlace.incSandStormCardCount();
        }
    }
    
}
