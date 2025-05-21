package watson.coopgrouping

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import kotlinx.coroutines.runBlocking
import watson.coopgrouping.R
import watson.coopgrouping.databinding.FragmentMainBinding
import watson.coopgrouping.network.NetworkModule
import watson.coopgrouping.objetos.RegularSchedule
import watson.coopgrouping.objetos.TeamSchedule
import watson.coopgrouping.objetos.BigRunSchedule

class MainWidgetRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private var regularSchedules: List<RegularSchedule> = emptyList()
    private var teamSchedules: List<TeamSchedule> = emptyList()
    private var bigRunSchedules: List<BigRunSchedule> = emptyList()
    private var combinedSchedules: MutableList<Any> = mutableListOf()


    override fun onCreate() {
        // Initialize data structures if needed
        fetchData()
    }

    override fun onDataSetChanged() {
        // This is called by the system to update the data
        fetchData()
    }

    private fun fetchData() {
        // Perform network request synchronously
        // Note: This runs on a binder thread, not the main thread.
        // Consider error handling and loading states more robustly in a real app.
        runBlocking {
            try {
                val response = NetworkModule.apiService.getSplatoon3Schedules()
                if (response.isSuccessful) {
                    response.body()?.let {
                        regularSchedules = it.regularSchedules?.nodes ?: emptyList()
                        teamSchedules = it.teamSchedules?.nodes ?: emptyList()
                        bigRunSchedules = it.bigRunSchedules?.nodes ?: emptyList()

                        combinedSchedules.clear()
                        combinedSchedules.addAll(regularSchedules)
                        combinedSchedules.addAll(teamSchedules)
                        combinedSchedules.addAll(bigRunSchedules)
                        // TODO: Sort schedules by start time if necessary
                    }
                } else {
                    // Handle error
                    combinedSchedules.clear()
                }
            } catch (e: Exception) {
                // Handle exception
                combinedSchedules.clear()
            }
        }
    }

    override fun onDestroy() {
        // Clean up any resources
        combinedSchedules.clear()
    }

    override fun getCount(): Int {
        return combinedSchedules.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_item_main)
        val item = combinedSchedules[position]

        var title = "Unknown Schedule"
        var details = ""
        var time = ""

        when (item) {
            is RegularSchedule -> {
                title = item.setting?.rule?.name ?: "Regular Coop"
                details = item.setting?.coopStage?.name ?: "Unknown Stage"
                time = "${item.startTime} - ${item.endTime}" // Format this date/time
            }
            is TeamSchedule -> {
                title = item.setting?.rule?.name ?: "Team Coop"
                details = item.setting?.coopStage?.name ?: "Unknown Stage"
                time = "${item.startTime} - ${item.endTime}" // Format this date/time
            }
            is BigRunSchedule -> {
                title = "Big Run"
                details = item.setting?.coopStage?.name ?: "Unknown Stage"
                time = "${item.startTime} - ${item.endTime}" // Format this date/time
            }
        }

        views.setTextViewText(R.id.widget_item_main_title, title)
        views.setTextViewText(R.id.widget_item_main_details, details)
        views.setTextViewText(R.id.widget_item_main_time, time) // TODO: Format time properly

        // Set up fill-in intent for item click (optional)
        val fillInIntent = Intent()
        // fillInIntent.putExtra("item_id", item.id) // Example
        views.setOnClickFillInIntent(R.id.widget_item_main_title, fillInIntent) // Or the root layout of the item

        return views
    }

    override fun getLoadingView(): RemoteViews? {
        // Optional: Return a custom loading view
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1 // Assuming all items have the same layout
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Or a unique ID if available
    }

    override fun hasStableIds(): Boolean {
        return false // Set to true if item IDs are stable
    }
}
