package yuzi.avp.shortdrama

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import love.yuzi.compose.portrait.PortraitVideoPlayerScreenUi
import love.yuzi.compose.ui.VideoPlayerScreenUi

@Module
@InstallIn(SingletonComponent::class)
object VideoPlayerScreenUiModule {

    @Singleton
    @Provides
    fun provideVideoPlayerScreenUi(): VideoPlayerScreenUi {
        return PortraitVideoPlayerScreenUi()
    }
}