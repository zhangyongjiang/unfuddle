package com.openfeint.game.archaeology;

import android.content.Context;

public class NextPlayerScene extends Node {

    public NextPlayerScene(Context context, ArchaellogySession session) {
        super(context, session);
    }

    public void prompt(final String playId, final NextPlayerReady callback) {
    }
    
    public static interface NextPlayerReady {
        void ready();
    }
}
