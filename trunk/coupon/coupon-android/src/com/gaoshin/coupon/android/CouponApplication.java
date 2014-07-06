package com.gaoshin.coupon.android;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.gaoshin.coupon.android.model.Client;
import com.gaoshin.coupon.android.model.ConfKey;
import com.gaoshin.coupon.android.model.Configuration;
import com.gaoshin.coupon.android.model.ConfigurationServiceImpl;
import com.gaoshin.coupon.android.model.CouponContentProvider;
import com.gaoshin.sms.android.agent.SmsAgentHandler;
import com.gaoshin.sorma.AndroidContentResolver;
import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.DatabaseHandler;
import com.gaoshin.sorma.browser.HttpServer;

public class CouponApplication extends Application {
    private static final String TAG = CouponApplication.class.getSimpleName();
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private HttpServer server;
    private SORMA sorma;
    private int activities = 0;
    private Handler handler;
    private WebClient webClient;
    private ConfigurationServiceImpl confService;

    private LocationManager mLocationManager; 
    private LocationListener locationListener;
    private Location bestLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        sorma = SORMA.getInstance(getBaseContext(), CouponContentProvider.class);
        
        confService = new ConfigurationServiceImpl(sorma);
        handler = new Handler();
        webClient = new WebClient();
        checkParameter();
        locationListener = new AppteraLocationListener();
        requestLocationUpdate();
    }

    private void checkParameter() {
        String serverUrl = getMeta(ConfKey.ServerBase.name());
        Configuration conf = confService.get(ConfKey.ServerBase, serverUrl);
        confService.save(conf);
    }

    private void requestLocationUpdate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        String gpsProvider = LocationManager.GPS_PROVIDER;
        providers.remove(gpsProvider);
        for (String provider : providers) {
            mLocationManager.requestLocationUpdates(provider, 90000, 50,
                    locationListener);
        }
    }
    
    public SORMA getSorma() {
        return sorma;
    }

    public void activityPaused(Activity activity) {
        synchronized (this) {
            activities--;
            if (activities < 0) {
                throw new RuntimeException("activities < 0");
            }
            if (activities == 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (CouponApplication.this) {
                            if (activities == 0) {
                                Log.i(TAG, "stop http");
                                server.stop();
                                server = null;
                            }
                        }
                    }
                }, 300000);
            }
        }
    }

    public synchronized void activityResumed(Activity activity) {
        synchronized (this) {
            activities++;
            if (activities == 1) {
                try {
                    server = new HttpServer(6789, "/explorer");
                    String contentProviderBaseUri = sorma.getContentBaseUri();
                    server.registerHandler("/db", new DatabaseHandler(new AndroidContentResolver(getContentResolver()), contentProviderBaseUri));
                    server.registerHandler("/sms", new SmsAgentHandler(this));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ConfigurationServiceImpl getConfService() {
        return confService;
    }

    public WebClient getWebClient() {
        return webClient;
    }
    
    private String getMeta(String keyName) {
        try {
            ApplicationInfo appi = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);

            Bundle bundle = appi.metaData;
            if (bundle == null)
                return null;
            Object value = bundle.get(keyName);
            return value == null ? null : value.toString();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public class AppteraLocationListener implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            setBestLocation(location);
        }
    }

    private void setBestLocation(android.location.Location loc) {
        if (isBestLocation(loc, bestLocation)) {
            bestLocation = loc;
        }
    }

    private boolean isBestLocation(android.location.Location loc, android.location.Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }
        if (loc == null) {
            return false;
        }
        // Compare Location Time
        long timeDelta = loc.getTime() - currentBestLocation.getTime();
        boolean isNewer = timeDelta > TWO_MINUTES;
        boolean isOlder = timeDelta < -TWO_MINUTES;
        boolean isNewest = timeDelta > 0;

        if (isNewer) {
            return true;
        }
        else if (isOlder) {
            return false;
        }

        // Compare Accuracy
        int accuracyDelta = (int) (loc.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isLessAccurate = accuracyDelta > 600;
        boolean isFromSameProvider = isFromSameProvider(loc.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        }
        else if (isNewest && isMoreAccurate) {
            return true;
        }
        else if (isNewest && !isLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }
    
    private boolean isFromSameProvider(String provider1, String provider2) {
        return (provider1 == provider2) || (provider1!=null && provider1.equals(provider2));
    }

    public Client getDevice() {
        Client client = new Client();
        client.setId(confService.getDeviceId());
        
        List<String> providers = mLocationManager.getProviders(true);

        for (String provider : providers) {
            android.location.Location loc = mLocationManager.getLastKnownLocation(provider);
            setBestLocation(loc);
        }

        if (bestLocation == null) {
            return client;
        }
        
        client.setLatitude((float) bestLocation.getLatitude());
        client.setLongitude((float) bestLocation.getLongitude());

        return client;
    }
}
