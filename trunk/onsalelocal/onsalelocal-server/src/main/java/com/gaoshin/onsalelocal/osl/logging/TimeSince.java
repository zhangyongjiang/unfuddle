package com.gaoshin.onsalelocal.osl.logging;

public class TimeSince {
    private long startTime = System.currentTimeMillis();
    
    @Override
    public String toString() {
        String log = String.valueOf(System.currentTimeMillis() - startTime);
        return log;
    }
}
