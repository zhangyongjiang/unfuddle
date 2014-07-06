package com.gaoshin.top;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Configuration;
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

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            screenOn = false;
            app.hideShortcutBars();
            return;
        }

        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            app.shutdown();
            return;
        }

        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            screenOn = true;
            return;
        }

        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            screenOn = true;
            Log.d(TopService.class.getSimpleName(), Intent.ACTION_USER_PRESENT);
            app.displayShortcutBars();
            //app.updateShortcuts();
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

        if (Intent.ACTION_CONFIGURATION_CHANGED.equals(action) && screenOn) {
            com.gaoshin.top.Configuration conf = getApp().getConfService().get("Orientation", "Portrait");
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (conf.getValue().equals("Portrait")) {
                    getApp().hideShortcutBars();
                } else {
                    getApp().displayShortcutBars();
                }
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (conf.getValue().equals("Landscape")) {
                    getApp().hideShortcutBars();
                } else {
                    getApp().displayShortcutBars();
                }
            }
        }
    }

    private TopApplication getApp() {
        return (TopApplication) getApplication();
    }
}
