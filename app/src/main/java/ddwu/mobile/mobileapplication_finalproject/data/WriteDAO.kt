package ddwu.mobile.mobileapplication_finalproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WriteDAO {

    @Query("SELECT * FROM ev_table")
    fun getAll() : Flow<List<WriteDTO>>

    @Query("SELECT * FROM ev_table WHERE id = :id")
    suspend fun getWriteById(id: Long) : List<WriteDTO>

    @Insert
    suspend fun insertReview(review: WriteDTO)

    @Update
    suspend fun updateReview(review: WriteDTO)

    @Delete
    suspend fun deleteReview(review: WriteDTO)
}