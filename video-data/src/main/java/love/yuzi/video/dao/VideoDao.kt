package love.yuzi.video.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import love.yuzi.video.model.VideoEntity

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(videos: List<VideoEntity>)

    @Query("SELECT * FROM video ORDER BY id ASC")
    fun observeFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM video WHERE programTitle = :programTitle ORDER BY id ASC")
    suspend fun getByProgramTitle(programTitle: String): List<VideoEntity>
}