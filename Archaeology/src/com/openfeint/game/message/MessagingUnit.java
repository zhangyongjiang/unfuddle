package com.openfeint.game.message;

public abstract class MessagingUnit {
    private MessageBus messageBus;
    
    public MessagingUnit(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }
}
