package watson.coopgrouping

import androidx.annotation.Keep
import retrofit2.http.GET

interface ScheduleService {
  @GET("schedules.json")
  suspend fun getSchedules(): JSON
}

interface ScheduleService2 {
  @GET("coop-schedules.json")
  suspend fun getSchedules(): JSON2
}

//二代
@Keep
data class JSON2(
  val details: List<Stage>,
)

@Keep
data class Stage(
  val start_time: String,
  val end_time: String,
  val weapons: List<Weapon2Wrap>,
  val stage: StageMap
)

@Keep
data class StageMap(
  val name: String,
  val image: String,
)

@Keep
data class Weapon2Wrap(
  val id: Long,
  val weapon: Weapon2,
)

@Keep
data class Weapon2(
  val name: String,
  val image: String,
)


//三代
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
  val teamContestSchedules: RegularSchedules,
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
