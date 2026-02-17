package love.yuzi.video.database

import androidx.room.Database
import androidx.room.RoomDatabase
import love.yuzi.video.dao.VideoDao
import love.yuzi.video.model.VideoEntity

@Database(
    entities = [
        VideoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}