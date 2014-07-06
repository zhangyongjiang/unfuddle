package com.openfeint.game.message;

public interface Message {
    /**
     * @return sender id
     */
    String getFrom();
    
    /**
     * @return recipient id. A null value means a broadcast message
     */
    String getTo();
}
