package ddwu.mobile.mobileapplication_finalproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//??
@Database(entities = [WriteDTO::class], version = 2, exportSchema = false)
abstract class WriteDB : RoomDatabase() {
    abstract fun writeDAO() : WriteDAO

    companion object {
        @Volatile
        private var INSTANCE : WriteDB? = null

        fun getDatabase(context: Context) : WriteDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    WriteDB::class.java, "ev_db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}