package common.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.gaoshin.topseller.R;
import common.util.web.JsonUtil;

public class WidgetProvider extends AppWidgetProvider {
    public static final String UPDATE_ACTION = "common.android.UPDATE_APP_WIDGET";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getData() != null && intent.getData().toString().equals(UPDATE_ACTION)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            WidgetMessage widgetMessage =
                    GenericApplication.getOneWidgetMessage(context.getContentResolver());
            String display = widgetMessage.getMsg() + "\n......";
            remoteViews.setTextViewText(R.id.testWidgetTextView1, display);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, this.getClass());
            appWidgetManager.updateAppWidget(componentName, remoteViews);

            GenericMessage gm = new GenericMessage();
            gm.setUrl("/m/widget");
            gm.setType(MsgType.Web);
            String json = JsonUtil.toJsonString(gm);
            Intent clickIntent = new Intent(context, GenericWebActivity.class);
            clickIntent.setData(Uri.parse(json));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            remoteViews.setOnClickPendingIntent(R.id.testWidgetImageButton1,
                    pendingIntent);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            WidgetMessage widgetMessage =
                    GenericApplication.getOneWidgetMessage(context.getContentResolver());
            String display = widgetMessage.getMsg() + "\n......";
            views.setTextViewText(R.id.testWidgetTextView1, display);
            appWidgetManager.updateAppWidget(widgetId, views);

            GenericMessage gm = new GenericMessage();
            gm.setUrl("/m/widget");
            gm.setType(MsgType.Web);
            String json = JsonUtil.toJsonString(gm);
            Intent intent = new Intent(context, GenericWebActivity.class);
            intent.setData(Uri.parse(json));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, 0);
            views.setOnClickPendingIntent(R.id.testWidgetImageButton1,
                    pendingIntent);
        }
    }
}

