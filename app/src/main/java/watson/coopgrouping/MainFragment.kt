package watson.coopgrouping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainFragment : Fragment() {

  private suspend fun fetchSchedules(): JSON3? {
    return try {
      val response = NetworkModule.apiService.getSplatoon3Schedules().execute()
      if (response.isSuccessful) {
        response.body()
      } else {
        // 处理 HTTP 错误响应
        println("获取 Schedules 时出错: ${response.code()} ${response.message()}")
        null
      }
    } catch (e: IOException) {
      // 处理网络错误或 IO 错误
      println("获取 Schedules 时出错: ${e.message}")
      null
    } catch (e: HttpException) {
      // 处理 Retrofit 特有的 HTTP 异常
      println("获取 Schedules 时出错 (HttpException): ${e.message()}")
      null
    } catch (e: Exception) {
      // 处理 JSON 解析错误或其他意外错误
      println("解析 Schedules 时出错: ${e.message}")
      null
    }
  }

  private val recyclerView: RecyclerView
    get() = requireView().findViewById(R.id.recyclerView1)

  private val swipeRefreshLayout: SwipeRefreshLayout
    get() = requireView().findViewById(R.id.swipeRefreshLayout)

  private val itemsReg = mutableListOf<Node>()
  private val itemsTeam = mutableListOf<Node>()
  private val itemsBigRun = mutableListOf<Node>()

  private val adapterReg by lazy {
    DateAdapter(0, requireContext(), itemsReg) {}
  }
  private val adapterTeam by lazy {
    DateAdapter(1, requireContext(), itemsTeam) {}
  }
  private val adapterBigRun by lazy {
    DateAdapter(2, requireContext(), itemsBigRun) {}
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
    recyclerView.adapter = ConcatAdapter(config, adapterBigRun, adapterTeam, adapterReg)

    log("f create")
    loadData()
  }

  private var loading = false

  private fun loadData() {
    if (loading) return
    loading = true
    try {
      lifecycleScope.launch {
        withContext(Dispatchers.IO) {
          val result = fetchSchedules() ?: return@withContext
          itemsReg.clear()
          itemsReg.addAll(result.data.coopGroupingSchedule.regularSchedules.nodes)
          itemsTeam.clear()
          if (result.data.coopGroupingSchedule.teamContestSchedules.nodes.isNotEmpty()) {
            itemsTeam.addAll(result.data.coopGroupingSchedule.teamContestSchedules.nodes)
          }
          itemsBigRun.clear()
          if (result.data.coopGroupingSchedule.bigRunSchedules.nodes.isNotEmpty()) {
            itemsBigRun.addAll(result.data.coopGroupingSchedule.bigRunSchedules.nodes)
          }
        }
        loading = false
        swipeRefreshLayout.isRefreshing = false
        adapterTeam.notifyDataSetChanged()
        adapterBigRun.notifyDataSetChanged()
        adapterReg.notifyDataSetChanged()
      }
    } catch (e: Exception) {
      e.printStackTrace()
      log(e.printStackTrace().toString())
    }
  }
}





