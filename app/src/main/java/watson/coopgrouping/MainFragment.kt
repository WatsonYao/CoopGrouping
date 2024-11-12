package watson.coopgrouping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

  private val retrofit = Retrofit.Builder()
    .baseUrl("https://splatoon3.ink/data/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  private val scheduleService = retrofit.create(ScheduleService::class.java)

  private suspend fun fetchSchedules(): JSON {
    return scheduleService.getSchedules()
  }

  private val recyclerView: RecyclerView
    get() = requireView().findViewById(R.id.recyclerView1)

  private val swipeRefreshLayout: SwipeRefreshLayout
    get() = requireView().findViewById(R.id.swipeRefreshLayout)

  private val itemsReg = mutableListOf<Node>()
  private val itemsTeam = mutableListOf<Node>()

  private val adapterReg by lazy {
    DateAdapter(0, requireContext(), itemsReg) {}
  }
  private val adapterTeam by lazy {
    DateAdapter(1, requireContext(), itemsTeam) {}
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    swipeRefreshLayout.setOnRefreshListener {
      loadData()
    }


    val config = ConcatAdapter.Config.Builder()
      //.setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
      .setIsolateViewTypes(true).build()

    val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = ConcatAdapter(config, adapterTeam, adapterReg)

    loadData()
  }

  private var loading = false

  private fun loadData() {
    if (loading) return
    loading = true
    try {
      lifecycleScope.launch {
        val result = fetchSchedules()
        withContext(Dispatchers.IO) {
          itemsReg.clear()
          itemsReg.addAll(result.data.coopGroupingSchedule.regularSchedules.nodes)
          itemsTeam.clear()
          if (result.data.coopGroupingSchedule.teamContestSchedules.nodes.isNotEmpty()) {
            itemsTeam.addAll(result.data.coopGroupingSchedule.teamContestSchedules.nodes)
          }
        }
        loading = false
        swipeRefreshLayout.isRefreshing = false
        adapterTeam.notifyDataSetChanged()
        adapterReg.notifyDataSetChanged()
      }
    } catch (e: Exception) {
      e.printStackTrace()
      log(e.printStackTrace().toString())
    }
  }
}





