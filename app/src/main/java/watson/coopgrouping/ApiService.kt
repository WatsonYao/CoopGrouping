package watson.coopgrouping

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("sample")
    fun getSampleData(): Call<ResponseBody>
}
