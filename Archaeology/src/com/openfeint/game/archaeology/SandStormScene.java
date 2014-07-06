package com.openfeint.game.archaeology;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.openfeint.game.archaeology.beans.Card;
import com.openfeint.game.archaeology.beans.PlayerData;
import com.openfeint.game.archaeology.message.CardLostMessage;
import com.openfeint.game.archaeology.message.NextPlayerMessage;

public class SandStormScene extends Node {
    protected PlayerData player;
    private int discardSize;
    
    public SandStormScene(Context context, ArchaellogySession session, PlayerData player) {
        super(context, session);
        this.player = player;
        setupDiscardButton();
    }
    
    private void setupDiscardButton() {
        Button btn = new Button(getContext());
        btn.setText("Discard a Card");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = pickupCard();
                discard(card);
            }
        });
    }
    
    private Card pickupCard() {
        return null;
    }

    public void resetDiscardSize() {
        discardSize = player.cardSize() / 2;
    }
    
    private void discard(Card card) {
        CardLostMessage discardMsg = new CardLostMessage(card);
        session.getMessageBus().sendMessage(discardMsg);
        
        player.discard(card);
        discardSize--;
        if(discardSize == 0) {
            NextPlayerMessage msg = new NextPlayerMessage(player.getId());
            session.getMessageBus().sendMessage(msg);
        }
    }
}
