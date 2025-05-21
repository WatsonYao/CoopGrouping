package watson.coopgrouping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SecondFragment : Fragment() {

  private suspend fun fetchSchedules(): JSON2? {
    return try {
      val response = NetworkModule.apiService.getSplatoon2Schedules().execute()
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
        withContext(Dispatchers.IO) {
          val result = fetchSchedules() ?: return@withContext
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





