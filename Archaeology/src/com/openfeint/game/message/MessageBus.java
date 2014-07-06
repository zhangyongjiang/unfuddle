package com.openfeint.game.message;

public interface MessageBus {
    void registerMessageListener(MessageListener listener);
    void unregisterMessageListener(MessageListener listener);
    void sendMessage(Message message);
}
