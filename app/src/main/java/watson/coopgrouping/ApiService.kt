package watson.coopgrouping

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import watson.coopgrouping.JSON2
import watson.coopgrouping.JSON3

interface ApiService {

    @GET("sample")
    fun getSampleData(): Call<ResponseBody>

    @GET("https://splatoon2.ink/data/coop-schedules.json")
    fun getSplatoon2Schedules(): Call<JSON2>

    @GET("https://splatoon3.ink/data/schedules.json")
    fun getSplatoon3Schedules(): Call<JSON3>
}
