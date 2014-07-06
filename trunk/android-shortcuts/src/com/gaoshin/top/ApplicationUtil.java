package com.gaoshin.top;

import java.io.ByteArrayOutputStream;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class ApplicationUtil {
    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    public static void runApp(final Context context, final Shortcut sc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String pkg = sc.getPkg();
                    String activity = sc.getActivity();
                    Log.i("==SC==", "run app " + pkg + ", " + activity);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.setComponent(new ComponentName(pkg, activity));
                    if (sc.getData() != null) {
                        intent.putExtra("data", sc.getData());
                    }
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void showAppInfo(Context context, String pkg) {
        try {
            Intent intent = new Intent();
            final int apiLevel = Build.VERSION.SDK_INT;
            if (apiLevel >= 9) { // above 2.3
                // String action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
                String action = "android.settings.APPLICATION_DETAILS_SETTINGS";// Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
                System.out.println(action);
                intent.setAction(action);
                Uri uri = Uri.fromParts(SCHEME, pkg, null);
                intent.setData(uri);
            } else { // below 2.3
                final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                        : APP_PKG_NAME_21);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                        APP_DETAILS_CLASS_NAME);
                intent.putExtra(appPkgName, pkg);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Cannot show application info. Most likely it's caused by the low Android version.", Toast.LENGTH_LONG).show();
        }
    }

    public static Drawable getActivityIcon(Context context, String pkg, String activity) throws Exception {
        PackageManager pm = context.getPackageManager();
        ComponentName cn = new ComponentName(pkg, activity);
        return pm.getActivityIcon(cn);
    }

    public static float pixToDp(Context context, int pix) {
        float scale = context.getResources().getDisplayMetrics().density;
        float dp = pix * scale;
        return dp;
    }

    public static byte[] drawableToByteArray(Drawable icon) {
        Rect rect = icon.getBounds();
        return drawableToByteArray(icon, rect.width(), rect.height());
    }

    public static byte[] drawableToByteArray(Drawable icon, int w, int h) {
        Rect rect = icon.getBounds();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 60, baos);
        return baos.toByteArray();
    }

    public static void kill(Context context, String pkg) {
        Log.i("==SC==", "Kill app " + pkg);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(pkg);
        //        for (RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
        //            for (String pkgName : info.pkgList) {
        //                if (pkg.equals(pkgName)) {
        //                    Log.i("SC", "Kill app " + pkg);
        //                    android.os.Process.killProcess(info.pid);
        //                }
        //            }
        //        }
    }
}
