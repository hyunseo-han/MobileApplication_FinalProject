package ddwu.mobile.mobileapplication_finalproject.ui

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ddwu.mobile.mobileapplication_finalproject.data.EvDetail
import ddwu.mobile.mobileapplication_finalproject.databinding.ListEvBinding


class EvAdapter(private val onClick: (EvDetail) -> Unit) : RecyclerView.Adapter<EvAdapter.EvHolder>(){
    var Evs: List<EvDetail>? = null //Ev가 여러개라 Evs...

    override fun getItemCount(): Int {
        return Evs?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvHolder {
        val itemBinding = ListEvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EvHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: EvHolder, position: Int) {
        val evCar = Evs?.get(position)
        evCar?.let {
            holder.itemBinding.tvBold.text = it.csNm
            holder.itemBinding.tvAddr2.text = it.addr


            // 뷰 높이 명시적 설정
            holder.itemBinding.root.layoutParams = holder.itemBinding.root.layoutParams.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            // 아이템 클릭 시 DetailActivity로 이동
            holder.itemView.setOnClickListener {
                onClick(evCar)
            }

            // 기타 로그 출력
        }
    }

    class EvHolder(val itemBinding: ListEvBinding) : RecyclerView.ViewHolder(itemBinding.root)
}