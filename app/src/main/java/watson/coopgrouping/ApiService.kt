package watson.coopgrouping

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("https://splatoon2.ink/data/coop-schedules.json")
    fun getSplatoon2Schedules(): Call<JSON2>

    @GET("https://splatoon3.ink/data/schedules.json")
    fun getSplatoon3Schedules(): Call<JSON3>
}
