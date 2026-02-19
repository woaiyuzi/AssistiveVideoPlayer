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
    }

    var lastPlayedVideoId: Long? = null
        private set

    init {
        scope.launch {
            val prefs = dataStore.data.first()
            lastPlayedVideoId = prefs[lastPlayedVideoIdKey]
        }
    }

    fun setLastPlayedVideoId(videoId: Long) = scope.launch {
        lastPlayedVideoId = videoId
        dataStore.edit {
            it[lastPlayedVideoIdKey] = videoId
        }
    }
}