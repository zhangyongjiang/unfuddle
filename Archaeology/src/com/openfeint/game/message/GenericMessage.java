package com.openfeint.game.message;

public class GenericMessage implements Message {
    private String from;
    private String to;;
    
    @Override
    public String getFrom() {
        return null;
    }

    @Override
    public String getTo() {
        return null;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
