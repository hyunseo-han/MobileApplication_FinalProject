package ddwu.mobile.mobileapplication_finalproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.mobile.mobileapplication_finalproject.data.EVRoot
import ddwu.mobile.mobileapplication_finalproject.data.EvDetail
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityDetailBinding
import ddwu.mobile.mobileapplication_finalproject.network.EVAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var currentLoc : Location
    private var marker: Marker? = null
    private var isFirst = true

    private var receivedLat: Double? = null
    private var receivedLng: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        // 충전기의 상태를 문자열로 변환하는 함수
        fun getStatusString(statusCode: String?): String {
            return when (statusCode) {
                "0" -> "상태확인불가"
                "1" -> "충전가능"
                "2" -> "충전중"
                "3" -> "고장/점검"
                "4" -> "통신장애"
                "9" -> "충전예약"
                else -> "알 수 없는 상태"
            }
        }

        // 충전 방식 코드를 문자열로 변환하는 함수
        fun getChargingTypeString(typeCode: String?): String {
            return when (typeCode) {
                "1" -> "B타입(5핀)"
                "2" -> "C타입(5핀)"
                "3" -> "BC타입(5핀)"
                "4" -> "BC타입(7핀)"
                "5" -> "DC차데모"
                "6" -> "AC3상"
                "7" -> "DC콤보"
                "8" -> "DC차데모 + DC콤보"
                "9" -> "DC차데모 + AC3상"
                "10" -> "DC차데모 + DC콤보 + AC3상"
                else -> "알 수 없는 충전 방식"
            }
        }

        // 인텐트에서 데이터 추출
        val addr = intent.getStringExtra("addr") ?: "주소 정보 없음"
        val cpNm = intent.getStringExtra("cpNm") ?: "충전기 명칭 정보 없음"
        val cpStat = intent.getStringExtra("cpStat") ?: "충전기 상태 정보 없음"
        val cpStatString = getStatusString(cpStat)
        val cpTp = intent.getStringExtra("cpTp") ?: "충전 방식 상태 정보 없음"
        val cpTpString = getChargingTypeString(cpTp)
        val csNm = intent.getStringExtra("csNm") ?: "충전소 명칭 정보 없음"
        val lat = intent.getStringExtra("lat")
        val longi = intent.getStringExtra("longi")
        val startUpdatetime = intent.getStringExtra("startUpdatetime")

        // 텍스트뷰에 보이게 함
        val detailsText = "충전소 명칭: $csNm\n주소: $addr\n충전기 명칭: $cpNm\n충전기 상태: $cpStatString\n" +
                "충전 방식: $cpTpString\n충전기 상태 갱신 시각: $startUpdatetime\n위도: $lat\n경도: $longi\n"
        detailBinding.tvDetail.text = detailsText


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermissions()

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EVAPIService::class.java)

        val apiKey = resources.getString(R.string.kobis_key)
        Log.d(ContentValues.TAG, "API키 까지 오케")


        val apiCallback = object : Callback<EVRoot> {

            override fun onResponse(call: Call<EVRoot>, response: Response<EVRoot>) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "successful Response!!")
                } else {
                    Log.d(ContentValues.TAG, "Unsuccessful Response: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<EVRoot>, t: Throwable) {
                Log.d(ContentValues.TAG, "OpenAPI Call Failure ${t.message}")
            }
        }

        val apiCall = service.getEvInfo(1, 10, apiKey)
        apiCall.enqueue(apiCallback)

        receivedLat = intent.getStringExtra("lat")?.toDoubleOrNull()
        receivedLng = intent.getStringExtra("longi")?.toDoubleOrNull()


        detailBinding.goReviewBtn.setOnClickListener{
            val intent = Intent(this@DetailActivity, WriteActivity::class.java)

            // 인텐트에 데이터 추가
            intent.putExtra("addr", addr)
            intent.putExtra("cpNm", cpNm)
            intent.putExtra("cpStat", cpStat)
            intent.putExtra("cpTp", cpTp)
            intent.putExtra("csNm", csNm)
            intent.putExtra("lat", lat)
            intent.putExtra("longi", longi)
            intent.putExtra("startUpdatetime", startUpdatetime)

            // WriteActivity 시작
            startActivity(intent)
        }
    }

    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map

            if (receivedLat != null && receivedLng != null) {
                val location = LatLng(receivedLat!!, receivedLng!!)
                addMarkerAtLocation(location)
            }

            googleMap.setOnInfoWindowClickListener { marker ->
                val ev = marker.tag as? EvDetail
                ev?.let {
                    val intent = Intent(this@DetailActivity, WriteActivity::class.java)
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

            }

            Log.d(ContentValues.TAG, "구글맵 준비 완")
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)

    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("onResume", "onResume")
        }
    }

    fun checkPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("checkPermission", "권한 성공")
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Toast.makeText(this, "FINE_LOCATION is granted", Toast.LENGTH_SHORT).show()
                startLocUpdates()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Toast.makeText(this, "COARSE_LOCATION is granted", Toast.LENGTH_SHORT).show()
                startLocUpdates()
            }
            else -> {
                Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val locCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations[0]
            val newMarkerLoc: LatLng = LatLng(currentLoc.latitude, currentLoc.longitude)
            marker?.remove()
        }
    }


    val locRequest : LocationRequest = LocationRequest.Builder(10000)
        .setMinUpdateIntervalMillis(15000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    @SuppressLint("MissingPermission")
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,
            locCallback,
            Looper.getMainLooper()
        )
    }

    private fun addMarkerAtLocation(location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title("선택된 위치")
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
    }
}