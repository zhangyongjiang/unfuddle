package common.android;

import java.util.Date;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;
import common.android.log.LogLevel;
import common.android.log.LogList;
import common.android.log.LogReader;
import common.util.DesEncrypter;
import common.util.web.JsonUtil;
import common.util.web.WebServiceUtil;

public class LogSender extends IntentService {
    public static final String LogCheckTimeKey = "LAST_LOG_CHECK_TIME";
    public static final String LogLevelKey = "LOG_LEVEL";

    public LogSender() {
        super(LogSender.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendLog();
    }

    private void sendLog() {
        long lastCheckTime = 0l;
        try {
            lastCheckTime = ContentProviderApi.getLongConf(getContentResolver(), LogCheckTimeKey, 0l);
            String logLevelConf = ContentProviderApi.getStringConf(getContentResolver(), LogLevelKey, "E");
            Log.i(this.getClass().getName(), "last log check time " + new Date(lastCheckTime) + ", log level is " + logLevelConf);
            LogLevel logLevel = LogLevel.get(logLevelConf);
            LogList logs = LogReader.read(logLevel, lastCheckTime, null, getApplicationInfo().packageName);
            int size = logs.getList().size();
            Log.i(this.getClass().getName(), size + " logs found above level " + logLevel);
            
            if (size > 0) {
                String json = JsonUtil.toJsonString(logs);
                send(json);
            }
            lastCheckTime = System.currentTimeMillis();
            ContentProviderApi.update(getContentResolver(), LogCheckTimeKey, lastCheckTime + "");
        } catch (Throwable e) {
            String msg = "cannot slog " + e.getMessage();
            try {
                send(msg);
            } catch (Exception e1) {
                Log.e(this.getClass().getSimpleName(), msg);
            }
        } 
    }

    private void send(String json) throws Exception {
        GenericMessage gm = new GenericMessage();
        gm.setType(MsgType.Log);
        gm.setMsg(DesEncrypter.selfencrypt(json, MsgType.Log.name()));

        String baseUrl = ((GenericApplication) getApplication()).getServerBaseUrl();
        String getfrom = baseUrl + "/notification/report";
        DefaultHttpClient httpclient = ((GenericApplication) getApplication()).getHttpClient(getfrom);
        String phoneCookie = AndroidUtil.getPhoneCookieValue(getApplicationContext());
        String domain = WebServiceUtil.getDomain(getfrom);
        WebServiceUtil.addCookie(httpclient, "p", phoneCookie, domain);
        WebServiceUtil.post(getfrom, httpclient, String.class, gm);
    }
}
