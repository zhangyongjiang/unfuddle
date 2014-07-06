package com.gaoshin.top;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gaoshin.shortcut.R;

public class ShortcutService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setUpAsForeground("Click Click");
        return START_STICKY;
    }

    void setUpAsForeground(String text) {
        Configuration useIconConf = ((TopApplication)getApplication()).getConfService().get(ConfigEnum.UseIcon, true);
        boolean useIcon = useIconConf.getBoolean();
        if(!useIcon) {
            return;
        }
        
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), ShortcutEnableActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = R.drawable.trans_80x80;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), text, pi);
        startForeground(NOTIFICATION_ID, mNotification);
    }
}
