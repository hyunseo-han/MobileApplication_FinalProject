package ddwu.mobile.mobileapplication_finalproject.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.mobile.mobileapplication_finalproject.data.WriteDTO
import ddwu.mobile.mobileapplication_finalproject.databinding.ListReviewBinding

class WriteAdapter : RecyclerView.Adapter<WriteAdapter.WriteHolder>(){
    var writeList: List<WriteDTO>? = null
    var itemClickListener: OnWriteItemClickListener? = null

    override fun getItemCount(): Int {
        return writeList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteHolder {
        val itemBinding = ListReviewBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return WriteHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: WriteHolder, position: Int) {
        val dto = writeList?.get(position)
        Log.d("WriteAdapter", "여길봐용 Binding view for position: $position with data: $dto")

        holder.itemBinding.tvBold2.text = dto?.csNm //충전소 이름
        holder.itemBinding.textView5.text = dto?.addr //주소
        holder.itemBinding.cl.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    class WriteHolder(val itemBinding: ListReviewBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnWriteItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnWriteItemClickListener) {
        itemClickListener = listener
    }
}