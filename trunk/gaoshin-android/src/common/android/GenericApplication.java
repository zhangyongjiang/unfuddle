package common.android;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import common.android.contentprovider.Configuration;
import common.android.contentprovider.ContentProviderApi;
import common.util.web.WebServiceUtil;

public class GenericApplication extends Application {
    private static final String TAG = GenericApplication.class.getName();

    private HttpParams params = null;
    private ThreadSafeClientConnManager connManager = null;
    private BasicCookieStore cookieStore = null;

	@Override
	public void onCreate() {
		super.onCreate();

        try {
            saveMetaDataToContentProvider();
        } catch (Exception e1) {
            Log.e(this.getClass().getName(), "", e1);
        }

        try {
            prepareConnectionManager();
        } catch (Exception e1) {
            Log.e(this.getClass().getName(), "", e1);
        }

        try {
            if (getConf(ConfKeyList.EnableLocationReport).getBoolean(false)) {
                startLocationReportService(this);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            startNotificationPollerService(this);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            startLogSenderService(this);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            setupWidgetUpdateTimer(this);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }
	}
	
    private void saveMetaDataToContentProvider() {
        ApplicationInfo appi = null;
        try {
            appi = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Bundle bundle = appi.metaData;
        for (String name : bundle.keySet()) {
            if (ContentProviderApi.getStringConf(getContentResolver(), name) == null) {
                Object value = bundle.get(name);
                if (value == null)
                    continue;
                Log.i(TAG, "insert into ContentProvider " + name + "=" + value);
                ContentProviderApi.insert(getContentResolver(), name, value.toString());
            }
        }
    }

	private void prepareConnectionManager() {
        cookieStore = new BasicCookieStore();

        params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, false);
        ConnManagerParams.setMaxTotalConnections(params, 10);
        HttpConnectionParams.setConnectionTimeout(params, 20000);
        HttpConnectionParams.setSoTimeout(params, 20000);

		SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        connManager = new ThreadSafeClientConnManager(params, schReg);
	}
	
    public DefaultHttpClient getHttpClient(String url) {
        DefaultHttpClient client = new DefaultHttpClient(connManager, params);
        String domain = WebServiceUtil.getDomain(url);
        PhoneInfo phoneInfo = AndroidUtil.getPhoneInfo(getApplicationContext());
        setCookie(domain, "a", phoneInfo.getApp());
        setCookie(domain, "v", phoneInfo.getVersion() + "");
        setCookie(domain, "p", AndroidUtil.getPhoneCookieValue(getApplicationContext()));

        client.setCookieStore(cookieStore);
        return client;
    }

    public void setCookie(String domain, String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
    }

    public Map<String, String> parseCookies(String str) {
        Map<String, String> cookies = new HashMap<String, String>();
        if (str == null)
            return cookies;
        for (String s : str.split(";")) {
            String[] kv = s.split("=");
            if (kv.length < 2 || kv[1].trim().length() == 0) {
                continue;
            }
            cookies.put(kv[0].trim(), kv[1].trim());
        }
        return cookies;
    }

    public void updateCookie(String domain, String cookieString) {
        Map<String, String> cookies = parseCookies(cookieString);
        for (Entry<String, String> entry : cookies.entrySet()) {
            String name = entry.getKey();
            if (!name.equals("a") && !name.equals("v") && !name.equals("p")) {
                setCookie(domain, name, entry.getValue());
            }
        }
    }

    public String getCookieString(String domain) {
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookieStore.getCookies()) {
            if (domain.equalsIgnoreCase(cookie.getDomain())) {
                sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
            }
        }
        return sb.toString();
    }

    public Map<String, String> getCookies(String domain) {
        Map<String, String> cookies = new HashMap<String, String>();
        for (Cookie cookie : cookieStore.getCookies()) {
            if (domain.equalsIgnoreCase(cookie.getDomain())) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookies;
    }

    public String getCookie(String domain, String name) {
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(name) && domain.equals(cookie.getDomain())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String addPhoneCookies(String cookieString) {
        Map<String, String> cookies = parseCookies(cookieString);
        cookies.put("p", AndroidUtil.getPhoneCookieValue(getApplicationContext()));
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : cookies.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            sb.append(name).append("=").append(value).append(";");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public static void startLocationReportService(Context context) {
        Log.i(TAG, "Start location report service");
        context.startService(new Intent(context, LocationReporter.class));

        int interval = ContentProviderApi.getIntConf(context.getContentResolver(), ConfKeyList.LocationReportInterval.name(), 60);
        Intent intent = new Intent(LocationReporter.class.getName());
        int requestCode = 0;

        PendingIntent sender = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), interval * 1000, sender);
    }

    public static void startNotificationPollerService(Context context) {
        Log.i(TAG, "Start notification service");
        context.startService(new Intent(context, NotificationPoller.class));

        int interval = ContentProviderApi.getIntConf(context.getContentResolver(), ConfKeyList.NotificationPollingInterval.name(), 60);
        Intent intent = new Intent(NotificationPoller.class.getName());

        int requestCode = 1;
        PendingIntent sender = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), interval * 1000, sender);
    }

    public static void startLogSenderService(Context context) {
        Log.i(TAG, "Start log sender service");
        context.startService(new Intent(context, LogSender.class));

        int interval = ContentProviderApi.getIntConf(context.getContentResolver(), ConfKeyList.LogSenderInterval.name(), 60);
        Intent intent = new Intent(LogSender.class.getName());

        int requestCode = 3;
        PendingIntent sender = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), interval * 1000, sender);
    }

    public static void setupWidgetUpdateTimer(Context context) {
        // Intent intent = new Intent(WidgetProvider.class.getName());
        // intent.setData(Uri.parse(WidgetProvider.UPDATE_ACTION));
        // int requestCode = 4;
        // PendingIntent sender = PendingIntent.getService(context, requestCode,
        // intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // AlarmManager am = (AlarmManager)
        // context.getSystemService(ALARM_SERVICE);
        // am.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), 30 *
        // 1000, sender);
    }

    public Configuration getConf(Enum key) {
        return getConf(key.name());
    }

    public String getConfString(Enum key) {
        Configuration conf = getConf(key.name());
        if (conf == null)
            return null;
        else
            return conf.getValue();
    }

    public String getConfString(String key) {
        Configuration conf = getConf(key);
        if (conf == null)
            return null;
        else
            return conf.getValue();
    }

    public Configuration getConf(String key) {
        return AndroidUtil.getApplicationConf(this, key);
    }

    public String getHomeUrl() {
        String homeUrl = getServerBaseUrl();
        if (homeUrl.indexOf(".htm") == -1) {
            PhoneInfo info = AndroidUtil.getPhoneInfo(getApplicationContext());
            homeUrl += "/mobile-app/android/" + info.getApp() + "/v" + info.getVersion() + "/";
        }
        return homeUrl;
    }

    public String getServerBaseUrl() {
        return getConf(ConfKeyList.SERVER_BASE_URL).getValue();
    }

    public String getManifestHomeUrl() {
        String homeUrl = AndroidUtil.getMeta(getApplicationInfo(), ConfKeyList.SERVER_BASE_URL.name());
        PhoneInfo info = AndroidUtil.getPhoneInfo(getApplicationContext());
        homeUrl += "/android/" + info.getApp() + "/" + info.getVersion() + "/";
        return homeUrl;
    }

    public void saveWidgetMessage(WidgetMessage msg) {
        int maxSize = 50;
        List<Configuration> confList = ContentProviderApi.getConfList(getContentResolver(), WidgetMessage.class);
        if (confList.size() > maxSize) {
            ContentProviderApi.delete(getContentResolver(), confList.get(0).getId());
        }
        ContentProviderApi.append(getContentResolver(), msg);
    }

    public WidgetMessage getOneWidgetMessage() {
        return getOneWidgetMessage(getContentResolver());
    }

    public static WidgetMessage getOneWidgetMessage(ContentResolver contentResolver) {
        List<WidgetMessage> confList = ContentProviderApi.getObjectList(contentResolver, WidgetMessage.class);
        if (confList.size() == 0) {
            String poem = "You said we'd always be together\n"
                    + "And that gave me so much pleasure\n"
                    + "\n"
                    + "You said you'd never tell me a lie\n"
                    + "And that made me want to cry\n......";
            WidgetMessage msg = new WidgetMessage();
            msg.setMsg(poem);
            return msg;
        } else {
            int random = new Random(System.currentTimeMillis()).nextInt(confList.size());
            Log.i("WidgetMessage", "got " + confList.size() + " and selected " + random);
            return confList.get(random);
        }
    }
}
