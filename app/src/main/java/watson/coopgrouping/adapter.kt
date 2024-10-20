package watson.coopgrouping

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateAdapter(
  private val context: Context,
  private val items: List<Node>,
  private val onItemClick: (Node) -> Unit
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val monthStart: TextView = view.findViewById(R.id.monthStart)
    val monthEnd: TextView = view.findViewById(R.id.monthEnd)
    val timeStart: TextView = view.findViewById(R.id.timeStart)
    val timeEnd: TextView = view.findViewById(R.id.timeEnd)
    val diff: TextView = view.findViewById(R.id.diff)
    val image: ImageView = view.findViewById(R.id.image)
    val w1: ImageView = view.findViewById(R.id.w1)
    val w2: ImageView = view.findViewById(R.id.w2)
    val w3: ImageView = view.findViewById(R.id.w3)
    val w4: ImageView = view.findViewById(R.id.w4)
    val boss: ImageView = view.findViewById(R.id.boss)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date, parent, false)
    return ViewHolder(view)
  }

  private val nowTime by lazy{
    ZonedDateTime.now(ZoneId.systemDefault())
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: DateAdapter.ViewHolder, position: Int) {
    val item = items[position]
    val dateStart = localTime(item.startTime)
    val dateEnd = localTime(item.endTime)
    holder.timeStart.text = dateStart.format(formatterTime)
    holder.timeEnd.text = dateEnd.format(formatterTime)
    holder.monthStart.text = queryDate(dateStart)
    holder.monthEnd.text = queryDate(dateEnd)
    holder.itemView.setOnClickListener { onItemClick(item) }
    if(dateStart.isBefore(nowTime) && dateEnd.isAfter(nowTime)) {
      holder.diff.visibility = View.VISIBLE
      holder.diff.text = "剩 ${nowTime.until(dateEnd, ChronoUnit.HOURS)} 小时"
    }else{
      holder.diff.visibility = View.GONE
      holder.diff.text = ""
    }
    val stage = item.setting.coopStage.thumbnailImage.url
    Glide.with(holder.itemView.context).load(stage)
      .apply(RequestOptions.bitmapTransform(roundedCorners))
      .into(holder.image)
    listOf(holder.w1, holder.w2, holder.w3, holder.w4).forEachIndexed { index, imageView ->
      Glide.with(holder.itemView.context).load(item.setting.weapons[index].image.url)
        .into(imageView)
    }
    val bossUrl = bossMap[item.setting.boss.name]
    if (bossUrl != null) {
      holder.boss.visibility = View.VISIBLE
      Glide.with(holder.itemView.context).load(bossUrl).into(holder.boss)
    } else {
      holder.boss.visibility = View.GONE
    }
  }

  private fun queryDate(date: ZonedDateTime): String {
    return "${date.format(formatterMonth)}月${date.format(formatterDay)}，周${getDayOfWeek(date)}"
  }

  private val roundedCorners: RoundedCorners by lazy {
    val px = context.resources.displayMetrics.density * 12
    RoundedCorners(px.toInt())
  }

  private val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
  private val formatterDay = DateTimeFormatter.ofPattern("dd")
  private val formatterMonth = DateTimeFormatter.ofPattern("MM")

  private val bossMap: HashMap<String, Int> by lazy {
    val map = HashMap<String, Int>()
    map["Cohozuna"] = R.drawable.hg
    map["Horrorboros"] = R.drawable.cl
    map["Megalodontia"] = R.drawable.dz
    map["Triumvirate"] = R.drawable.sg
    // 还有一个随机的图标
    map
  }

  override fun getItemCount() = items.size
}




