package com.gaoshin.fandroid;

import java.util.List;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public abstract class WidgetProvider extends AppWidgetProvider {
    public static final String UPDATE_ACTION = "common.android.UPDATE_APP_WIDGET";
    private static final int[] txtResIds = {
            R.id.text00, R.id.text01, R.id.text02, R.id.text03,
            R.id.text10, R.id.text11, R.id.text12, R.id.text13,
            R.id.text20, R.id.text21, R.id.text22, R.id.text23,
            R.id.text30, R.id.text31, R.id.text32, R.id.text33,
    };
    private static final int[] imgResIds = {
            R.id.img00, R.id.img01, R.id.img02, R.id.img03,
            R.id.img10, R.id.img11, R.id.img12, R.id.img13,
            R.id.img20, R.id.img21, R.id.img22, R.id.img23,
            R.id.img30, R.id.img31, R.id.img32, R.id.img33,
    };
    private static final int[] contactIds = {
            R.id.contact00, R.id.contact01, R.id.contact02, R.id.contact03,
            R.id.contact10, R.id.contact11, R.id.contact12, R.id.contact13,
            R.id.contact20, R.id.contact21, R.id.contact22, R.id.contact23,
            R.id.contact30, R.id.contact31, R.id.contact32, R.id.contact33,
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getData() != null && intent.getData().toString().equals(UPDATE_ACTION)) {
            RemoteViews views = updateWidget(context);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, this.getClass());
            appWidgetManager.updateAppWidget(componentName, views);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews views = updateWidget(context);
            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }

    private RemoteViews updateWidget(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        List<RecentItem> recents = getRecents(context);
        int i = 0;
        for (; i < 16 && i < recents.size(); i++) {
            views.setViewVisibility(contactIds[i], View.VISIBLE);
            setupItem(recents.get(i), txtResIds[i], imgResIds[i], views);
        }

        for(;i<16; i++) {
            views.setViewVisibility(contactIds[i], View.INVISIBLE);
        }
        return views;
    }

    private void setupItem(RecentItem recentItem, int txtResId, int imgResId, RemoteViews views) {
        if (recentItem.getText() != null) {
            views.setTextViewText(txtResId, recentItem.getText());
        }

        if (recentItem.getIcon() != null) {
            views.setImageViewBitmap(imgResId, recentItem.getIcon());
        }

        if (recentItem.getIntent() != null) {
            views.setOnClickPendingIntent(txtResId, recentItem.getIntent());
            views.setOnClickPendingIntent(imgResId, recentItem.getIntent());
        }
    }

    protected abstract List<RecentItem> getRecents(Context context);
}

