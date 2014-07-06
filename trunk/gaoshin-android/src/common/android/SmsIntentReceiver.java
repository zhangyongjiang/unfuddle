package common.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;
import common.util.web.JsonUtil;

public class SmsIntentReceiver extends BroadcastReceiver {

    private SmsMessage[] getMessagesFromIntent(Intent intent) {
        SmsMessage retMsgs[] = null;
        Bundle bdl = intent.getExtras();
        Object pdus[] = (Object[]) bdl.get("pdus");
        retMsgs = new SmsMessage[pdus.length];
        for (int n = 0; n < pdus.length; n++) {
            byte[] byteData = (byte[]) pdus[n];
            retMsgs[n] = SmsMessage.createFromPdu(byteData);
        }
        return retMsgs;
    }

    public void onReceive(Context context, Intent intent) {
        ApplicationInfo appinfo = context.getApplicationInfo();

        if (!intent.getAction().equals(
                "android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }

        SmsMessage msg[] = getMessagesFromIntent(intent);

        int xo = 0;
        final List<PendingMessage> mymsgs = new ArrayList<PendingMessage>();
        for (int i = 0; i < msg.length; i++) {
            String message = msg[i].getMessageBody();
            if (message == null)
                continue;

            String address = msg[i].getOriginatingAddress();
            String pattern = appinfo.packageName + " ";
            int pos = message.indexOf(pattern);
            if (pos == -1)
                continue;

            xo++;
            try {
                String encrypted = message.substring(pos + pattern.length());

                // String descrpted =
                // DesEncrypter.selfdecrypt(encrypted).get(0);
                // PendingMessage gm = JsonUtil.toJavaObject(descrpted,
                // PendingMessage.class);
                PendingMessage gm = JsonUtil.toJavaObject(encrypted, PendingMessage.class);

                gm.setFrom(address == null ? msg[i].getEmailFrom() : address);
                gm.setReceiveTime(msg[i].getTimestampMillis());

                ContentProviderApi.append(context.getContentResolver(), gm);
                mymsgs.add(gm);

                try {
                    MessageProcessor.getInstance().processMessage(context, gm);
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "", e);
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        }

        if (xo == msg.length) {
            abortBroadcast();
        } else {
            int requestCode = 0;
            Intent smsDeleteIntent = new Intent(context, SmsDeleteTimerReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, smsDeleteIntent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + 2000, sender);
        }
    }

}
