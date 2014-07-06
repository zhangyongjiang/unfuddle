package com.openfeint.game.archaeology;

import java.util.ArrayList;
import java.util.List;

import com.openfeint.game.archaeology.beans.Deck;
import com.openfeint.game.archaeology.beans.MarketPlaceData;
import com.openfeint.game.archaeology.beans.PlayerData;
import com.openfeint.game.message.MessageBus;

public class ArchaellogySession{
    private MessageBus messageBus;
    private long sessionId;
    private List<PlayerData> players;
    private Deck deck;
    private MarketPlaceData marketPlace;
    
    public ArchaellogySession(long sessionId, MessageBus messageBus) {
        this.sessionId = sessionId;
        this.messageBus = messageBus;
        players = new ArrayList<PlayerData>();
        marketPlace = new MarketPlaceData(this);
        deck = new Deck();
    }
    
    public MessageBus getMessageBus() {
        return messageBus;
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
    public void addPlayer(PlayerData player) {
        players.add(player);
    }
    
    public List<PlayerData> getPlayers() {
        return players;
    }
    
    public long getSessionId() {
        return sessionId;
    }

    public Deck getDeck() {
        return deck;
    }
    
    public MarketPlaceData getMarketPlace() {
        return marketPlace;
    }
}
