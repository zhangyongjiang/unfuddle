package com.gaoshin.coupon.android;

import java.net.URLEncoder;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gaoshin.coupon.android.model.CityList;
import com.gaoshin.coupon.android.model.Client;
import com.gaoshin.coupon.android.model.ConfKey;
import com.gaoshin.coupon.android.model.ShoplocalCrawlHistory;
import com.gaoshin.coupon.android.model.WebPage;

public class TestActivity extends CouponActivity {
    private boolean stopped = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        setContentView(btn);
        btn.setText("TEST");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    test1();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        stopped = true;
    }

    public void test() throws Exception {
        String location = "SAN FRANCISCO, CA";
        String targetUrl = "http://www.shoplocal.com/childrens-apparel.fp";
        String locationUrl = "http://www.shoplocal.com/ChangeShoppingZoneHandler.ashx?redirect=" +
                URLEncoder.encode(targetUrl, "UTF-8") +
                "&newzone=Y&citystatezip=" + URLEncoder.encode(location, "UTF-8") + "&x=0&y=0";
        
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    return getApp().getWebClient().httpGet(params[0], String.class, "text/html");
                }
                catch (BusinessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
            @Override
            protected void onPostExecute(String html) {
                AlertDialog dialog = new AlertDialog.Builder(TestActivity.this).create();
                dialog.setTitle("Alert");
                dialog.setMessage(html);
                dialog.show();
            }
        }.execute(locationUrl);
    }
    
    class ShoplocalTask extends AsyncTask<ShoplocalCrawlHistory, Void, String> {
        @Override
        protected String doInBackground(ShoplocalCrawlHistory...tasks) {
            try {
                final String serverBase = getApp().getConfService().get(ConfKey.ServerBase).getValue();
                Client device = getApp().getDevice();
                Client saved = getApp().getConfService().get(ConfKey.DeviceLocation, Client.class);
                if(saved == null) {
                    String path = serverBase + "/ws/location/set-location/" + device.getLatitude() + "/" + device.getLongitude();
                    CityList cityList = getApp().getWebClient().httpGet(path, CityList.class);
                    if(cityList.getItems().size() > 0) {
                        device.setCity(cityList.getItems().get(0).getCity());
                        device.setState(cityList.getItems().get(0).getState());
                        getApp().getConfService().save(ConfKey.DeviceLocation, device);
                    }
                }
                String html = getApp().getWebClient().httpGetHtml(tasks[0].getUrl());
                WebPage page = new WebPage();
                page.setContent(html);
                page.setId(tasks[0].getId());
                page.setClient(device);
                
                String fullUrl = serverBase + "/ws/coupon/upload-shoplocal-result";
                getApp().getWebClient().httpPost(fullUrl , String.class, page);
                
                if(!stopped) {
                    try {
                        test1();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (BusinessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void test1() throws Exception {
        try {
            final String serverBase = getApp().getConfService().get(ConfKey.ServerBase).getValue();
            Client device = getApp().getDevice();
            Client saved = getApp().getConfService().get(ConfKey.DeviceLocation, Client.class);
            if(saved == null) {
                String path = serverBase + "/ws/location/set-location/" + device.getLatitude() + "/" + device.getLongitude();
                CityList cityList = getApp().getWebClient().httpGet(path, CityList.class);
                if(cityList.getItems().size() > 0) {
                    device.setCity(cityList.getItems().get(0).getCity());
                    device.setState(cityList.getItems().get(0).getState());
                    getApp().getConfService().save(ConfKey.DeviceLocation, device);
                }
            }
            String url = serverBase + "/ws/coupon/get-shoplocal-task";
            ShoplocalCrawlHistory task = getApp().getWebClient().httpPost(url, ShoplocalCrawlHistory.class, device);
            if(task != null)
                new ShoplocalTask().execute(task);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
