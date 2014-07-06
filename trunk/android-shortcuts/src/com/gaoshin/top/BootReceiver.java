package com.gaoshin.top;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.gaoshin.sorma.AndroidContentResolver;
import com.gaoshin.sorma.AnnotatedORM;
import common.android.AndroidUtil;
import common.android.PhoneInfo;
import common.util.web.WebServiceUtil;

public class BootReceiver extends BroadcastReceiver {
    private AnnotatedORM orm = TopContentProvider.orm;

	@Override
	public void onReceive(Context context, Intent intent) {
        TopApplication.startService(context, intent);
        checkVersion(context);
	}

    private void checkVersion(final Context context) {
        orm.setContentResolver(new AndroidContentResolver(context.getContentResolver()));
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PhoneInfo info = AndroidUtil.getPhoneInfo(context);
                    String pkg = info.getApp();
                    String[] domain = pkg.split("[\\.]+");
                    int ver = info.getVersion();
                    String path = "http://" + domain[1] + "." + domain[0] + "/" + domain[2] + "/" + ver + "/conf.html";
                    ConfigurationList configurationList = WebServiceUtil.get(new DefaultHttpClient(), path, ConfigurationList.class);
                    for (Configuration conf : configurationList.getList()) {
                        orm.delete(Configuration.class, "ckey=?", new String[] { conf.getKey() });
                        orm.insert(conf);
                    }
                } catch (Exception e) {
                }
                return null;
            }
        }.execute(new Void[0]);
    }
}
