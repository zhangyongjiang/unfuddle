package com.openfeint.game.archaeology;

import android.content.Context;
import android.widget.FrameLayout;

public class Node extends FrameLayout {
    
    protected ArchaellogySession session;

    public Node(Context context, ArchaellogySession session) {
        super(context);
        this.session = session;
    }
    
    public void start() {
    }
    
    public void hide() {
    }
    
    public void show() {
    }
    
    public void refresh() {
    }
}
