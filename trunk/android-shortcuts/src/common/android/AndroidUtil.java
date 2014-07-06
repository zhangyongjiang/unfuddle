package common.android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;

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

        try {
            pi.setApp(context.getApplicationInfo().packageName);
            PackageManager manager = context.getPackageManager();
            PackageInfo info;
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

    public static String getApplicationInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName + "-" + info.versionCode + "-" + info.versionName;
        } catch (Exception e) {
            return null;
        }
    }

}
