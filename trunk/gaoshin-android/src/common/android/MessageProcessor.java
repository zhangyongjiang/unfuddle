package common.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;
import common.util.web.JsonUtil;

public class MessageProcessor {
    private static MessageProcessor instance = null;

    public static MessageProcessor getInstance() {
        if (instance == null)
            instance = new MessageProcessor();
        return instance;
    }

    public void processMessage(Context context, GenericMessage msg) {
        if (MsgType.Web.equals(msg.getType())) {
            processWebMessage(context, msg);
        }
        if (MsgType.Conf.equals(msg.getType())) {
            processConfMessage(context, msg);
        }
    }

    private void processConfMessage(Context context, GenericMessage msg) {
        MsgType subtype = msg.getSubtype();
        String key = msg.getTitle();
        String value = msg.getMsg();

        if (MsgType.Insert.equals(subtype)) {
            if (key != null && value != null) {
                ContentProviderApi.insert(context.getContentResolver(), key, value);
            }
        }
        if (MsgType.Update.equals(subtype)) {
            if (key != null && value != null) {
                ContentProviderApi.update(context.getContentResolver(), key, value);
            }
        }
        if (MsgType.Delete.equals(subtype)) {
            if (key != null) {
                ContentProviderApi.delete(context.getContentResolver(), key);
            }
        }
    }

    private void processWebMessage(Context context, GenericMessage msg) {
        String url = msg.getUrl();
        int pos = url.indexOf('?');
        if (pos != -1)
            url = url.substring(0, pos);
        String lastVisited = ContentProviderApi.getStringConf(context.getContentResolver(), ConfKeyList.LastVisitedPage.name());

        long idleTime = AndroidUtil.getUserIdleTime(context);
        Log.i(this.getClass().getName(), "lastVisited is " + lastVisited + ", noti url is " + msg.getUrl() + ", idle time is " + (idleTime / 1000));
        if (idleTime > 60000 || lastVisited == null || !lastVisited.endsWith(url)) {
            if (msg.getSentTime() == null || msg.getSentTime() == 0l) {
                msg.setSentTime(System.currentTimeMillis());
            }

            int icon = context.getApplicationInfo().icon;
            long when = System.currentTimeMillis();
            String tickerText = null;
            try {
                tickerText = context.getResources().getString(context.getApplicationInfo().labelRes);
            } catch (Throwable t) {
                tickerText = context.getApplicationInfo().name;
            }

            String title = msg.getTitle();
            String content = msg.getMsg();
            Intent notificationIntent = new Intent(context, GenericWebActivity.class);
            notificationIntent.setData(Uri.parse(JsonUtil.toJsonString(msg)));
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification(icon, tickerText, when);
            notification.defaults |= Notification.FLAG_AUTO_CANCEL;
            // notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.setLatestEventInfo(context, title, content, contentIntent);

            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
            mNotificationManager.cancel(msg.getType().ordinal());
            mNotificationManager.notify(msg.getType().ordinal(), notification);
        }
    }
}
