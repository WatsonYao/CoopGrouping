package watson.coopgrouping

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import kotlinx.coroutines.runBlocking

class SecondWidgetRemoteViewsFactory(
  private val context: Context,
  private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

  private var schedules: List<Stage> = emptyList()
//    private var coopSchedules: List<Stage> = emptyList() // For S2 coop data

  override fun onCreate() {
    fetchData()
  }

  override fun onDataSetChanged() {
    fetchData()
  }

  private fun fetchData() {
    runBlocking {
      try {
        // Fetch Splatoon 2 schedules
        val responseS2 = NetworkModule.apiService.getSplatoon2Schedules().execute()
        if (responseS2.isSuccessful) {
          responseS2.body()?.let {
            schedules = it.details ?: emptyList()
            //coopSchedules = it.schedules ?: emptyList()
          }
        } else {
          schedules = emptyList()
        }
      } catch (e: Exception) {
        schedules = emptyList()
      }
    }
  }

  override fun onDestroy() {
    schedules = emptyList()
  }

  override fun getCount(): Int {
    // Decide which list to use based on your needs, or combine them.
    // For simplicity, let's assume 'schedules' (details) is what we want to display primarily
    // or if coopSchedules is more relevant, use that.
    // If they represent different types of items, you might need different view types.
    return schedules.size // Or coopSchedules.size, or a combined size
  }

  override fun getViewAt(position: Int): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_item_second)
    // This example will use 'schedules' (Detail objects)
    // Adjust if you intend to display items from 'coopSchedules' or a mix
    if (position < schedules.size) {
      val item = schedules[position]

      var title = item.stage?.name ?: "Unknown Stage"
      var details = "Unknown Rule"
      // Splatoon 2 API might not have explicit start/end times in the same way for all schedule types
      // You'll need to adapt this based on the actual structure of Detail and Schedule objects
      var time = "${item.start_time} - ${item.end_time}" // Format this date/time

      // Example for coopSchedules if you were to use it:
      // val coopItem = coopSchedules[position]
      // title = coopItem.stage?.name ?: "Coop Stage"
      // details = "Weapons: ${coopItem.weapons?.joinToString { it.weapon?.name ?: "Unknown" }}"
      // time = "${coopItem.start_time} - ${coopItem.end_time}"

      views.setTextViewText(R.id.widget_item_second_title, title)
      views.setTextViewText(R.id.widget_item_second_details, details)
      views.setTextViewText(R.id.widget_item_second_time, time) // TODO: Format time properly

      val fillInIntent = Intent()
      // fillInIntent.putExtra("item_id", item.someUniqueId) // Example
      views.setOnClickFillInIntent(R.id.widget_item_second_title, fillInIntent)
    }
    return views
  }

  override fun getLoadingView(): RemoteViews? {
    return null
  }

  override fun getViewTypeCount(): Int {
    return 1
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun hasStableIds(): Boolean {
    return false
  }
}
