package common.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = BootReceiver.class.getSimpleName() + "------";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "BootReceiver starting ...");

        try {
            if(ContentProviderApi.getBooleanConf(context.getContentResolver(), ConfKeyList.EnableLocationReport.name(), false)) {
                GenericApplication.startLocationReportService(context);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            GenericApplication.startNotificationPollerService(context);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            GenericApplication.startLogSenderService(context);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }

        try {
            GenericApplication.setupWidgetUpdateTimer(context);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "", e);
        }
	}
}
