package com.openfeint.game.archaeology;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.openfeint.game.archaeology.NextPlayerScene.NextPlayerReady;
import com.openfeint.game.archaeology.beans.Card;
import com.openfeint.game.archaeology.beans.Deck;
import com.openfeint.game.archaeology.beans.PlayerData;
import com.openfeint.game.archaeology.message.EmptyDeckMessage;
import com.openfeint.game.archaeology.message.NextPlayerMessage;
import com.openfeint.game.archaeology.message.PlayerSettledMessage;
import com.openfeint.game.archaeology.message.SandStormMessage;
import com.openfeint.game.message.GenericListener;
import com.openfeint.game.message.Message;

public class Controller extends Node {
    private AddPlayerScene addPlayerScene;
    private NextPlayerScene nextPlayerScene;
    private GameOverScene gameOverScene;
    private List<PlayerScene> playersConsoleList;
    
    /**
     * the current player index of the List<PlayerScene>
     */
    private int currentPlayer;
    
    /*
     * track how many users left who needs to take care of a sand storm
     */
    private int sandStromingCount = 0;

    public Controller(Context context, ArchaellogySession session) {
        super(context, session);
        playersConsoleList = new ArrayList<PlayerScene>();
        currentPlayer = -1;
        addPlayerScene = new AddPlayerScene(context, session);
        nextPlayerScene = new NextPlayerScene(context, session);
    }
    
    private void hideAll() {
        addPlayerScene.hide();
        nextPlayerScene.hide();
        for(PlayerScene pc : playersConsoleList) {
            pc.hide();
        }
    }

    @Override
    public void start() {
        super.start();
        session.getMessageBus().registerMessageListener(new SandStormMessageListener());
        session.getMessageBus().registerMessageListener(new NextPlayerMessageListener());
        session.getMessageBus().registerMessageListener(new PlayerSettledMessageListener());
        addPlayerScene.addUser(addUserCallback);
    }
    
    private PlayerAddedCallback addUserCallback = new PlayerAddedCallback() {
        @Override
        public void addPlayer(String name) {
            PlayerData player = new PlayerData(name);
            session.addPlayer(player);
            PlayerScene console = new PlayerScene(getContext(), session, player);
            playersConsoleList.add(console);
            if(playersConsoleList.size() < 4) {
                addPlayerScene.addUser(addUserCallback);
            }
            else {
                dispatchTheFirstTreasure();
            }
        }
    };
    
    private NextPlayerReady userReadyCallback = new NextPlayerReady() {
        @Override
        public void ready() {
            playersConsoleList.get(currentPlayer).hide();
            currentPlayer = getNext();
            playersConsoleList.get(currentPlayer).show();
            playersConsoleList.get(currentPlayer).onTurn();
        }
    };
    
    private void dispatchTheFirstTreasure() {
        Deck deck = session.getDeck();
        for(PlayerScene pc : playersConsoleList) {
            Card card = deck.dig();
            pc.addCard(card);
        }
        promptNextPlayer();
    }
    
    private int getNext() {
        int next = currentPlayer;
        next ++;
        if(next >= playersConsoleList.size()) {
            next = 0;
        }
        return next;
    }
    
    private int getPrev(int player) {
        int prev = player;
        prev --;
        if(prev < 0) {
            prev = playersConsoleList.size() - 1;
        }
        return prev;
    }
    
    private void promptNextPlayer() {
        Deck deck = session.getDeck();
        if(deck.size() == 0) {
            Message msg = new EmptyDeckMessage();
            session.getMessageBus().sendMessage(msg);
            return;
        }
        
        final PlayerData next = playersConsoleList.get(getNext()).getPlayer();
        nextPlayerScene.prompt(next.getId(), userReadyCallback);
    }
    
    private class PlayerSettledMessageListener extends GenericListener<PlayerSettledMessage> {
        public PlayerSettledMessageListener() {
            super("Controller");
        }

        @Override
        public void processMessage(PlayerSettledMessage msg) {
            for(PlayerScene pc : playersConsoleList) {
                PlayerData player = pc.getPlayer();
                if(player.cardSize() != 0) {
                    return;
                }
            }
            gameOver();
        }
    }
    
    private class SandStormMessageListener extends GenericListener<SandStormMessage> {
        public SandStormMessageListener() {
            super("Controller");
        }

        @Override
        public void processMessage(SandStormMessage msg) {
            playersConsoleList.get(currentPlayer).handleSandStrom();
            sandStromingCount = playersConsoleList.size() - 1;
        }
    }
    
    private class NextPlayerMessageListener extends GenericListener<NextPlayerMessage> {
        public NextPlayerMessageListener() {
            super("Controller");
        }

        @Override
        public void processMessage(NextPlayerMessage msg) {
            if(sandStromingCount > 0) {
                promptNextSandStormPlayer(msg.getFrom());
            }
            else {
                promptNextPlayer();
            }
        }
    }
    
    private void promptNextSandStormPlayer(final String currentPlayerId) {
        int player = findPlayer(currentPlayerId);
        playersConsoleList.get(player).hide();
        player = getPrev(player);
        
        final PlayerData next = playersConsoleList.get(player).getPlayer();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Next player is " + next.getId());
        alert.setMessage("Please press this");

        final EditText input = new EditText(getContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int player = findPlayer(currentPlayerId);
                player = getPrev(player);
                playersConsoleList.get(player).show();
                playersConsoleList.get(player).handleSandStrom();
                sandStromingCount--;
            }
        });

        alert.show();
    }
    
    private int findPlayer(String playId) {
        for(int i=0; i<playersConsoleList.size(); i++) {
            PlayerScene pc = playersConsoleList.get(i);
            PlayerData player = pc.getPlayer();
            if(player.getId().equals(playId)) {
                return i;
            }
        }
        throw new RuntimeException();
    }
    
    private void gameOver() {
    }
}
