package ddwu.mobile.mobileapplication_finalproject

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.mobile.mobileapplication_finalproject.data.WriteDAO
import ddwu.mobile.mobileapplication_finalproject.data.WriteDB
import ddwu.mobile.mobileapplication_finalproject.databinding.ActivityWriteResultBinding
import ddwu.mobile.mobileapplication_finalproject.ui.WriteAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewActivity : AppCompatActivity() {
    private lateinit var reviewBinding: ActivityWriteResultBinding

    val writeDB : WriteDB by lazy {
        WriteDB.getDatabase(this)
    }

    val writeDAO : WriteDAO by lazy {
        writeDB.writeDAO()
    }

    val adapter : WriteAdapter by lazy {
        WriteAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewBinding = ActivityWriteResultBinding.inflate(layoutInflater)
        setContentView(reviewBinding.root)

        reviewBinding.rvReview.adapter = adapter
        reviewBinding.rvReview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: WriteAdapter.OnWriteItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@ReviewActivity, ReviewDetailActivity::class.java)
                intent.putExtra("writeDTO", adapter.writeList?.get(position))
                startActivity(intent)
            }
        })

        show()
    }

    private fun show() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                writeDAO.getAll().collect {  reviews ->
                    adapter.writeList = reviews
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}