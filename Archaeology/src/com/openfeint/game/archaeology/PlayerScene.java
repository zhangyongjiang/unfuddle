package com.openfeint.game.archaeology;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.openfeint.game.archaeology.beans.Card;
import com.openfeint.game.archaeology.beans.CardType;
import com.openfeint.game.archaeology.beans.Deck;
import com.openfeint.game.archaeology.beans.PlayerData;
import com.openfeint.game.archaeology.message.EmptyDeckMessage;
import com.openfeint.game.archaeology.message.NextPlayerMessage;
import com.openfeint.game.archaeology.message.PlayerSettledMessage;
import com.openfeint.game.archaeology.message.SandStormMessage;
import com.openfeint.game.message.GenericListener;

public class PlayerScene extends Node {
    protected PlayerData playerData;
    private TradeScene tradeScene;
    private SandStormScene sandStormScene;

    public PlayerScene(Context context, ArchaellogySession session, PlayerData player) {
        super(context, session);
        this.playerData = player;
    }

    public PlayerData getPlayer() {
        return playerData;
    }

    public void start() {
        session.getMessageBus().registerMessageListener(new EmptyDeckMessageListener());
        setupNextPlayerButton();
    }
    
    private void setupNextPlayerButton() {
        Button nextPlayer = new Button(getContext());
        nextPlayer.setText("Next Player");
        addView(nextPlayer);
        nextPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextPlayerMessage msg = new NextPlayerMessage(playerData.getId());
                session.getMessageBus().sendMessage(msg);
            }
        });
    }

    public void addCard(Card card) {
        playerData.addCard(card);
    }

    private class EmptyDeckMessageListener extends GenericListener<EmptyDeckMessage> {
        public EmptyDeckMessageListener() {
            super(playerData.getId());
        }

        @Override
        public void processMessage(EmptyDeckMessage msg) {
            settle();
        }
    }

    private void settle() {
        // todo
        PlayerSettledMessage msg = new PlayerSettledMessage(playerData.getId());
        session.getMessageBus().sendMessage(msg);
    }

    public void onTurn() {
        tradeScene.show();
        sandStormScene.hide();
        
        Deck deck = session.getDeck();
        Card card = deck.dig();
        if (CardType.SandStorm.equals(card.getCardType())) {
            sandstorm();
        }
        else {
            addCard(card);
            tradeScene.refresh();
        }
    }

    private void sandstorm() {
        SandStormMessage msg = new SandStormMessage(playerData.getId());
        session.getMessageBus().sendMessage(msg);
    }

    public void handleSandStrom() {
        tradeScene.hide();
        sandStormScene.resetDiscardSize();
        sandStormScene.show();
    }
}
