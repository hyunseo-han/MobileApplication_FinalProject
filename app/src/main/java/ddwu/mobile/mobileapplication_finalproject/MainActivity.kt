package ddwu.mobile.mobileapplication_finalproject

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.mobile.mobileapplication_finalproject.data.EVRoot
import ddwu.mobile.mobileapplication_finalproject.data.EvDetail
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityMainBinding
import ddwu.mobile.mobileapplication_finalproject.network.EVAPIService
import ddwu.mobile.mobileapplication_finalproject.ui.EvAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val adapter = EvAdapter {
            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("addr", it.addr) //주소
            intent.putExtra("cpNm", it.cpNm) //충전기 명칭
            intent.putExtra("cpStat", it.cpStat) //충전기 상태
            intent.putExtra("cpTp", it.cpTp) //충전 방식
            intent.putExtra("csNm", it.csNm) //충전소 명칭
            intent.putExtra("lat", it.lat) //위도
            intent.putExtra("longi", it.longi) //경도
            intent.putExtra("startUpdatetime", it.statUpdatetime) //충전기 상태 갱신 시각

            startActivity(intent)
        }

        mainBinding.rvSearch.adapter = adapter
        mainBinding.rvSearch.layoutManager = LinearLayoutManager(this)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory(GsonConverterFactory.create()) //json이니까 Gson
            .client(client)
            .build()

        val service = retrofit.create(EVAPIService::class.java)

        //검색버튼 클릭하면
        mainBinding.searchBtn.setOnClickListener {
            val searchQuery = mainBinding.etSearch.text.toString() //입력받은 검색어(주소 ex.부산광역시)
            val apiKey = resources.getString(R.string.kobis_key)

            //교수님 코드 가져오기
            val apiCallback = object : Callback<EVRoot> {
                override fun onResponse(
                    call: Call<EVRoot>,
                    response: Response<EVRoot>
                ) {
                    if (response.isSuccessful) {
                        //response.body(): Ev.kt
                        Log.d("API_CALL", "API Call 성공!!!. Number of items received")

                        val evs = response.body()?.data?:listOf()

                        val inputEv = condition(evs, searchQuery)

                        if (inputEv.isNotEmpty()) {
                            Log.d(
                                ContentValues.TAG,
                                "Filtered Hospitals Found: ${inputEv.size}"
                            )
                            adapter.Evs = inputEv
                            adapter.notifyDataSetChanged()
                        } else {
                            Log.d(
                                ContentValues.TAG,
                                "No Hospitals Found Matching Query: $searchQuery"
                            )
                        }
                    } else {
                        Log.d("API_CALL", "API Call 실패!!!!ㅠㅠ!!!")
                        Log.d(ContentValues.TAG, "Unsuccessful Response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EVRoot>, t: Throwable) {
                    Log.d(ContentValues.TAG, "OpenAPI Call Failure ${t.message}")
                }
            }

            val apiCall = service.getEvInfo(1, 100, apiKey)

            apiCall.enqueue(apiCallback)
        }

    }

    //주소랑 충전소 명칭으로 검색 가능
    private fun condition (evs: List<EvDetail>, query: String): List<EvDetail> {
        return evs.filter {
            it.addr.contains(query, ignoreCase = true) || it.csNm.contains(query, ignoreCase = true)
        }
    }
}