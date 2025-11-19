package watson.coopgrouping

import androidx.annotation.Keep

//二代
@Keep
data class APPJSON2(
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
  val weapon: Weapon2?,
  val coop_special_weapon: Weapon2?
)

@Keep
data class Weapon2(
  val name: String,
  val image: String,
)


//三代
@Keep
data class APPJSON3(
  val data: Data,
)

@Keep
data class Data(
  val coopGroupingSchedule: CoopGroupingSchedule,
  val bankaraSchedules: BankaraSchedules,
)

@Keep
data class CoopGroupingSchedule(
  val regularSchedules: RegularSchedules,
  val teamContestSchedules: RegularSchedules,
  val bigRunSchedules: RegularSchedules,
)

@Keep
data class BankaraSchedules(
  val nodes: List<Bankara>
)

@Keep
data class Bankara(
  val startTime: String,
  val endTime: String,
  val bankaraMatchSettings: List<BankaraMatchSetting>,
)

@Keep
data class BankaraMatchSetting(
  val vsStages: List<VsStage>,
  val vsRule: VsRule,
  val bankaraMode: String
)

@Keep
data class VsStage(
  val vsStageId: String,
  val id: String,
  val image: ImageUrl
)


@Keep
data class VsRule(
  val name: String,
  val rule: String,
  val id: String,
)

@Keep
data class RegularSchedules(
  val nodes: List<RegularNode>
)

@Keep
data class RegularNode(
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
