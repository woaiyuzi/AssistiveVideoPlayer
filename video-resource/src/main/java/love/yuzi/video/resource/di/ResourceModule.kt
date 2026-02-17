package love.yuzi.video.resource.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import love.yuzi.video.resource.provider.LocalVideoResourceProvider
import love.yuzi.video.resource.provider.VideoResourceProvider

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Singleton
    @Provides
    fun provideVideoResourceProvider(
        @ApplicationContext context: Context
    ): VideoResourceProvider {
        return LocalVideoResourceProvider(context)
    }

}