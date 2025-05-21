package watson.coopgrouping

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import watson.coopgrouping.R
import android.net.Uri // Added import
import android.app.PendingIntent // Added import

class SecondWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            // Create RemoteViews for the layout
            val views = RemoteViews(context.packageName, R.layout.widget_second_layout)

            // Create an Intent to launch SecondWidgetService
            val intent = Intent(context, SecondWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            // Set a unique data URI for the intent
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            // Set up the RemoteViews object to use a RemoteViewsAdapter
            views.setRemoteAdapter(R.id.widget_second_listview, intent)

            // Finally, update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // Handle any specific broadcast intents here, if needed
    }
}
