package com.openfeint.game.archaeology;

import android.app.Activity;
import android.os.Bundle;


public class ArchaeologyActivity extends Activity {
    private ArchaellogySession session;
    private Controller controllerConsole;

    protected ArchaeologyApplication getApp() {
        return (ArchaeologyApplication)getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        session = getApp().createGameSession();
        session.start();
        
        controllerConsole = new Controller(getBaseContext(), session);
        controllerConsole.start();
    }
}