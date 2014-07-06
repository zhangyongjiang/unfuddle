package common.android;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import common.android.contentprovider.ContentProviderApi;
import common.util.web.ServiceException;
import common.util.web.WebServiceUtil;

public class NotificationPoller extends IntentService {
    public NotificationPoller() {
        super(NotificationPoller.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        pollNotification(intent);
        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(getApplicationContext(), WidgetProvider.class);
        intent.setData(Uri.parse(WidgetProvider.UPDATE_ACTION));
        sendBroadcast(intent);
    }

    protected void pollNotification(Intent intent) {
        Context context = getApplicationContext();
        try {
            int pollerCounter = ContentProviderApi.getIntConf(getContentResolver(), "pollerCounter", 0);
            pollerCounter++;
            if (AndroidUtil.getUserIdleTime(context) > 600000 && pollerCounter < 20) {
                Log.i("poller zzz", pollerCounter + "");
                ContentProviderApi.update(getContentResolver(), "pollerCounter", pollerCounter + "");
                return;
            }
            pollerCounter = 0;
            ContentProviderApi.update(getContentResolver(), "pollerCounter", pollerCounter + "");
            Log.i("poller", pollerCounter + "");

            long lastSeenNotificationId = ContentProviderApi.getLongConf(context.getContentResolver(), ConfKeyList.LastSeenNotificationId.name(), 0l);
            String getfrom = "/notification/after?mark=true&unread=true&after=" + lastSeenNotificationId;
            String baseUrl = ((GenericApplication) getApplication()).getServerBaseUrl();
            getfrom = baseUrl + getfrom;
            DefaultHttpClient httpclient = ((GenericApplication) getApplication()).getHttpClient(getfrom);
            String phoneCookie = AndroidUtil.getPhoneCookieValue(context);
            String domain = WebServiceUtil.getDomain(getfrom);
            WebServiceUtil.addCookie(httpclient, "p", phoneCookie, domain);
            NotificationList list = WebServiceUtil.get(httpclient, getfrom, NotificationList.class);

            if (list != null && list.getList() != null) {
                long newLastSeen = lastSeenNotificationId;
                for (GenericMessage gm : list.getList()) {
                    if (gm.getId() > newLastSeen) {
                        newLastSeen = gm.getId();
                    }
                    MessageProcessor.getInstance().processMessage(context, gm);
                }
                if (newLastSeen > lastSeenNotificationId) {
                    ContentProviderApi.update(context.getContentResolver(), ConfKeyList.LastSeenNotificationId.name(), newLastSeen + "");
                }
            }
        } catch (ServiceException e) {
            String msg = e.getMessage();
            if (msg != null && msg.length() > 128)
                msg = msg.substring(0, 128);
            Log.e(this.getClass().getName(), "===NotiPoS=== errorCode: " + e.getErrorCode() + ", " + WebViewClientError.getErrorByCode(e.getErrorCode()) + ", " + msg);
        } catch (java.net.SocketException se) {
            Log.i(this.getClass().getName(), "==== SocketException" + se.getMessage());
        } catch (ConnectTimeoutException e) {
            Log.i(this.getClass().getName(), "==== noti poller ConnectTimeoutException ");
        } catch (Throwable e) {
            Log.e(this.getClass().getName(), "", e);
        }
	}
}
