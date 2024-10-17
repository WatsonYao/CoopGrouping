package watson.coopgrouping

import androidx.annotation.Keep
import retrofit2.http.GET

interface ScheduleService {
  @GET("schedules.json")
  suspend fun getSchedules(): JSON
}

@Keep
data class JSON(
  val data: Data,
)

@Keep
data class Data(
  val coopGroupingSchedule: CoopGroupingSchedule,
)

@Keep
data class CoopGroupingSchedule(
  val regularSchedules: RegularSchedules,
)

@Keep
data class RegularSchedules(
  val nodes: List<Node>
)

@Keep
data class Node(
  val startTime: String,
  val endTime: String,
  val setting: Setting,
)

@Keep
data class Setting(
  val boss: Boss,
  val coopStage: CoopStage,
  val weapons: List<Weapon>
)

@Keep
data class Boss(
  val name: String,
)

@Keep
data class CoopStage(
  val name: String,
  val thumbnailImage: ImageUrl,
)

@Keep
data class Weapon(
  val name: String,
  val image: ImageUrl,
)

@Keep
data class ImageUrl(
  val url: String,
)
