package watson.coopgrouping

import android.util.Log
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun localTime(isoString: String): ZonedDateTime {
  val zonedDateTime = ZonedDateTime.parse(isoString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  val localZoneId = ZoneId.systemDefault()
  val localDateTime = zonedDateTime.withZoneSameInstant(localZoneId)
  return localDateTime
}


val weekNames = arrayOf("一", "二", "三", "四", "五", "六", "日")
fun getDayOfWeek(date: ZonedDateTime): String {
  val dayOfWeek = date.dayOfWeek
  return weekNames[dayOfWeek.value - 1]
}

fun log(msg: String) {
  Log.i("temp", msg)
}
