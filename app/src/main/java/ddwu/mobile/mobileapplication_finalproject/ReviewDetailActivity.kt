package ddwu.mobile.mobileapplication_finalproject

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.mobile.mobileapplication_finalproject.data.WriteDTO
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityWriteDetailBinding

class ReviewDetailActivity : AppCompatActivity() {
    private lateinit var reviewDetailBinding: ActivityWriteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?, ) {
        super.onCreate(savedInstanceState)
        reviewDetailBinding = ActivityWriteDetailBinding.inflate(layoutInflater)
        setContentView(reviewDetailBinding.root)

        val review = intent.getSerializableExtra("writeDTO") as WriteDTO?
        reviewDetailBinding.tvName.text = review?.csNm
        reviewDetailBinding.tvAddress.text = review?.addr
        reviewDetailBinding.tvMyReview.text = review?.review

        reviewDetailBinding.shareBtn.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("")) // 수신자 이메일 주소
                putExtra(Intent.EXTRA_SUBJECT, "리뷰 공유: ${review?.csNm} 충전소") // 이메일 제목
                putExtra(Intent.EXTRA_TEXT, "충전소 이름: ${review?.csNm}\n주소: ${review?.addr}\n내 리뷰: ${review?.review}") // 이메일 내용
            }

            // 이메일 전송 인텐트 실행
            startActivity(Intent.createChooser(emailIntent, "리뷰 공유"))
        }

        reviewDetailBinding.backBtn.setOnClickListener{
            val intent = Intent(this@ReviewDetailActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}