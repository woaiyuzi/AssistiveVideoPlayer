package love.yuzi.video.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "video",
    indices = [
        Index(
            value = ["title", "programTitle", "uri"],
            unique = true
        )
    ]
)
data class VideoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val uri: String,
    val programTitle: String,
    @ColumnInfo(defaultValue = "0")
    val position: Long = 0L
)

data class Video(
    val id: Long = 0L,
    val title: String,
    val uri: String,
    val programTitle: String,
    val position: Long = 0L
)

fun VideoEntity.toVideo() = Video(
    id = id,
    title = title,
    uri = uri,
    programTitle = programTitle,
    position = position
)

fun Video.toVideoEntity() = VideoEntity(
    title = title,
    uri = uri,
    programTitle = programTitle,
    position = position
)

fun List<VideoEntity>.toModels() = map { it.toVideo() }

fun List<Video>.toEntities() = map { it.toVideoEntity() }