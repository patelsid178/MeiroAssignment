package com.meiro.meiroassignmentsiddharth;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Create RemoteViews for the widget layout
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);


            Intent intent = new Intent(context, scannerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.buttonClickImage, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);



            ////////////////////////////////////////////////////////////


            Intent intent2 = new Intent(context, LocationForegroundService.class);
            intent2.setAction("START_LOCATION_SERVICE");
            PendingIntent pendingIntent2 = PendingIntent.getService(context, 0, intent2, 0);
            views.setOnClickPendingIntent(R.id.startButton, pendingIntent2);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
