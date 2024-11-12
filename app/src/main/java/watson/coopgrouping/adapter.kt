package watson.coopgrouping

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
  private val type: Int,//0 reg 1 team
  private val context: Context,
  private val items: List<Node>,
  private val onItemClick: (Node) -> Unit
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layout: View = view.findViewById(R.id.layout)
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


  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: DateAdapter.ViewHolder, position: Int) {
    val nowTime = ZonedDateTime.now(ZoneId.systemDefault())
    val item = items[position]
    val dateStart = localTime(item.startTime)
    val dateEnd = localTime(item.endTime)
    val current = dateStart.isBefore(nowTime) && dateEnd.isAfter(nowTime)

    holder.timeStart.text = dateStart.format(formatterTime)
    holder.timeEnd.text = dateEnd.format(formatterTime)
    holder.monthStart.text = queryDate(dateStart)
    holder.monthEnd.text = queryDate(dateEnd)
    holder.itemView.setOnClickListener { onItemClick(item) }
    val stage = item.setting.coopStage.thumbnailImage.url
    Glide.with(holder.itemView.context).load(stage)
      .apply(RequestOptions.bitmapTransform(roundedCorners))
      .into(holder.image)
    listOf(holder.w1, holder.w2, holder.w3, holder.w4).forEachIndexed { index, imageView ->
      Glide.with(holder.itemView.context).load(item.setting.weapons[index].image.url)
        .into(imageView)
    }
    if (type == 0) {
      //log("$position $current || $dateStart $dateEnd")
      if (current) {
        holder.diff.visibility = View.VISIBLE
        holder.diff.text = "剩 ${nowTime.until(dateEnd, ChronoUnit.HOURS)} 小时"
      } else {
        holder.diff.visibility = View.GONE
        holder.diff.text = ""
      }
      val bossUrl = bossMap[item.setting.boss.name]
      if (bossUrl != null) {
        holder.boss.visibility = View.VISIBLE
        Glide.with(holder.itemView.context).load(bossUrl).into(holder.boss)
      } else {
        holder.boss.visibility = View.GONE
      }
    } else if (type == 1) { //team logo
      holder.diff.visibility = View.VISIBLE
      holder.diff.setBackgroundResource(R.drawable.rounded_rectangle_team)
      if (current) {
        holder.diff.text = "剩 ${nowTime.until(dateEnd, ChronoUnit.HOURS)} 小时"
      } else {
        holder.diff.text = "距 ${nowTime.until(dateStart, ChronoUnit.HOURS)} 小时"
      }
      Glide.with(holder.itemView.context).load(R.drawable.team).into(holder.boss)
      holder.layout.setOnClickListener {
        context.startActivity(Intent(context, TeamActivity::class.java))
      }
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

class DateAdapter2(
  private val context: Context,
  private val items: List<Stage>,
  private val onItemClick: (Stage) -> Unit
) : RecyclerView.Adapter<DateAdapter2.ViewHolder>() {

  private val imageAssets = "https://splatoon2.ink/assets/splatnet/"

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layout: View = view.findViewById(R.id.layout)
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
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter2.ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date2, parent, false)
    return ViewHolder(view)
  }


  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: DateAdapter2.ViewHolder, position: Int) {
    val nowTime = ZonedDateTime.now(ZoneId.systemDefault())
    val item = items[position]
    val dateStart = localTime2(item.start_time)
    val dateEnd = localTime2(item.end_time)
    val current = dateStart.isBefore(nowTime) && dateEnd.isAfter(nowTime)
    val isAfterNow = dateStart.isAfter(nowTime) && dateEnd.isAfter(nowTime)

    holder.timeStart.text = dateStart.format(formatterTime)
    holder.timeEnd.text = dateEnd.format(formatterTime)
    holder.monthStart.text = queryDate(dateStart)
    holder.monthEnd.text = queryDate(dateEnd)
    holder.itemView.setOnClickListener { onItemClick(item) }

    val stage = imageAssets + item.stage.image
    Glide.with(holder.itemView.context).load(stage)
      .apply(RequestOptions.bitmapTransform(roundedCorners))
      .into(holder.image)
    listOf(holder.w1, holder.w2, holder.w3, holder.w4).forEachIndexed { index, imageView ->
      val url = imageAssets + item.weapons[index].weapon.image
      Glide.with(holder.itemView.context).load(url)
        .into(imageView)
    }
    if (current) {
      holder.diff.setBackgroundResource(R.drawable.rounded_rectangle)
      holder.diff.visibility = View.VISIBLE
      holder.diff.text = "剩 ${nowTime.until(dateEnd, ChronoUnit.HOURS)} 小时"
    } else if (isAfterNow) {
      holder.diff.setBackgroundResource(R.drawable.rounded_rectangle_future)
      holder.diff.visibility = View.VISIBLE
      holder.diff.text = "距 ${nowTime.until(dateStart, ChronoUnit.HOURS)} 小时"
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

  override fun getItemCount() = items.size
}




