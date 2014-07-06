package com.openfeint.game.message;

public interface MessageListener<T extends Message> {
    /**
     * @return listener id
     */
    String getListenerId();

    /**
     * Once this listener is registered with the message bus, 
     * this method will be called by message bus when msg.getTo() is null or msg.getTo() equals this listener id
     * @param msg
     */
    void processMessage(T msg);
}
