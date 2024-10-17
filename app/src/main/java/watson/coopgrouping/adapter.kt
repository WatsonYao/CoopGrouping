package watson.coopgrouping

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateAdapter(
  private val items: List<Node>, private val onItemClick: (Node) -> Unit
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {


  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val monthStart: TextView = view.findViewById(R.id.monthStart)
    val monthEnd: TextView = view.findViewById(R.id.monthEnd)
    val timeStart: TextView = view.findViewById(R.id.timeStart)
    val timeEnd: TextView = view.findViewById(R.id.timeEnd)
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
    val stage = item.setting.coopStage.thumbnailImage.url
    Glide.with(holder.itemView.context).load(stage).into(holder.image)
    listOf(holder.w1, holder.w2, holder.w3, holder.w4).forEachIndexed { index, imageView ->
      Glide.with(holder.itemView.context).load(item.setting.weapons[index].image.url)
        .into(imageView)
    }
    Glide.with(holder.itemView.context).load(bossMap[item.setting.boss.name]).into(holder.boss)
  }

  private fun queryDate(date: ZonedDateTime): String {
    return "${date.format(formatterMonth)}月${date.format(formatterDay)}，周${getDayOfWeek(date)}"
  }

  // 格式化输出，你可以自定义格式
  private val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
  private val formatterDay = DateTimeFormatter.ofPattern("dd")
  private val formatterMonth = DateTimeFormatter.ofPattern("MM")
//    return

  private val bossMap: HashMap<String, Int> by lazy {
    val map = HashMap<String, Int>()
    map["Cohozuna"] = R.drawable.hg
    map["Horrorboros"] = R.drawable.cl
    map["Megalodontia"] = R.drawable.dz
    map["Triumvirate"] = R.drawable.sg
    map
  }

  override fun getItemCount() = items.size
}




