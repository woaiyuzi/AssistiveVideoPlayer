package love.yuzi.avp.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) {
    companion object {
        private val lastPlayedVideoIdKey = longPreferencesKey("last_played_video_id")
        private val lastPlayedVideoPositionKey = longPreferencesKey("last_played_video_position")
    }

    var lastPlayedVideoId: Long? = null
        private set

    var lastPlayedVideoPosition: Long = 0L
        private set

    init {
        scope.launch {
            val prefs = dataStore.data.first()
            lastPlayedVideoId = prefs[lastPlayedVideoIdKey]
            lastPlayedVideoPosition = prefs[lastPlayedVideoPositionKey] ?: 0L
        }
    }

    fun setPlayback(videoId: Long?, position: Long) = scope.launch {
        lastPlayedVideoId = videoId
        lastPlayedVideoPosition = position
        dataStore.edit {
            videoId?.let { id -> it[lastPlayedVideoIdKey] = id }
            it[lastPlayedVideoPositionKey] = position
        }
    }
}