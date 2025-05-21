package watson.coopgrouping

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import watson.coopgrouping.R

class MainWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_main_layout)
            // TODO: Set up the RemoteViewsService intent here
            // Example:
            // val intent = Intent(context, MainWidgetService::class.java)
            // views.setRemoteAdapter(R.id.widget_main_listview, intent)
            val intent = Intent(context, MainWidgetService::class.java).apply {
                // Add the app widget ID to the intent extras.
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                // When intents are compared, the extras are ignored, so embed
                // the extras into the data so that the extras are not ignored.
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            // Instantiate the RemoteViews object for the app widget layout.
            val rv = RemoteViews(context.packageName, R.layout.widget_main_layout).apply {
                // Set up the RemoteViews object to use a RemoteViewsAdapter.
                // This adapter connects to a RemoteViewsService through the specified intent.
                // This is how you populate the data.
                setRemoteAdapter(R.id.widget_main_listview, intent) // R.id.my_collection_view is the ID of your ListView, GridView, etc.

                // The empty view is displayed when the collection has no items.
                // It should be a sibling of the collection view.
                //setEmptyView(R.id.widget_main_empty_view, R.id.my_empty_view)
            }
            appWidgetManager.updateAppWidget(appWidgetId, rv)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // Handle any specific broadcast intents here, if needed
    }
}
