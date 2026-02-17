package love.yuzi.avp.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import love.yuzi.avp.app.ApplicationScope

internal val Context.preferenceDataSource by preferencesDataStore(name = "preference_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.preferenceDataSource
    }

    @Provides
    @Singleton
    fun providePreferenceRepository(
        dataStore: DataStore<Preferences>,
        @ApplicationScope scope: CoroutineScope
    ): PreferenceRepository {
        return PreferenceRepository(dataStore, scope)
    }
}