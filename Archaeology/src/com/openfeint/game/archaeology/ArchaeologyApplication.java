package com.openfeint.game.archaeology;

import android.app.Application;

import com.openfeint.game.message.MessageBus;
import com.openfeint.game.message.MessageBusImpl;

public class ArchaeologyApplication extends Application {
    private ArchaellogySession gameSession;
    private MessageBus messageBus;

    @Override
    public void onCreate() {
        super.onCreate();
        messageBus = new MessageBusImpl();
    }
    
    public ArchaellogySession getGameSession() {
        return gameSession;
    }
    
    public ArchaellogySession createGameSession() {
        if(gameSession != null) {
            gameSession.stop();
        }
        gameSession = new ArchaellogySession(System.currentTimeMillis(), messageBus);
        return gameSession;
    }
}
