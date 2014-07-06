package com.gaoshin.top;

public class CallEvent{
    private CallState type;
    private String incomingNumber;
    private String outgoingNumber;
    private Long startTime;

    public CallEvent() {
        type = CallState.IDLE;
        startTime = System.currentTimeMillis();
    }
    
    public CallState getType() {
        return type;
    }

    public void setType(CallState type) { 
        this.type = type;
    }

    public String getIncomingNumber() {
        return incomingNumber;
    }

    public void setIncomingNumber(String incomingNumber) {
        this.incomingNumber = incomingNumber;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public void setOutgoingNumber(String outgoingNumber) {
        this.outgoingNumber = outgoingNumber;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String toString() {
        return type + "," + incomingNumber + "," + outgoingNumber;
    }
}
