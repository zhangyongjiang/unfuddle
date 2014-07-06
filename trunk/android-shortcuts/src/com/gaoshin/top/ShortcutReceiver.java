package com.gaoshin.top;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShortcutReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("ShortcutReceiver");
        TopApplication.startService(context, intent);
    }

}
