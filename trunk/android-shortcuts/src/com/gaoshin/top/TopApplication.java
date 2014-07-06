package com.gaoshin.top;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.gaoshin.shortcut.R;
import com.gaoshin.shortcut.ads.BarIconOrder;
import com.gaoshin.shortcut.ads.BarOrientation;
import com.gaoshin.shortcut.ads.ShortcutBar;
import com.gaoshin.sorma.AndroidContentResolver;
import com.gaoshin.sorma.AnnotatedORM;
import com.gaoshin.sorma.SormaContentResolver;
import com.gaoshin.top.plugin.dialer.CallShortcutEditActivity;
import com.gaoshin.top.plugin.dialer.SpeedDialerActivity;
import com.gaoshin.top.plugin.email.EmailShortcutEditActivity;
import com.gaoshin.top.plugin.email.SpeedEmailActivity;
import com.gaoshin.top.plugin.generic.GenericShortcutEditActivity;
import com.gaoshin.top.plugin.generic.SpeedViewActivity;
import com.gaoshin.top.plugin.internet.InternetShortcutEditActivity;
import com.gaoshin.top.plugin.internet.SpeedInternetActivity;
import com.gaoshin.top.plugin.sms.SmsShortcutEditActivity;
import com.gaoshin.top.plugin.sms.SpeedSmsActivity;
import common.util.web.JsonUtil;

public class TopApplication extends Application {
    private static final String TAG = TopApplication.class.getSimpleName();
    public static final String DefaultGroupName = "Untitled Group";
    public static int instanceCounter = 0;
    public static int appCounter = 0;

    private Map<Integer, ShortcutBar> shortcutGroups = new HashMap<Integer, ShortcutBar>();
    private ConfigurationServiceImpl confService;
    private Handler handler;
    private AnnotatedORM orm;
    private boolean shuttingDown;
    private boolean visible;
    private CallEvent callEvent;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private Sensor mAcceleroSensor;
    private Runnable lightSensorRunnable;
    private int sensorEventValue;

    private BuiltinApp[] builtinApps = {
            new BuiltinApp("com.gaoshin.shortcut", SpeedDialerActivity.class.getName()),
            new BuiltinApp("com.gaoshin.shortcut", SpeedSmsActivity.class.getName()),
            new BuiltinApp("com.gaoshin.shortcut", SpeedInternetActivity.class.getName()),
            new BuiltinApp("com.gaoshin.shortcut", SpeedEmailActivity.class.getName()),
            new BuiltinApp("com.gaoshin.shortcut", SpeedViewActivity.class.getName()),
            new BuiltinApp("com.android.settings", "com.android.settings.RunningServices"),
            new BuiltinApp("com.android.settings", "com.android.settings.PowerUsageSummary"),
            new BuiltinApp("com.android.settings", "com.android.settings.wifi.WifiSettings"),
            new BuiltinApp("com.android.settings", "com.android.settings.WirelessSettings"),
            new BuiltinApp("com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary"),
    };
    private AcceleroSensorRunnable acceleroSensorRunnable;
    private AcceleroSensorEventListener acceleroSensorEventListener;
    private LightSensorEventListener lightSensorEventListener;

    public TopApplication() {
        instanceCounter++;
        Log.i(TAG, "TopApplication Constructor ---------------------- .");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appCounter++;

        //        runningInfo();
        //        runningInfo();
        //        readShortcuts("content://com.android.launcher2.settings/favorites?notify=true");
        //        readShortcuts("content://com.android.launcher.settings/favorites?notify=true");

        orm = TopContentProvider.orm;
        orm.setContentResolver(new AndroidContentResolver(getContentResolver()));

        handler = new Handler();

        SormaContentResolver contentResolver = new AndroidContentResolver(getContentResolver());
        TopContentProvider.orm.setContentResolver(contentResolver);

        confService = new ConfigurationServiceImpl(TopContentProvider.orm);

        upgrade();

        //createOngoingNotification();
        setupShortcutBar();
        startService(new Intent(this, TopService.class));

        registerSensorListener();
        registerScreenReceiver();
        createTimer(this.getBaseContext());
    }

    private void registerSensorListener() {
        if(true) {
            return;
        }
        
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorRunnable = new LightSensorRunnable();
        lightSensorEventListener = new LightSensorEventListener();
        mSensorManager.registerListener(lightSensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        
        mAcceleroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        acceleroSensorRunnable = new AcceleroSensorRunnable();
        acceleroSensorEventListener = new AcceleroSensorEventListener();
        mSensorManager.registerListener(acceleroSensorEventListener, mAcceleroSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void registerScreenReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        for (BroadcastAction action : BroadcastAction.class.getEnumConstants()) {
            filter.addAction(action.name());
        }

        BroadcastReceiver receiver = new ShortcutReceiver();
        registerReceiver(receiver, filter);
    }

    private void setupShortcutBar() {
        Log.i("TopApplication", "setupShortcutBar");

        List<ShortcutGroup> groups = orm.getObjectList(ShortcutGroup.class, null, null);
        for (Integer groupId : shortcutGroups.keySet().toArray(new Integer[0])) {
            boolean found = false;
            for (ShortcutGroup group : groups) {
                if (group.getId().equals(groupId)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                deleteView(groupId);
            }
        }
        for (ShortcutGroup group : groups) {
            setupShortcutBar(group);
        }
    }

    private void deleteView(Integer groupId) {
        try {
            Log.i("SC", "delete shortcut group " + groupId);
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(shortcutGroups.get(groupId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConfigurationServiceImpl getConfService() {
        return confService;
    }

    private void setupShortcutBar(ShortcutGroup group) {
        Log.i(TAG, "setupShortcutBar " + group.getName());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        int groupId = group.getId();
        List<Shortcut> shortcuts = orm.getObjectList(Shortcut.class, "groupId=?", new String[] { groupId + "" }, "sequence");
        Log.i(TAG, shortcuts.size() + " shortcuts found for group " + group.getName());
        ShortcutBar shortcutBar = shortcutGroups.get(groupId);
        if (shortcutBar != null) {
            try {
                shortcutBar.removeAllViews();
                wm.removeView(shortcutBar);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (shortcuts.size() == 0) {
                return;
            }
        }
        if (!group.isEnabled()) {
            return;
        }

        int transparency = getConfService().get(ConfigEnum.Transparency, 100).getIntValue();
        boolean oneClickMode = getConfService().get(ConfigEnum.OneClickMode, true).getBoolean();
        {
            shortcutBar = new ShortcutBar(getApplicationContext(), group, transparency);
            shortcutBar.setIcon(group.getIcon());
            shortcutBar.setOneClickMode(oneClickMode);
            shortcutGroups.put(groupId, shortcutBar);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_KEYGUARD | WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
            params.format = 1;
            params.flags = 40 | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            if (BarIconOrder.IconFirst.equals(group.getIconOrder())) {
                if ((group.getX() + 80) >= width) {
                    params.gravity = Gravity.TOP | Gravity.RIGHT;
                    params.horizontalMargin = 0;
                } else {
                    params.gravity = Gravity.TOP | Gravity.LEFT;
                    float marginX = ((float) group.getX()) / (float) width;
                    params.horizontalMargin = marginX;
                }
                float marginY = ((float) group.getY()) / (float) height;
                params.verticalMargin = marginY;
            } else {
                if (group.getX() < 1) {
                    params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                    params.horizontalMargin = 0;
                } else {
                    params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                    float marginX = ((float) width - (float) group.getX() - 80) / (float) width;
                    params.horizontalMargin = marginX;
                }
                float marginY = ((float) height - (float) group.getY() - 80) / (float) height;
                params.verticalMargin = marginY;
            }
            shortcutBar.setLayoutParams(params);
            wm.addView(shortcutBar, shortcutBar.getLayoutParams());
            int rootViewHashCode = shortcutBar.getParent().hashCode();
            Log.i(TAG, "root view hash code " + rootViewHashCode);
        }

        for (Shortcut sc : shortcuts) {
            shortcutBar.addShortcut(sc);
        }

        if (shortcuts.size() == 0) {
            shortcutBar.hide();
        } else {
            shortcutBar.show();
        }
    }

    public static void startService(Context context, Intent intent) {
        Log.i(TAG, "Start shortcut service");
        
        {
            Intent serviceIntent = new Intent(context, TopService.class);
            serviceIntent.setAction(intent.getAction());
            serviceIntent.putExtras(intent);
            context.startService(serviceIntent);
        }
        
        {
            Intent scIntent = new Intent(context, ShortcutService.class);
            scIntent.setAction(ShortcutService.class.getName());
            scIntent.putExtras(intent);
            context.startService(scIntent);
        }
    }

    public void hideShortcutBars() {
    	if(!visible) return;
        for (ShortcutBar shortcutBar : shortcutGroups.values()) {
            shortcutBar.hide();
        }
        visible = false;
    }

    public void displayShortcutBars() {
    	if(visible) return;
        for (ShortcutBar shortcutBar : shortcutGroups.values()) {
            if (shortcutBar.size() == 0) {
                shortcutBar.hide();
            } else {
                shortcutBar.show();
            }
        }
    	visible = true;
    }

    public void enterPreviewMode() {
        if (getShortcutCount() == 0) {
            Toast.makeText(this, "You have no shortcut defined.", Toast.LENGTH_LONG).show();
            return;
        }

        for (ShortcutBar shortcutBar : shortcutGroups.values()) {
            if (shortcutBar.size() > 0) {
                shortcutBar.enterPreviewMode();
            }
        }
    }

    public int getShortcutCount() {
        int count = 0;
        for (ShortcutBar shortcutBar : shortcutGroups.values()) {
            count += shortcutBar.size();
        }
        return count;
    }

    public void exitPreviewMode() {
        for (ShortcutBar shortcutBar : shortcutGroups.values()) {
            if (shortcutBar.size() > 0) {
                shortcutBar.exitPreviewMode();
            }
        }
    }

    public void updateShortcuts() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setupShortcutBar();
            }
        });
    }

    public static void createTimer(Context context) {
        int requestCode = 0;
        Intent smsDeleteIntent = new Intent(TimerService.class.getName());
        PendingIntent sender = PendingIntent.getService(context, requestCode, smsDeleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime(), 10000, sender);
    }

    private void createOngoingNotification() {
        long when = System.currentTimeMillis();
        String title = "1-click shortcut";
        String content = "Click to edit your shortcuts";
        Intent notificationIntent = new Intent(this, ShortcutListActivity.class);
        Notification notification = new Notification(R.drawable.star_bw_256, null, when);
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, title, content, contentIntent);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) getSystemService(ns);
        nm.notify(1, notification);
    }

    public boolean hasShortcut(ShortcutGroupPosition pos) {
        List<Shortcut> shortcuts = orm.getObjectList(Shortcut.class, "groupId=?", new String[] { pos.getId() + "" });
        return shortcuts.size() > 0;
    }

    public boolean hasShortcut() {
        for (ShortcutGroupPosition pos : ShortcutGroupPosition.class.getEnumConstants()) {
            if (hasShortcut(pos))
                return true;
        }
        return false;
    }

    public void shortctClicked(Shortcut sc) {
        if (isBarIndenpendent()) {
            return;
        }

        for (ShortcutBar bar : shortcutGroups.values()) {
            if (bar.getGroupId().equals(sc.getGroupId())) {
                continue;
            }
            bar.shortcutClicked(sc);
        }
    }

    public void longSearch() {
        for (ShortcutBar bar : shortcutGroups.values()) {
            bar.starClicked(-1);
        }
    }

    public void starClicked(Integer groupId) {
        if (isBarIndenpendent()) {
            for (ShortcutBar bar : shortcutGroups.values()) {
                if (!bar.getGroupId().equals(groupId)) {
                    bar.restore();
                }
            }
        } else {
            for (ShortcutBar bar : shortcutGroups.values()) {
                bar.starClicked(groupId);
            }
        }
    }

    public boolean isBarIndenpendent() {
        return confService.get(ConfigEnum.IndependentBar, true).getBoolean();
    }

    public void startShortcutEditorActivity(Context context, Shortcut sc) {
        String thisPkg = "com.gaoshin.shortcut";
        Class activityClass = ShortcutEditActivity.class;
        if (thisPkg.equals(sc.getPkg()) && SpeedDialerActivity.class.getName().equals(sc.getActivity())) {
            activityClass = CallShortcutEditActivity.class;
        }
        if (thisPkg.equals(sc.getPkg()) && SpeedSmsActivity.class.getName().equals(sc.getActivity())) {
            activityClass = SmsShortcutEditActivity.class;
        }
        if (thisPkg.equals(sc.getPkg()) && SpeedInternetActivity.class.getName().equals(sc.getActivity())) {
            activityClass = InternetShortcutEditActivity.class;
        }
        if (thisPkg.equals(sc.getPkg()) && SpeedEmailActivity.class.getName().equals(sc.getActivity())) {
            activityClass = EmailShortcutEditActivity.class;
        }
        if (thisPkg.equals(sc.getPkg()) && SpeedViewActivity.class.getName().equals(sc.getActivity())) {
            activityClass = GenericShortcutEditActivity.class;
        }
        Intent intent = new Intent(context, activityClass);
        intent.putExtra("data", JsonUtil.toJsonString(sc));
        context.startActivity(intent);
    }

    public List<App> getBuildInApps() {
        List<App> buildin = new ArrayList<App>();

        PackageManager pm = getPackageManager();
        for (BuiltinApp builtin : builtinApps) {
            try {
                App app = new App();
                app.setPkgName(builtin.getPkg());
                app.setActivityName(builtin.getActivity());
                ComponentName cn = new ComponentName(app.getPkgName(), app.getActivityName());
                ActivityInfo info = pm.getActivityInfo(cn, 0);
                app.setLabel(info.loadLabel(pm).toString());
                buildin.add(app);
            } catch (Exception e) {
                System.out.println("cannot find " + builtin.getActivity());
            }
        }

        return buildin;
    }

    public synchronized void shortctBarDetached(final Integer groupId) {
        System.out.println("shortctBarDetached " + groupId);
    }

    public void shutdown() {
        shuttingDown = true;
    }

    public Map<Integer, ShortcutBar> getShortcutGroups() {
        return shortcutGroups;
    }

    private void upgrade() {
        List<ShortcutGroup> groups = orm.getObjectList(ShortcutGroup.class, null, null);
        int count = groups.size();
        if (count > 0) {
            return;
        }

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        List<Shortcut> shortcuts = orm.getObjectList(Shortcut.class, null, null);
        HashMap<Integer, List<Shortcut>> map = new HashMap<Integer, List<Shortcut>>();
        for (Shortcut sc : shortcuts) {
            List<Shortcut> list = map.get(sc.getGroupId());
            if (list == null) {
                list = new ArrayList<Shortcut>();
                map.put(sc.getGroupId(), list);
            }
            list.add(sc);
        }
        for (ShortcutGroupPosition pos : ShortcutGroupPosition.class.getEnumConstants()) {
            int groupId = pos.getId();
            List<Shortcut> scs = map.get(groupId);
            if (scs == null || scs.isEmpty()) {
                continue;
            }

            Log.i(TAG, scs.size() + " shortcut found for position " + pos.getLabel());
            count++;

            ShortcutGroup group = new ShortcutGroup();
            group.setOrientation(pos.getOrientation());
            group.setIconOrder(pos.getIconOrder());
            group.setName(pos.getLabel());

            int x = (int) (pos.getOffsetX() * width);
            if (x + 80 > width) {
                x = width - 80;
            }
            int halfw = width / 2;
            if (halfw >= x && (x + 80) >= halfw) {
                x = halfw - 40;
            }
            group.setX(x);

            int y = (int) (pos.getOffsetY() * height);
            if (y + 80 > height) {
                y = height - 80;
            }
            int halfh = height / 2;
            if (halfh >= y && (y + 80) >= halfh) {
                y = halfh - 40;
            }
            group.setY(y);

            group.setIconId(R.drawable.star_256);
            groupId = (int) orm.insert(group);
            group.setId(groupId);

            for (Shortcut sc : scs) {
                sc.setGroupId(group.getId());
                orm.update(sc);
            }
        }

        if (count == 0) {
            newShortcutGroup();
        }
    }

    public ShortcutGroup newShortcutGroup() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        ShortcutGroup defaultGroup = new ShortcutGroup();
        defaultGroup.setName(DefaultGroupName);
        defaultGroup.setOrientation(BarOrientation.Vertical);
        defaultGroup.setIconOrder(BarIconOrder.IconFirst);
        defaultGroup.setX(width - 80);
        defaultGroup.setY(0);
        defaultGroup.setIconId(R.drawable.star_256);
        long id = orm.insert(defaultGroup);
        defaultGroup.setId((int) id);
        return defaultGroup;
    }

    public void runningInfo() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        //        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        //            System.out.println("----------------------------------------" + service.service.getClassName());
        //            System.out.println("----------------------------------------" + service.service.getPackageName());
        //        }

        for (RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
            System.out.println("----------------------------------------" + info.pkgList[0]);
            System.out.println("----------------------------------------" + info.processName);
        }

    }

    public void readShortcuts(String uri) {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        boolean isInstallShortcut = false;
        final ContentResolver cr = getContentResolver();
        final Uri CONTENT_URI = Uri.parse(uri);
        Cursor c = cr.query(CONTENT_URI, null, null, null, null);
        if (c != null) {
            System.out.println("----------------------------------------------------------");
            int cols = c.getColumnCount();
            for (int i = 0; i < cols; i++) {
                System.out.print(c.getColumnName(i) + "\t");
            }
            System.out.println();
            while (c.moveToNext()) {
                for (int i = 0; i < cols; i++) {
                    try {
                        System.out.print(c.getString(i) + "\t");
                    } catch (Exception e) {
                        System.out.print("---\t");
                    }
                }
                System.out.println();
            }
            c.close();
        }
    }

    public void setCallEvent(CallEvent callEvent) {
        this.callEvent = callEvent;
        if(CallState.IDLE.equals(callEvent.getType())) {
            for (ShortcutBar shortcutBar : shortcutGroups.values()) {
                shortcutBar.enable(true);
            }
        }
    }

    private class LightSensorRunnable implements Runnable {
        @Override
        public void run() {
        }
    }

    private class AcceleroSensorRunnable implements Runnable {
        private int shakes;
        
        @Override
        public void run() {
            if(shakes < 4) {
                shakes = 0;
                return;
            }
            
            shakes = 0;
            if(visible) {
                hideShortcutBars();
            }
            else {
                displayShortcutBars();
            }
        }
        
        public int getShakes() {
            return shakes;
        }

        public void setShakes(int shakes) {
            this.shakes = shakes;
        }
    }
    
    private class LightSensorEventListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent arg0) {
            if(callEvent == null) {
                return;
            }
            if(arg0.sensor.getType() == Sensor.TYPE_LIGHT) {
                if(arg0.values[0]<100 && !CallState.IDLE.equals(callEvent.getType())){
                    for (ShortcutBar shortcutBar : shortcutGroups.values()) {
                        shortcutBar.enable(false);
                    }
                }
                else if (arg0.values[0]>=100 && !CallState.IDLE.equals(callEvent.getType())) {
                    for (ShortcutBar shortcutBar : shortcutGroups.values()) {
                        shortcutBar.enable(true);
                    }
                }
            }
        }
    }
    
    private class AcceleroSensorEventListener implements SensorEventListener {
        private float lastX, lastY, lastZ;
        private long lastUpdate;
        private float lastSpeed;
        
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent arg0) {
            if(arg0.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
                return;
            }
            
            if(lastUpdate == 0) {
                lastX = arg0.values[SensorManager.DATA_X];
                lastY = arg0.values[SensorManager.DATA_Y];
                lastZ = arg0.values[SensorManager.DATA_Z];
                lastUpdate = System.currentTimeMillis();
                return;
            }

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) < 100) {
                return;
            }
            
            float x = arg0.values[SensorManager.DATA_X];
            float y = arg0.values[SensorManager.DATA_Y];
            float z = arg0.values[SensorManager.DATA_Z];

            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            
            float speed = ( x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
            if(speed < -1200 || speed >1200) {
                if((lastSpeed < 0 && speed > 0) || (lastSpeed > 0 && speed < 0)) {
                    acceleroSensorRunnable.setShakes(acceleroSensorRunnable.getShakes() + 1);
                }
                handler.removeCallbacks(acceleroSensorRunnable);
                handler.postDelayed(acceleroSensorRunnable, 2000);
            }
            
            lastX = x;
            lastY = y;
            lastZ = z;
            lastSpeed = speed;
        }

    }

    public void userPresent() {
        screenOn();
    }
    
    public void screenOn() {
        displayShortcutBars();
        if(lightSensorEventListener == null || acceleroSensorEventListener==null) {
            registerSensorListener();
        }
    }
    
    public void screenOff() {
        hideShortcutBars();
        
        if(lightSensorEventListener != null) {
            mSensorManager.unregisterListener(lightSensorEventListener);
            lightSensorEventListener = null;
        }
        
        if(acceleroSensorEventListener != null) {
            mSensorManager.unregisterListener(acceleroSensorEventListener);
            acceleroSensorEventListener = null;
        }
    }
    
    public boolean isShortcutBarVisible() {
        return visible;
    }
}
