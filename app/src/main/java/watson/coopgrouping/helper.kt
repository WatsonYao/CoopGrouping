package watson.coopgrouping

import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun localTime(isoString: String): ZonedDateTime {
  val zonedDateTime = ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  val localZoneId = ZoneId.systemDefault()
  val localDateTime = zonedDateTime.withZoneSameInstant(localZoneId)
  return localDateTime
}

fun localTime2(timestamp: String): ZonedDateTime {
  // 将时间戳转换为 Instant
  val instant = Instant.ofEpochSecond(timestamp.toLong())
  // 将 Instant 转换为 ZonedDateTime，指定时区
  return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
}


val weekNames = arrayOf("一", "二", "三", "四", "五", "六", "日")
fun getDayOfWeek(date: ZonedDateTime): String {
  val dayOfWeek = date.dayOfWeek
  return weekNames[dayOfWeek.value - 1]
}

fun log(msg: String) {
  Log.i("temp", msg)
}
