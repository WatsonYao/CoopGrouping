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
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainFragment : Fragment() {

  // 创建一个 OkHttpClient 实例 (可以考虑复用，例如作为类的成员变量)
  private val httpClient = OkHttpClient()
  // 创建一个 Gson 实例 (也可以考虑复用)
  private val gson = Gson()

  private fun fetchSchedules(): JSON3? {
    // 1. 确定完整的 URL
    val url = "https://splatoon3.ink/data/schedules.json"

    // 2. 创建一个 Request 对象
    val request = Request.Builder()
      .url(url)
      .get() // 明确是 GET 请求 (虽然没有请求体时默认是 GET)
      .build()

    return try {
      // 3. 使用 OkHttpClient 执行请求
      // execute() 是同步方法，会在当前线程阻塞直到收到响应
      httpClient.newCall(request).execute().use { response -> // 使用 use 来确保 response 被关闭

        // 4. 检查响应是否成功
        if (!response.isSuccessful) {
          throw IOException("请求失败: ${response.code()} ${response.message()}")
        }

        // 5. 获取响应体
        val responseBody = response.body()
        if (responseBody == null) {
          throw IOException("响应体为空")
        }

        // 6. 读取响应体内容为字符串并使用 Gson 解析
        // responseBody.string() 会读取整个响应体并关闭它
        // 如果在 use 块之外访问 responseBody，需要手动关闭
        val jsonString = responseBody.string()
         gson.fromJson(jsonString, JSON3::class.java)
      }
    } catch (e: IOException) {
      // 处理网络错误或 IO 错误
      println("获取 Schedules 时出错: ${e.message}")
      // 你可能想根据具体情况抛出自定义异常或返回 null/默认值
//      throw IOException("无法获取 Schedules 数据", e)
      null
    } catch (e: Exception) {
      // 处理 JSON 解析错误或其他意外错误
      println("解析 Schedules 时出错: ${e.message}")
//      throw RuntimeException("无法解析 Schedules 数据", e)
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





