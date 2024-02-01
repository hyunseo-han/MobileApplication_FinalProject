package ddwu.mobile.mobileapplication_finalproject.network

import ddwu.mobile.mobileapplication_finalproject.data.EVRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//좋았ㅅ숴 이건 잘한듯ㅎ
interface EVAPIService {
    @GET("EvInfoServiceV2/v1/getEvSearchList")
    fun getEvInfo(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("serviceKey") serviceKey: String,
    ): Call<EVRoot>
}