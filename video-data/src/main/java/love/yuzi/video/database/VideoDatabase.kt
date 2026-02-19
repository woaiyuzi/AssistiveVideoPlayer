package love.yuzi.video.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import love.yuzi.video.dao.VideoDao
import love.yuzi.video.model.VideoEntity

@Database(
    entities = [
        VideoEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}