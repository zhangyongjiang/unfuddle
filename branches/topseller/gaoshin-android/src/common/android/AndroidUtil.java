package common.android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import common.android.contentprovider.Configuration;
import common.android.contentprovider.ContentProviderApi;
import common.util.DesEncrypter;

public class AndroidUtil {
    public static String getMyIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static DeviceLocation getCurrentLocation(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);

        Location bestLocation = null;

        Criteria criteria = new Criteria();
        String bestProvider = manager.getBestProvider(criteria, false);
        bestLocation = manager.getLastKnownLocation(bestProvider);

        List<String> providers = manager.getAllProviders();
        for (String it : providers) {
            Location location = manager.getLastKnownLocation(it);
            if (location != null) {
                if (bestLocation == null ||
                        location.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
        }

        if (bestLocation == null)
            return null;

        DeviceLocation deviceLocation = Geocoder.reverseGeocode(bestLocation.getLatitude(), bestLocation.getLongitude(), true);

        return deviceLocation;

    }

    public static PhoneInfo getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneInfo pi = new PhoneInfo();
        pi.setCallState(tm.getCallState());
        pi.setDataActivity(tm.getDataActivity());
        pi.setDataState(tm.getDataState());
        pi.setDeviceId(tm.getDeviceId());
        pi.setDeviceSoftwareVersion(tm.getDeviceSoftwareVersion());
        pi.setLine1Number(tm.getLine1Number());
        pi.setNetworkCountryIso(tm.getNetworkCountryIso());
        pi.setNetworkOperator(tm.getNetworkOperator());
        pi.setNetworkOperatorName(tm.getNetworkOperatorName());
        pi.setNetworkType(tm.getNetworkType());
        pi.setPhoneType(tm.getPhoneType());
        pi.setSimCountryIso(tm.getSimCountryIso());
        pi.setSimOperator(tm.getSimOperator());
        pi.setSimOperatorName(tm.getSimOperatorName());
        pi.setSimSerialNumber(tm.getSimSerialNumber());
        pi.setSimState(tm.getSimState());
        pi.setSubscriberId(tm.getSubscriberId());
        pi.setVoiceMailAlphaTag(tm.getVoiceMailAlphaTag());
        pi.setVoiceMailNumber(tm.getVoiceMailNumber());
        pi.setApp(context.getApplicationInfo().packageName);

        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getApplicationInfo().packageName, 0);
            pi.setVersion(info.versionCode);
        } catch (NameNotFoundException e) {
        }

        return pi;
    }

    private static String myphone = null;
    public static String getMyPhoneNumber(Context context) {
        if (myphone != null)
            return myphone;
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        myphone = mTelephonyMgr.getLine1Number();
        if (myphone == null)
            myphone = "8888888888";
        return myphone;
    }

    public static String getPhoneCookieValue(Context context) {
        String phone = getMyPhoneNumber(context);
        String ipaddr = getMyIpAddress();
        String phoneCookie = DesEncrypter.selfencrypt(phone, ipaddr);
        return phoneCookie;
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static String getMeta(ApplicationInfo appi, String keyName) {
        Bundle bundle = appi.metaData;
        if (bundle == null || !bundle.containsKey(keyName))
            return null;
        Object value = bundle.get(keyName);
        return value == null ? null : value.toString();
    }

    public static Configuration getApplicationConf(Context context, String key) {
        String value = ContentProviderApi.getStringConf(context.getContentResolver(), key);
        if (value == null) {
            value = getMeta(context.getApplicationInfo(), key);
        }
        return new Configuration(key, value);
    }

    public static String getApplicationInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName + "-" + info.versionCode + "-" + info.versionName;
        } catch (Exception e) {
            return null;
        }
    }

    public static long getUserIdleTime(Context context) {
        return System.currentTimeMillis() - getLastAccessTime(context);
    }

    public static long getLastAccessTime(Context context) {
        ApplicationAccessInfo aai = ContentProviderApi.getConf(context.getContentResolver(), ApplicationAccessInfo.class);
        if (aai == null) {
            return 0;
        } else {
            return aai.getLastAccessTime();
        }
    }

    public static void resetLastAccessTime(Context context) {
        try {
            ApplicationAccessInfo aai = ContentProviderApi.getConf(context.getContentResolver(), ApplicationAccessInfo.class);
            if (aai == null) {
                aai = new ApplicationAccessInfo();
            }
            aai.setLastAccessTime(System.currentTimeMillis());
            ContentProviderApi.save(context.getContentResolver(), aai);
        } catch (Exception e) {
            Log.e(AndroidUtil.class.getName(), "", e);
        }
    }

}
