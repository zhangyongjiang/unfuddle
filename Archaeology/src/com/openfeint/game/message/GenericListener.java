package com.openfeint.game.message;

public abstract class GenericListener<T extends Message> implements MessageListener<T>{
    private String listenerId;
    
    public GenericListener(String listenerId) {
        this.listenerId = listenerId;
    }
    
    @Override
    public String getListenerId() {
        return listenerId;
    }
}
