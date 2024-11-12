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

class SecondFragment : Fragment() {

  private val retrofit = Retrofit.Builder()
    .baseUrl("https://splatoon2.ink/data/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  private val scheduleService = retrofit.create(ScheduleService2::class.java)

  private suspend fun fetchSchedules(): JSON2 {
    return scheduleService.getSchedules()
  }

  private val recyclerView: RecyclerView
    get() = requireView().findViewById(R.id.recyclerView1)

  private val swipeRefreshLayout: SwipeRefreshLayout
    get() = requireView().findViewById(R.id.swipeRefreshLayout)

  private val itemsReg = mutableListOf<Stage>()

  private val adapterReg by lazy {
    DateAdapter2(requireContext(), itemsReg) {}
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    swipeRefreshLayout.setOnRefreshListener {
      loadData()
    }

    val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapterReg

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
          itemsReg.addAll(result.details)
        }
        loading = false
        swipeRefreshLayout.isRefreshing = false
        adapterReg.notifyDataSetChanged()
      }
    } catch (e: Exception) {
      e.printStackTrace()
      log(e.printStackTrace().toString())
    }
  }
}





