package common.android;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;
import common.util.DesEncrypter;
import common.util.web.GeoUtil;
import common.util.web.JsonUtil;
import common.util.web.ServiceException;
import common.util.web.WebServiceUtil;

public class LocationReporter extends IntentService {
    private DeviceLocation lastSent = null;

    public LocationReporter() {
        super(LocationReporter.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();

        DeviceLocation location = null;
        try {
            location = AndroidUtil.getCurrentLocation(context);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "=getloc=", e);
            return;
        }
        if (location == null)
            return;

        try {
            saveLocation(context, location);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "=saveloc=", e);
        }

        try {
            reportLocation(context, intent, location);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "=reploc=", e);
        }

        try {
            broadcastLocation(context, intent, location);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "=castloc=", e);
        }

	}

    private void broadcastLocation(Context context, Intent intent,
			DeviceLocation location) {
    	DeviceLocation saved = getSavedLocation(context.getContentResolver());
    	String json = JsonUtil.toJsonString(location);

        GenericMessage gm = new GenericMessage();
        gm.setType(MsgType.Location);
        gm.setMsg(json);
        String tobesent = JsonUtil.toJsonString(gm);

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(CommonAction.LocationChanged.getAction());
        broadcastIntent.putExtra("data", tobesent);
		context.sendBroadcast(broadcastIntent);
	}
    
    private void reportLocation(Context context, Intent intent, DeviceLocation location) {
        try {
            if (lastSent != null && location != null) {
                double miles = GeoUtil.distance(lastSent.getLatitude(), lastSent.getLongitude(), location.getLatitude(), location.getLongitude());
                if (miles < 0.1 && (location.getTime() - lastSent.getTime() < 300000)) {
                    Log.i(this.getClass().getName(), "skip loc. dis is " + miles);
                    return;
                }
            }

            String baseUrl = ((GenericApplication) getApplication()).getServerBaseUrl();
            String url = baseUrl + "/location/new";

            String jsonString = JsonUtil.toJsonString(location);
            String phone = AndroidUtil.getMyPhoneNumber(context);
            sendLocation(url, jsonString, phone);
            lastSent = location;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "=LRREPORT=", e);
        }
	}

    private void saveLocation(Context context, DeviceLocation location) {
    	try {
            Log.i(LocationReporter.class.getName(), "------ Save Location " + (location == null ? null : location.getLatitude()));
            if (location == null)
                return;

            String jsonString = JsonUtil.toJsonString(location);
            ContentProviderApi.update(context.getContentResolver(), DeviceLocation.class.getName(), jsonString);
        } catch (Throwable e) {
            Log.e(this.getClass().getName(), "=LRSAVE=", e);
        }
    }
    
    public static DeviceLocation getSavedLocation(ContentResolver contentResolver) {
    	String conf = ContentProviderApi.getStringConf(contentResolver, DeviceLocation.class.getName());
    	if(conf == null)
    		return null;
    	else
    		return JsonUtil.toJavaObject(conf, DeviceLocation.class);
    }

    private void sendLocation(String... params) {
        try {
            String sendto = params[0];
            String location = params[1];
            String phone = params[2];
            DefaultHttpClient httpclient = ((GenericApplication) getApplication()).getHttpClient(sendto);
            String ipaddr = AndroidUtil.getMyIpAddress();
            String phoneCookie = DesEncrypter.selfencrypt(phone, ipaddr);
            String domain = WebServiceUtil.getDomain(sendto);
            WebServiceUtil.addCookie(httpclient, "p", phoneCookie, domain);
            WebServiceUtil.post(sendto, httpclient, String.class, location);
        } catch (ServiceException e) {
            Log.e(this.getClass().getName(), "===sendloc=== errorCode: " + e.getErrorCode());
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "===sendloc=== " + e.getMessage());
        }
    }
}
