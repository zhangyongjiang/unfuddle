package common.android;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import common.android.contentprovider.Configuration;
import common.android.contentprovider.ContentProviderApi;
import common.util.web.JsonUtil;

public class SmsDeleteTimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            List<Configuration> list = ContentProviderApi.list(context.getContentResolver(), PendingMessage.class.getName());
            for (Configuration conf : list) {
                Uri deleteUri = Uri.parse("content://sms");
                PendingMessage pm = JsonUtil.toJavaObject(conf.getValue(), PendingMessage.class);
                context.getContentResolver().delete(deleteUri, "address=? and body=?", new String[] { pm.getFrom(), pm.getMsg() });

                ContentProviderApi.delete(context.getContentResolver(), conf.getId());
                GaoshinMessage gmsg = JsonUtil.toJavaObject(conf.getValue(), GaoshinMessage.class);
                ContentProviderApi.append(context.getContentResolver(), gmsg);
            }
        } catch (Throwable t) {
            Log.e(this.getClass().getName(), "", t);
        }
    }
}
