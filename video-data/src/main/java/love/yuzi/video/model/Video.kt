package love.yuzi.video.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "video",
    indices = [
        Index(
            value = ["title", "programTitle"],
            unique = true
        )
    ]
)
data class VideoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val uri: String,
    val programTitle: String
)

data class Video(
    val id: Long = 0L,
    val title: String,
    val uri: String,
    val programTitle: String
)

fun VideoEntity.toVideo() = Video(
    id = id,
    title = title,
    uri = uri,
    programTitle = programTitle
)

fun Video.toVideoEntity() = VideoEntity(
    title = title,
    uri = uri,
    programTitle = programTitle
)

fun List<VideoEntity>.toModels() = map { it.toVideo() }

fun List<Video>.toEntities() = map { it.toVideoEntity() }