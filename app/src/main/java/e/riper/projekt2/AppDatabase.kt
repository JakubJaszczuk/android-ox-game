package e.riper.projekt2

import android.content.Context
import androidx.room.*

@Entity(tableName = "HighScores")
data class HighScore(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playerName: String,
    val score: Int,
    val unixTime: Long
)

@Dao
interface HighScoresDao {
    @Insert
    suspend fun add(score: HighScore): Long

    @Update
    suspend fun update(score: HighScore): Int

    @Delete
    suspend fun delete(score: HighScore): Int

    @Query("SELECT * FROM HighScores;")
    suspend fun getAll(): Array<HighScore>

    @Query("SELECT * FROM HighScores ORDER BY score DESC;")
    suspend fun getAllDescending(): Array<HighScore>

    @Query("SELECT * FROM HighScores WHERE id = :id;")
    suspend fun getById(id: Int): HighScore?
}

@Database(entities = [HighScore::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun highScoresDao(): HighScoresDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_data.db").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
