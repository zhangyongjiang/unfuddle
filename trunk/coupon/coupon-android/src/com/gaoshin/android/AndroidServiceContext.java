package com.gaoshin.android;

import android.app.Application;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;

public class AndroidServiceContext {
    private static final String tag = AndroidServiceContext.class.getSimpleName();
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private Application context;
    private String home;
    private String myVoiceMail;
    private LocationManager mLocationManager;
    private android.location.Location bestLocation = null;
    private LocationListener locationListener;
    private TelephonyService telephonyService;

    public AndroidServiceContext(Application app) {
        this.context = app;
		telephonyService = new TelephonyService(app.getBaseContext());
        home = app.getDir("home", 0).getAbsolutePath();
    }

    public void sendSMS(String phone, String msg) {
    	SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, msg, null, null);
    }
    
}
