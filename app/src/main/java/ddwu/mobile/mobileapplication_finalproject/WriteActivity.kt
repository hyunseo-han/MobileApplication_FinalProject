package ddwu.mobile.mobileapplication_finalproject

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ddwu.mobile.mobileapplication_finalproject.data.WriteDAO
import ddwu.mobile.mobileapplication_finalproject.data.WriteDB
import ddwu.mobile.mobileapplication_finalproject.data.WriteDTO
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityWriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteActivity : AppCompatActivity() {

    val writeDB : WriteDB by lazy {
        WriteDB.getDatabase(this)
    }

    val writeDAO : WriteDAO by lazy {
        writeDB.writeDAO()
    }

    private lateinit var writeBinding: ActivityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writeBinding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(writeBinding.root)

        val csNm = intent.getStringExtra("csNm") ?: "이름 없음"
        val addr = intent.getStringExtra("addr") ?: "주소 없음"

        // 받아온 데이터를 뷰에 설정
        writeBinding.tvCName.text = csNm
        writeBinding.tvAddr.text = addr

        writeBinding.submitbtn.setOnClickListener {
            val review = writeBinding.tvWrite.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    writeDAO.insertReview(WriteDTO(0, csNm, addr, review)) //충전소 이름, 주소, 리뷰
                    Log.d("WriteActivity", "여기!! Review inserted: $review")

                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@WriteActivity, GoActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "에러에러에러 Error inserting memo")
                }
            }

        }
    }
}