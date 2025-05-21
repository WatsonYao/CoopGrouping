package watson.coopgrouping

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import watson.coopgrouping.R

class SecondWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_second_layout)
            // TODO: Set up the RemoteViewsService intent here
            // Example:
            // val intent = Intent(context, SecondWidgetService::class.java)
            // views.setRemoteAdapter(R.id.widget_second_listview, intent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // Handle any specific broadcast intents here, if needed
    }
}
