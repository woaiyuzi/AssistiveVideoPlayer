package love.yuzi.avp.longvideo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import love.yuzi.compose.landscape.LandscapeVideoPlayerScreenUi
import love.yuzi.compose.ui.VideoPlayerScreenUi

@Module
@InstallIn(SingletonComponent::class)
object VideoPlayerScreenUiModule {

    @Singleton
    @Provides
    fun provideVideoPlayerScreenUi(): VideoPlayerScreenUi {
        return LandscapeVideoPlayerScreenUi()
    }
}