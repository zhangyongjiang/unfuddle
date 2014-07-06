package com.gaoshin.top;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import common.util.web.JsonUtil;

public class TopService extends IntentService {
    public static boolean screenOn = true;

    public TopService() {
        super(TopService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TopApplication app = (TopApplication) getApplication();
        String action = intent.getAction();
        
        if("CallEvent".equals(action)) {
            String json = intent.getStringExtra("data");
            Log.d(TopService.class.getSimpleName(), "CallEvent: " + json);
            CallEvent callEvent = JsonUtil.toJavaObject(json, CallEvent.class);
            app.setCallEvent(callEvent);
        }

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            screenOn = false;
            app.screenOff();
            return;
        }

        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            app.shutdown();
            return;
        }

        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            screenOn = true;
            app.screenOn();
            return;
        }

        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            screenOn = true;
            Log.d(TopService.class.getSimpleName(), Intent.ACTION_USER_PRESENT);
            app.userPresent();
            return;
        }

        if (BroadcastAction.StarClicked.name().equals(action)) {
            String data = intent.getStringExtra("data");
            Integer groupId = Integer.parseInt(data);
            app.starClicked(groupId);
            return;
        }

        if (BroadcastAction.ShortcutClicked.name().equals(action)) {
            String data = intent.getStringExtra("data");
            Shortcut sc = JsonUtil.toJavaObject(data, Shortcut.class);
            app.shortctClicked(sc);
            return;
        }

        if (BroadcastAction.ShortcutBarDetached.name().equals(action)) {
            Integer groupId = intent.getIntExtra("groupId", -1);
            app.shortctBarDetached(groupId);
            return;
        }

    }

    private TopApplication getApp() {
        return (TopApplication) getApplication();
    }
}
