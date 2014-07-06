package com.gaoshin.top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.gaoshin.sorma.AndroidContentResolver;
import com.gaoshin.sorma.AnnotatedORM;

public class ShortcutActivity extends Activity {
    private static final String tag = ShortcutActivity.class.getSimpleName();
    private static Object lock = new Object();
    private static int runningActivityCount = 0;

    protected AnnotatedORM orm = TopContentProvider.orm;
    protected Handler handler;
    protected ConfigurationServiceImpl confService;
    private boolean changed = false;

    private static void incCount() {
        synchronized (lock) {
            runningActivityCount++;
        }
    }

    private static void decCount() {
        synchronized (lock) {
            runningActivityCount--;
        }
    }

    public static boolean running() {
        return runningActivityCount > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();
        confService = getApp().getConfService();
        orm.setContentResolver(new AndroidContentResolver(getContentResolver()));
    }

    protected TopApplication getApp() {
        return (TopApplication) getApplication();
    }

    @Override
    protected void onPause() {
        decCount();
        Log.i(tag, this.getClass() + " onPause");
        super.onPause();
        if (changed) {
            getApp().updateShortcuts();
            changed = false;
        }
        getApp().displayShortcutBars();
    }

    protected void onResume() {
        incCount();
        Log.i(tag, this.getClass() + " onResume");
        getApp().hideShortcutBars();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i(tag, this.getClass() + " onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i(tag, this.getClass() + " onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(tag, this.getClass() + " onStart");
        super.onStart();
    }

    protected ActivityInfo getApp(String pkg, String activity) {
        PackageManager pm = getPackageManager();
        ComponentName cn = new ComponentName(pkg, activity);
        try {
            ActivityInfo info = pm.getActivityInfo(cn, 0);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    protected void setChanged() {
        changed = true;
    }

    private List<ResolveInfo> getApplicationResolveInfo() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> appList1 = pm.queryIntentActivities(mainIntent, 0);

        Intent mainIntent2 = new Intent(Intent.ACTION_MAIN, null);
        mainIntent2.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appList2 = pm.queryIntentActivities(mainIntent2, 0);

        appList1.addAll(appList2);
        return appList1;
    }

    private List<ResolveInfo> getAllApplicationResolveInfo() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        return appList;
    }

    protected List<App> getApplicationList(boolean all) {
        List<App> apps = new ArrayList<App>();
        PackageManager pm = getPackageManager();
        List<ResolveInfo> appList = null;
        if (all) {
            appList = getAllApplicationResolveInfo();
        } else {
            appList = getApplicationResolveInfo();
        }
        for (ResolveInfo info : appList) {
            String pkgName = info.activityInfo.applicationInfo.packageName;
            String activityName = info.activityInfo.name;
            App app = new App();
            app.setPkgName(pkgName);
            app.setActivityName(activityName);
            app.setLabel(info.activityInfo.loadLabel(pm).toString());
            //            app.setIcon(info.activityInfo.loadIcon(pm));
            apps.add(app);
        }

        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App a1, App a2) {
                return a1.getLabel().compareToIgnoreCase(a2.getLabel());
            }
        });

        apps.addAll(0, getApp().getBuildInApps());

        return apps;
    }

}
