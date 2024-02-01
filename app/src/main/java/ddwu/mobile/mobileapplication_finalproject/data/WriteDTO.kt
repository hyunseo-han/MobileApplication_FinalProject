package ddwu.mobile.mobileapplication_finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "ev_table")
data class WriteDTO(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var csNm: String,
    var addr: String,
    var review: String)
    : Serializable{
    override fun toString(): String {
        return "${id} : (${csNm})\n${addr}\n $review"
    }
    }
