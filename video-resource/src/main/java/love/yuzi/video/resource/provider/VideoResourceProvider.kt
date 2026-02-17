package love.yuzi.video.resource.provider

import love.yuzi.video.resource.model.Episode
import love.yuzi.video.resource.model.Program
import love.yuzi.video.resource.utils.chineseNaturalComparator
import timber.log.Timber
import java.io.File

abstract class VideoResourceProvider {

    abstract val videosDir: File?

    fun getPrograms(): List<Program> {
        val programFiles = videosDir?.listFiles()
        if (programFiles.isNullOrEmpty()) {
            Timber.e("No programs found in $videosDir")
            return emptyList()
        }

        return programFiles.map {
            val name = it.name
            val coverUri = getCoverUri(name)
            Timber.d("Found program: $name, cover: $coverUri")

            Program(
                name = it.name,
                coverUri = getCoverUri(it.name),
            )
        }.sortedWith(chineseNaturalComparator { it.name })
    }

    private fun getCoverUri(title: String) =
        File(videosDir, "$title/cover.jpg").toURI().toString()

    fun getEpisodesByProgramName(name: String): List<Episode> {
        val episodeFiles = File(videosDir, name).listFiles()
        if (episodeFiles.isNullOrEmpty()) {
            Timber.e("No episodes found in $videosDir/$name")
            return emptyList()
        }

        val episodes: MutableList<Episode> = mutableListOf()

        for (file in episodeFiles) {
            if (file.extension == "mp4") {
                episodes.add(
                    Episode(
                        title = file.nameWithoutExtension,
                        uri = file.toURI().toString(),
                    )
                )
                Timber.d("Found episode: ${file.nameWithoutExtension}, uri: ${file.toURI()}")
            }
        }

        return episodes.sortedWith(chineseNaturalComparator { it.title })
    }
}
