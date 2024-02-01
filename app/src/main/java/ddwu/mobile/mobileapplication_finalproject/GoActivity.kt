package ddwu.mobile.mobileapplication_finalproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityCompleteBinding

class GoActivity : AppCompatActivity(){
    private lateinit var goBinding: ActivityCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        goBinding = ActivityCompleteBinding.inflate(layoutInflater)
        setContentView(goBinding.root)

        goBinding.goBtn.setOnClickListener{
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
        }

    }
}