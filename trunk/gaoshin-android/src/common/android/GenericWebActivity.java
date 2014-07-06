package common.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xava.R;
import org.xml.sax.InputSource;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import common.amazon.aws.AWSBrowseNodeLookup;
import common.amazon.aws.AWSItemLookup;
import common.amazon.aws.AWSItemSearch;
import common.amazon.aws.AWSSimilarityLookup;
import common.amazon.aws.AwsSearchIndex;
import common.android.c2dm.C2DMessaging;
import common.android.contentprovider.Configuration;
import common.android.contentprovider.ContentProviderApi;
import common.android.contentprovider.MultipleRecordsException;
import common.util.DesEncrypter;
import common.util.reflection.ReflectionUtil;
import common.util.web.JsonUtil;
import common.util.web.Security;
import common.util.web.WebServiceUtil;

public class GenericWebActivity extends GenericActivity {
    private static final String TAG = GenericWebActivity.class.getName();

    private static final int GalleryIntentResult = 306;
    private static final int SelectPictureIntentResult = 208;

    private static String JSessionIdName = "JSESSIONID";

    private boolean noloading = true;
    private ProgressDialog progressDialog = null;
    private boolean progressDialogShowing = false;
    private WebView web = null;
    private WebView dialog = null;
    private String javascriptCallback = null;
    private String inputJson = null;
    private BroadcastReceiver receiver = new GenericBroadcastReceiver();
    private LinkedList<GenericMessage> broadcastMsgs = new LinkedList<GenericMessage>();
    private HashMap<String, Object> activityData = new HashMap<String, Object>();
    private HashMap<Integer, String> keyHandler = new HashMap<Integer, String>();
    private String lastSuccessPage = null;
    private int numOfPageVisited = 0;
    private boolean menuVisible = false;
    private CookieManager cookieManager;
    private Handler handler;
    private DialogWebViewClient dialogWebViewClient = null;
    private String dialogHtml = null;

    private int[] imgIds = {
            R.drawable.loading_48x48_0,
            R.drawable.loading_48x48_1,
            R.drawable.loading_48x48_2,
            R.drawable.loading_48x48_3,
            R.drawable.loading_48x48_4,
            R.drawable.loading_48x48_5,
            R.drawable.loading_48x48_6,
            R.drawable.loading_48x48_7,
    };
    private int currentLoadingImg = 0;
    private Timer timer;

    private WebViewJavascriptInterface webViewJavascriptInterface;

    private GenericApplication getGenericApplication() {
        return (GenericApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ipaddr = AndroidUtil.getMyIpAddress();
        if(ipaddr == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    GenericWebActivity.this).create();
            alertDialog.setMessage("It seems that you don't have Internet connection which is is required by this application. Please try again later.");
            alertDialog.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int which) {
                            dialog.cancel();
                            GenericWebActivity.this.finish();
                        }
                    });
            alertDialog.show();
        	return; 
        }
        handler = new Handler();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView img = (ImageView) findViewById(R.id.the_loading);
                        if (progressDialogShowing) {
                            img.setVisibility(View.VISIBLE);
                            img.setImageResource(imgIds[currentLoadingImg]);
                            currentLoadingImg++;
                            if (currentLoadingImg == imgIds.length)
                                currentLoadingImg = 0;
                        }
                        else {
                            img.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 150);
        
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (false) {
            web = new WebView(this);
            setContentView(web);
        } else {
            setContentView(R.layout.webview_layout);
            web = (WebView) findViewById(R.id.generalWebViewId);
            dialog = (WebView) findViewById(R.id.generalWebViewDialog);
            dialog.setVisibility(View.INVISIBLE);
        }

        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webViewJavascriptInterface = new WebViewJavascriptInterface();
        if (savedInstanceState != null) {
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setAllowFileAccess(true);
            web.restoreState(savedInstanceState);
            web.addJavascriptInterface(webViewJavascriptInterface, "Device");
            web.setWebChromeClient(new WebChromeClient());
            web.setWebViewClient(new GenericWebViewClient());

            dialogWebViewClient = new DialogWebViewClient();
            dialog.getSettings().setJavaScriptEnabled(true);
            dialog.addJavascriptInterface(webViewJavascriptInterface, "Device");
            dialog.setWebViewClient(dialogWebViewClient);
            dialog.restoreState(savedInstanceState);
            return;
        }

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.addJavascriptInterface(webViewJavascriptInterface, "Device");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new GenericWebViewClient());

        Intent currentIntent = getIntent();
        if (currentIntent.getData() != null) {
            Log.i(this.getClass().getName(), "onCreate got intent");
            processIntent(currentIntent);
            return;
        }

        String url = getGenericApplication().getHomeUrl();
        Log.i(this.getClass().getName(), "load u0 " + url);
        loadUrl(url);

        dialogWebViewClient = new DialogWebViewClient();
        dialog.getSettings().setJavaScriptEnabled(true);
        dialog.addJavascriptInterface(webViewJavascriptInterface, "Device");
        dialog.setWebViewClient(dialogWebViewClient);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(this.getClass().getName(), "onnewintent");
        processIntent(intent);
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        Log.i(this.getClass().getName(), "web intent data is not null");
        String url = null;
        try {
            if (intent.getData() == null)
                return;

            String json = intent.getData().toString();
            GenericMessage gm = JsonUtil.toJavaObject(json, GenericMessage.class);

            if (!gm.getUrl().startsWith("http")) {
                GenericApplication application = (GenericApplication) getApplication();
                url = application.getServerBaseUrl() + gm.getUrl();
            } else {
                url = gm.getUrl();
            }

            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nm = (NotificationManager) getSystemService(ns);
            nm.cancel(gm.getType().ordinal());

            Log.i(this.getClass().getName(), "==== load url : " + url);
            loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(String url) {
        // String domain = WebServiceUtil.getDomain(url);
        // String cookieString = cookieManager.getCookie(domain);
        // if (cookieString.indexOf(JSessionIdName)==-1) {
        // Log.i(this.getClass().getName(), "No session found.");
        // String phoneCookie = "p=" +
        // AndroidUtil.getPhoneCookieValue(getApplicationContext());
        // String expire = "; expires=" + new Date().toGMTString() + "; path=/";
        // String cookie = phoneCookie + expire;
        // System.out.println("setcookie =============== " + cookie);
        // cookieManager.setCookie(domain, cookie);
        // }
        web.loadUrl(url);
    }

    public void syncCookie(String url) {
        // try {
        // String domain = WebServiceUtil.getDomain(url);
        // String cookieString = cookieManager.getCookie(url);
        // Log.i(this.getClass().getName(), "-------- cookie after url " + url +
        // ": " + cookieString);
        // getGenericApplication().updateCookie(cookieString);
        // } catch (Exception e) {
        // Log.e(this.getClass().getName(), "syncCookie error", e);
        // }
    }

    private class GenericBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                String broadcastMsg = intent.getExtras().getString("data");
                GenericMessage gm = JsonUtil.toJavaObject(broadcastMsg, GenericMessage.class);
                broadcastMsgs.add(gm);
                String url = "javascript:handleMessage()";
                web.loadUrl(url);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "onReceive ", e);
            }
        }
    }

    private void registerReceiver() {
        for (CommonAction ca : CommonAction.class.getEnumConstants()) {
            IntentFilter filter = new IntentFilter(ca.getAction());
            registerReceiver(receiver, filter);
        }
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver();
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
    public String getPath(Uri uri) {
        String selectedImagePath;
        // 1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        } else {
            selectedImagePath = null;
        }

        if (selectedImagePath == null) {
            // 2:OI FILE Manager --- call method: uri.getPath()
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult requestCode is " + requestCode + ", resultCode is " + resultCode);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == SelectPictureIntentResult) {
            try {
                Uri selectedImageUri = data.getData();
                String filePath = getPath(selectedImageUri);
                if (filePath != null) {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    FileInputStream fis = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(fis, null, o);
                    fis.close();

                    int scale = 1;

                    final Integer targetWidth = (Integer) activityData.get("imageWidth");
                    if (targetWidth != null && targetWidth > 0) {
                        int width_tmp = o.outWidth, height_tmp = o.outHeight;
                        while (true) {
                            if (width_tmp / 2 < targetWidth || height_tmp / 2 < targetWidth)
                                break;
                            width_tmp /= 2;
                            height_tmp /= 2;
                            scale *= 2;
                        }
                    }

                    // Decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;

                    FileInputStream is = new FileInputStream(filePath);
                    Bitmap photo = BitmapFactory.decodeStream(is, null, o2);
                    is.close();
                    String fileName = System.currentTimeMillis() + ".jpg";
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                    String url = (String) activityData.get("form-url");
                    Log.i(this.getClass().getName(), "send image to " + url);
                    String result = WebServiceUtil.upload(((GenericApplication) getApplication()).getHttpClient(url), url, bais, fileName);
                    Log.i(this.getClass().getName(), "image upload result: " + result);

                    String successCallback = (String) activityData.get("successCallback");
                    web.loadUrl("javascript:" + successCallback + "();");
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "cannot load image", e);
                String errorCallback = (String) activityData.get("errorCallback");
                web.loadUrl("javascript:" + errorCallback + "();");
            }
        } else if (requestCode == GalleryIntentResult) {
            try {
                Bitmap photo = data.getParcelableExtra("data");
                if (photo == null) {
                    Uri selectedImageUri = data.getData();
                    String filePath = getPath(selectedImageUri);
                    if (filePath != null) {
                        photo = BitmapFactory.decodeFile(filePath);
                    }
                }

                String fileName = System.currentTimeMillis() + ".jpg";
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

                String url = (String) activityData.get("form-url");
                Log.i(this.getClass().getName(), "send image to " + url);
                String result = WebServiceUtil.upload(((GenericApplication) getApplication()).getHttpClient(url), url, bais, fileName);
                Log.i(this.getClass().getName(), "image upload result: " + result);

                String successCallback = (String) activityData.get("successCallback");
                web.loadUrl("javascript:" + successCallback + "();");
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "cannot load image", e);
                String errorCallback = (String) activityData.get("errorCallback");
                web.loadUrl("javascript:" + errorCallback + "();");
            }
        } else if (javascriptCallback != null) {
            String result = "null";
            if (data != null) {
                result = data.getStringExtra("outputJson");
            }
            if (result != null) {
                result = "'" + result + "'";
            }

            String inputJsonBack = inputJson;
            if (inputJsonBack != null) {
                inputJsonBack = "'" + inputJsonBack + "'";
            }

            web.loadUrl("javascript:" + javascriptCallback + "(" + inputJsonBack + "," + result + ")");
        }
        requestCode = 0;
        javascriptCallback = null;
    }

    private KeySequence backKey = new KeySequence(new int[] {
            KeyEvent.KEYCODE_BACK,
            KeyEvent.KEYCODE_BACK,
    });
    private KeySequence reloadKey = new KeySequence(new int[] {
            KeyEvent.KEYCODE_SEARCH,
            KeyEvent.KEYCODE_SEARCH,
            KeyEvent.KEYCODE_SEARCH,
            KeyEvent.KEYCODE_SEARCH,
    });
    private KeySequence testKey = new KeySequence(new int[] {
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_SEARCH,
            KeyEvent.KEYCODE_SEARCH,
    });
    private KeySequence sourceKey = new KeySequence(new int[] {
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_MENU,
            KeyEvent.KEYCODE_MENU,
    });

    private void showHtmlSource() {
        try {
            web.loadUrl("javascript:alert(document.getElementsByTagName('html')[0].innerHTML)");
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(this.getClass().getName(), "key pressed: " + keyCode);

        if (sourceKey.keyPressed(keyCode)) {
            showHtmlSource();
            return true;
        }
        if (backKey.keyPressed(keyCode)) {
            if (web.canGoBack()) {
                web.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        if (reloadKey.keyPressed(keyCode)) {
            web.loadUrl("javascript:window.location.reload()");
            return true;
        }
        if (testKey.keyPressed(keyCode)) {
            web.loadUrl("javascript:test()");
            return true;
        }

        if (progressDialogShowing) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (progressDialog != null)
                    progressDialog.hide();
                progressDialogShowing = false;
                web.stopLoading();
                if (web.canGoBack()) {
                    web.goBack();
                }
            }
            Log.i(this.getClass().getName(), "1");
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (menuVisible) {
                if (keyHandler.containsKey(keyCode)) {
                    String handler = keyHandler.get(keyCode);
                    web.loadUrl("javascript:" + handler + "()");
                    return true;
                }
            }
            if (web.canGoBack()) {
                web.goBack();
            }
            return true;
        }
            
        if (keyHandler.containsKey(keyCode)) {
            Log.i(this.getClass().getName(), "got handler");
            String handler = keyHandler.get(keyCode);
            web.loadUrl("javascript:" + handler + "()");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        Log.i(this.getClass().getName(), "onDestroy");
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialogShowing = false;
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (web != null) {
                // web.clearHistory();
                // web.clearFormData();
                web.clearCache(true);
                web.destroy();
                web = null;
            }
        } catch (Throwable t) {
            Log.e(TAG, "onDestroy", t);
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        web.saveState(outState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public class GenericWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.setVisibility(View.INVISIBLE);
                }
            });

            try {
                Configuration cookieConf = getConf(ConfKeyList.RemoveCookies);
                if (cookieConf != null && cookieConf.getBoolean(false)) {
                    cookieManager.removeAllCookie();
                    CookieSyncManager.getInstance().sync();
                    ContentProviderApi.delete(getContentResolver(), ConfKeyList.RemoveCookies.name());
                }
            } catch (Exception e2) {
                Log.e(this.getClass().getName(), "cookie handling error", e2);
            }

            menuVisible = false;
            numOfPageVisited++;
            Log.e(this.getClass().getName(), "onPageStarted:" + url);
            try {
                String msg = null;

                try {
                    int pos = url.indexOf("?");
                    if (pos != -1) {
                        String[] items = url.substring(pos + 1).split("&");
                        for (String s : items) {
                            if (s.startsWith("msg=")) {
                                msg = s.substring(4);
                                msg = URLDecoder.decode(msg, "UTF-8");
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (msg == null || msg.trim().length() == 0) {
                    WidgetMessage widgetMessage = getGenericApplication().getOneWidgetMessage();
                    msg = widgetMessage.getMsg();
                }
                if (progressDialog == null) {
                    if (!noloading) {
                        progressDialog = ProgressDialog.show(GenericWebActivity.this, null, msg);
                    }
                    progressDialogShowing = true;
                } else {
                    progressDialog.setMessage(msg);
                    progressDialog.show();
                    progressDialogShowing = true;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            keyHandler.clear();
            AndroidUtil.resetLastAccessTime((Context) GenericWebActivity.this);
            try {
                super.onPageStarted(view, url, favicon);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "error when loading " + url, e);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            syncCookie(url);
            lastSuccessPage = url;
            try {
                AndroidUtil.resetLastAccessTime(GenericWebActivity.this);
            } catch (Exception e) {
            }
            try {
                String lastVisitedPage = url;
                int pos = url.indexOf('?');
                if (pos != -1)
                    lastVisitedPage = url.substring(0, pos);
                ContentProviderApi.update(getContentResolver(), ConfKeyList.LastVisitedPage.name(), lastVisitedPage);
            } catch (Exception e) {
            }

            try {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
                progressDialogShowing = false;
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            keyHandler.clear();
            try {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
                progressDialogShowing = false;
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            WebViewClientError errorType = WebViewClientError.getErrorByCode(errorCode);
            Log.e(this.getClass().getName(), "page received error " + errorType + ", url is " + failingUrl);
            if (numOfPageVisited == 1) {
                GenericApplication application = (GenericApplication) getApplication();
                String home = application.getManifestHomeUrl();
                Log.i(this.getClass().getName(), "first visit. try url by manifest " + home);
                loadUrl(home);
            } else if (WebViewClientError.ERROR_TIMEOUT.equals(errorType)) {
            } else {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith("tel:"))
                loadUrl(url);
            return true;
        }
    }

    public String getMyPhoneNumber() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = mTelephonyMgr.getLine1Number();
        if (phoneNumber == null)
            phoneNumber = "8888888888";
        return phoneNumber;
    }

    public static class CachedData {
        public long time;
        public String content;
    }

    public class WebViewJavascriptInterface {
        private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        private HashMap<String, CachedData> cache = new HashMap<String, CachedData>();

        public String getClientType() {
            return "android";
        }

        public double getSavedLatitude() {
            DeviceLocation location = LocationReporter
                    .getSavedLocation(getContentResolver());
            if (location == null)
                return 0;
            else
                return location.getLatitude();
        }

        public double getSavedLongitude() {
            DeviceLocation location = LocationReporter
                    .getSavedLocation(getContentResolver());
            if (location == null)
                return 0;
            else
                return location.getLongitude();
        }

        public String getSavedLocationTime() {
            DeviceLocation location = LocationReporter
                    .getSavedLocation(getContentResolver());
            if (location == null)
                return null;

            Date date = new Date(location.getTime());
            return ReflectionUtil.getIso8601DateFormat().format(date);
        }

        public double getLatitude() {
            try {
                DeviceLocation location = AndroidUtil.getCurrentLocation(GenericWebActivity.this);
                return location == null ? getSavedLatitude() : location.getLatitude();
            } catch (Exception e) {
                e.printStackTrace();
                return getSavedLatitude();
            }
        }

        public double getLongitude() {
            try {
                DeviceLocation location = AndroidUtil.getCurrentLocation(GenericWebActivity.this);
                return location == null ? getSavedLongitude() : location.getLongitude();
            } catch (Exception e) {
                e.printStackTrace();
                return getSavedLongitude();
            }
        }

        public String getLocationTime() {
            try {
                DeviceLocation location = AndroidUtil.getCurrentLocation(GenericWebActivity.this);
                Date date = new Date(location.getTime());
                return ReflectionUtil.getIso8601DateFormat().format(date);
            } catch (Exception e) {
                return null;
            }
        }

        public int getViewWidth() {
            return web.getWidth();
        }

        public int getViewHeight() {
            return web.getHeight();
        }

        public int getDeviceWidth() {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            return width;
        }

        public int getDeviceHeight() {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            return height;
        }
        
        public void pickupImageFiles(int width, int height, String url, String keyValueJson,
                String successCallback, String errorCallback) {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", width);
                intent.putExtra("outputY", height);
                intent.putExtra("return-data", true);

                activityData.clear();
                activityData.put("form-url", url);
                activityData.put("form-data", keyValueJson);
                activityData.put("successCallback", successCallback);
                activityData.put("errorCallback", errorCallback);

                startActivityForResult(intent, GalleryIntentResult);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "cannot crop image", e);
            }
        }

        public void pickupPhotoFromGallery(int width, int height, String url, String keyValueJson,
                String successCallback, String errorCallback) {
            try {
                activityData.clear();
                activityData.put("form-url", url);
                activityData.put("form-data", keyValueJson);
                activityData.put("successCallback", successCallback);
                activityData.put("errorCallback", errorCallback);

                if (width > 0) {
                    activityData.put("imageWidth", width);
                    activityData.put("imageHeight", height);
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SelectPictureIntentResult);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "cannot pick image", e);
            }
        }

        public void takePicture(int width, int height, String url, String keyValueJson) {
        }

        public void startActivity(String activityName, String inputJson, String javascriptCallback) {
            try {
                Intent intent = new Intent(GenericWebActivity.this, Class.forName(activityName));
                GenericWebActivity.this.inputJson = inputJson;
                intent.putExtra("inputJson", inputJson);
                GenericWebActivity.this.javascriptCallback = javascriptCallback;
                startActivityForResult(intent, 0);
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        GenericWebActivity.this).create();
                alertDialog.setMessage("System error. Please report the bug.");
                alertDialog.setButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        }

        public void exit() {
            GenericWebActivity.this.finish();
        }

        public void back() {
            if (web.canGoBack()) {
                web.goBack();
            }
        }

        public String getMyPhoneNumber() {
            return AndroidUtil.getMyPhoneNumber(GenericWebActivity.this);
        }

        public void setTitle(String title) {
            GenericWebActivity.this.setTitle(title);
        }

        public void sendSms(String phone, String type, String subtype, String title, String msg, String url) {
            try {
                if (phone.indexOf("__") != -1) {
                    phone = DesEncrypter.selfdecrypt(phone).get(0);
                }
                SmsManager mng = SmsManager.getDefault();
                GenericMessage gm = new GenericMessage();
                gm.setTitle(title);
                gm.setType(MsgType.getType(type));
                gm.setSubtype(MsgType.getType(subtype));
                gm.setMsg(msg);
                gm.setUrl(url);
                String json = JsonUtil.toJsonString(gm);
                String encrypted = new DesEncrypter("XO").encrypt64(json);
                mng.sendTextMessage(phone, null, GenericWebActivity.this.getApplicationInfo().packageName + " " + json, null, null);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        }

        public void startSimpleActivity(String action, String data) {
            try {
                Intent intent = new Intent(action);
                intent.setData(Uri.parse(data));
                GenericWebActivity.this.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Failed to start activity. " + action + ", " + data,
                        e);
            }
        }

        public void map(String addr) {
            Uri uri = new Uri.Builder().scheme("http")
                    .authority("maps.google.com").encodedPath("maps")
                    .appendQueryParameter("f", "q")
                    .appendQueryParameter("q", addr).build();
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
            GenericWebActivity.this.startActivity(intent);
        }

        public void geo(float latitude, float longitude) {
            String url = "geo:" + latitude + "," + longitude;
            Log.i(TAG, "move to map " + url);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
            GenericWebActivity.this.startActivity(intent);
        }

        public void call(String phone) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                GenericWebActivity.this.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Failed to invoke call " + phone, e);
            }
        }

        public void setConf(String key, String value) {
            try {
                Log.i(this.getClass().getName(), "==== set conf " + key + " = " + value);
                ContentProviderApi.update(
                        GenericWebActivity.this.getContentResolver(), key, value);
            } catch (MultipleRecordsException e) {
                e.printStackTrace();
            }
        }

        public String getConf(String key) {
            try {
                List<Configuration> list = ContentProviderApi.list(
                        GenericWebActivity.this.getContentResolver(), key);
                if (list.size() == 0)
                    return null;
                if (list.size() == 1)
                    return list.get(0).getValue();
                JSONArray array = new JSONArray();
                for (Configuration conf : list) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put(conf.getKey(), conf.getValue());
                        array.put(json);
                    } catch (JSONException e) {
                    }
                }
                return array.toString();
            } catch (Exception e) {
                return null;
            }
        }

        public String getLog() {
            return null;
        }

        public String getCookie(String path) {
            return CookieManager.getInstance().getCookie(path);
        }

        public void broadcast(String msg) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(CommonAction.Test.getAction());
            broadcastIntent.putExtra("data", msg);
            // broadcastIntent.addCategory("common.android.category");
            sendBroadcast(broadcastIntent);
        }

        public void startApplication(String pkgName, String data) {
            PackageManager pm = GenericWebActivity.this.getPackageManager();

            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);

            for (ResolveInfo info : appList) {
                if (info.activityInfo.loadLabel(pm).equals(pkgName)) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    if (data != null) {
                        i.setData(Uri.parse(data));
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
                    GenericWebActivity.this.startActivity(i);
                }
            }
        }

        public boolean startActivity(String action, String uri, String type, String data, String extra) {
            try {
                Intent intent = null;

                if (uri == null)
                    intent = new Intent(action);
                else
                    intent = new Intent(action, Uri.parse(uri));

                if (type != null)
                    intent.setType(type);

                if (data != null)
                    intent.setData(Uri.parse(data));

                if (extra != null) {
                    JSONObject kvmap = JsonUtil.toJavaObject(extra, JSONObject.class);
                    Iterator keys = kvmap.keys();
                    while (keys.hasNext()) {
                        String key = keys.next().toString();
                        String value = kvmap.getString(key);
                        intent.putExtra(key, value);
                    }
                }

                GenericWebActivity.this.startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public String listFiles(String path) {
            File file = new File(path);
            String[] list = file.list();
            if (list == null)
                return "empty";
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append(",");
            }
            return sb.toString();
        }

        public void c2dmRegisterClient(String senderId) {
            C2DMessaging.register(GenericWebActivity.this, senderId);
        }

        public String getMyIpAddress() {
            return AndroidUtil.getMyIpAddress();
        }

        public String getGeocode(String address) {
            DeviceLocation location = Geocoder.getGeoFromAddress(address);
            if (location == null)
                return null;
            return "{\"lat\":\"" + location.getLatitude() + "\",\"lng\":\"" + location.getLongitude() + "\"}";
        }

        public String getAmazonItemLookupUrl(String ids) {
            String[] itemIdList = ids.split("[ ,;]+");
            AWSItemLookup lookup = new AWSItemLookup(itemIdList);
            try {
                return lookup.getRequestUrl();
            } catch (Exception e) {
                return null;
            }
        }

        public String getAmazonBrowseNodeLookupUrl(String ids) {
            String[] idList = ids.split("[ ,;]+");
            AWSBrowseNodeLookup lookup = new AWSBrowseNodeLookup(idList);
            try {
                return lookup.getRequestUrl();
            } catch (Exception e) {
                return null;
            }
        }

        public String getAmazonSimilarityLookupUrl(String ids) {
            AWSSimilarityLookup lookup = new AWSSimilarityLookup();
            try {
                lookup.setItemIdList(ids);
                return lookup.getRequestUrl();
            } catch (Exception e) {
                return null;
            }
        }

        public String getAmazonItemSearchUrl(String searchIndex, String keywords) {
            AWSItemSearch lookup = new AWSItemSearch();
            try {
                lookup.setSearchIndex(searchIndex);
                lookup.setKeywords(keywords);
                return lookup.getRequestUrl();
            } catch (Exception e) {
                Log.e("WebActivity", "cannot get search url for " + searchIndex + "," + keywords, e);
                return null;
            }
        }

        public String getAmazonSearchIndexs() {
            StringBuilder sb = new StringBuilder();
            for (AwsSearchIndex index : AwsSearchIndex.class.getEnumConstants()) {
                sb.append(index.name()).append(",");
            }
            return sb.substring(0, sb.length() - 1);
        }

        public String getHttpContent(String addr) {
            String key = addr;
            if (addr.startsWith("http://ecs.amazonaws.com")) {
                key = addr.substring(0, addr.indexOf("Timestamp="));
            }
            try {
                CachedData cd = cache.get(key);
                if (cd != null) {
                    Log.i(TAG, "cache found for " + key);
                    if ((System.currentTimeMillis() - cd.time) > 2 * 60 * 1000) {
                        Log.i(TAG, "too old");
                        cache.remove(key);
                    } else {
                        return cd.content;
                    }
                }

                DefaultHttpClient httpClient = ((GenericApplication) getApplication()).getHttpClient(addr);
                String resp = WebServiceUtil.get(httpClient, addr, String.class, "text/html");
                cd = new CachedData();
                cd.time = System.currentTimeMillis();
                cd.content = resp;
                Log.i(TAG, "save content for " + key + " to cache. total # of caches is " + cache.size());
                cache.put(key, cd);
                return resp;
            } catch (Exception e) {
                return null;
            }
        }

        public String getXmlContentAsJson(String addr) {
            try {
                String resp = getHttpContent(addr);
                InputSource inputSource = new InputSource(new StringReader(resp));
                Document document = factory.newDocumentBuilder().parse(inputSource);
                return JsonUtil.toJsonStr(document.getDocumentElement());
            } catch (Exception e) {
                Log.e("WebActivity", "cannot get xml for " + addr, e);
                e.printStackTrace();
                return null;
            }
        }

        public void zoomIn() {
            web.zoomIn();
        }

        public void zoomOut() {
            web.zoomOut();
        }

        public boolean hasMessage() {
            return !broadcastMsgs.isEmpty();
        }

        public String getMessage() {
            if (broadcastMsgs.isEmpty())
                return null;
            else
                return JsonUtil.toJsonString(broadcastMsgs.poll());
        }

        public String encrypt(String key, String important) {
            try {
                key = Security.md5(key).substring(4, 4);
                return new DesEncrypter(key).encrypt(important);
            } catch (Exception e) {
                return null;
            }
        }

        public String decrypt(String key, String encrypted) {
            try {
                key = Security.md5(key).substring(4, 4);
                return new DesEncrypter(key).decrypt(encrypted);
            } catch (Exception e) {
                return null;
            }
        }

        public String selfencrypt(String important, String otherInfo) {
            try {
                return DesEncrypter.selfencrypt(important, otherInfo);
            } catch (Exception e) {
                return null;
            }
        }

        public String selfdecrypt(String encrypted) {
            try {
                List<String> info = DesEncrypter.selfdecrypt(encrypted);
                StringBuilder sb = new StringBuilder();
                sb.append(info.get(0));
                for (int i = 1; i < info.size(); i++) {
                    sb.append("__").append(info.get(i));
                }
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }

        public void log(String log) {
            Log.i(this.getClass().getName(), log);
        }

        public String getApplicationInfo() {
            return AndroidUtil.getApplicationInfo(GenericWebActivity.this);
        }

        public void cancelNotification(int id) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (id == -1) {
                nm.cancelAll();
            } else {
                nm.cancel(id);
            }
        }

        public void resetLastAccessTime() {
            try {
                AndroidUtil.resetLastAccessTime(GenericWebActivity.this);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        }

        public void toast(String msg) {
            try {
                Toast.makeText(GenericWebActivity.this, msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        }

        public String getPhoneInfo() {
            try {
                PhoneInfo pi = AndroidUtil.getPhoneInfo(GenericWebActivity.this);
                pi.setDeviceHeight(getDeviceHeight());
                pi.setDeviceWidth(getDeviceWidth());
                return JsonUtil.toJsonString(pi);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "", e);
                return null;
            }
        }

        public void registerKeyHandler(int keyCode, String handler) {
            keyHandler.put(keyCode, handler);
        }

        public void unregisterKeyHandler(int keyCode) {
            keyHandler.remove(keyCode);
        }

        public void hideLoadingDialog() {
            try {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
                progressDialogShowing = false;
            } catch (Exception e) {
            }
        }

        public void showLoadingDialog() {
            try {
                if (progressDialog != null) {
                    progressDialog.show();
                }
                progressDialogShowing = true;
            } catch (Exception e) {
            }
        }

        public void disableLoadingDialog() {
            noloading = true;
        }

        public void enableLoadingDialog() {
            noloading = false;
        }

        public void setMenuVisible(boolean visible) {
            menuVisible = visible;
        }

        public String getApacheCookie(String domain, String name) {
            if(name == null) {
                return getGenericApplication().getCookieString(domain);
            } else {
                return getGenericApplication().getCookie(domain, name);
            }
        }

        public String getPhoneCookieValue() {
            return AndroidUtil.getPhoneCookieValue(getApplicationContext());
        }

        public boolean addWidgetMessage(String msg) {
            try {
                WidgetMessage widgetMessage = JsonUtil.toJavaObject(msg, WidgetMessage.class);
                getGenericApplication().saveWidgetMessage(widgetMessage);
                return true;
            } catch (Throwable t) {
                Log.i(this.getClass().getName(), "cannot save widget msg" + msg);
                return false;
            }
        }

        public void removeAllCookies() {
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        }

        public void removeSessionCookies() {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }

        public void dialogTo(String url) {
            dialog.loadUrl(url);
        }

        public void setDialogHeightAndScroll(final int height, final int scrollX, final int scrollY) {
            dialogWebViewClient.setScrollX(scrollX);
            dialogWebViewClient.setScrollY(scrollY);
            dialogWebViewClient.setHeight(height);
        }

        public void showDialog() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.setVisibility(View.VISIBLE);
                }
            });
        }

        public void hideDialog() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.setVisibility(View.INVISIBLE);
                }
            });
        }

        public void dialogPageLoaded(String html) {
            String print = html;
            if (print == null)
                print = "null";
            if (print.length() > 100)
                print = print.substring(0, 100);
            System.out.println("dialogPageLoaded with " + print);
            dialogHtml = html;
            web.loadUrl("javascript:dialogPageLoaded()");
        }

        public String getDialogHtml() {
            return dialogHtml;
        }
    }

    public class DialogWebViewClient extends WebViewClient {
        private int scrollX = 0;
        private int scrollY = 0;
        private int height = 200;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            dialog.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("dialog web client", "page finished " + url);
            dialog.loadUrl("javascript:Device.dialogPageLoaded(document.getElementsByTagName('html')[0].innerHTML)");
            super.onPageFinished(view, url);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dialog.getLayoutParams();
                    if (layoutParams.height != height && height > 0) {
                        layoutParams.height = height;
                        // dialog.setLayoutParams(layoutParams);
                    }

                    if (scrollX > 0 || scrollY > 0) {
                        dialog.scrollTo(scrollX, scrollY);
                    }
                }
            });
        }

        public void setScrollX(int scrollX) {
            this.scrollX = scrollX;
        }

        public int getScrollX() {
            return scrollX;
        }

        public void setScrollY(int scrollY) {
            this.scrollY = scrollY;
        }

        public int getScrollY() {
            return scrollY;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }
    }
}
