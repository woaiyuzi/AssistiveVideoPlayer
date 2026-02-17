package love.yuzi.video.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import love.yuzi.video.dao.VideoDao
import love.yuzi.video.database.VideoDatabase
import love.yuzi.video.repo.VideoRepository
import love.yuzi.video.source.LocalVideoDataSource
import love.yuzi.video.source.VideoDataSource

@Module
@InstallIn(SingletonComponent::class)
object VideoModule {

    @Singleton
    @Provides
    fun provideVideoDatabase(
        @ApplicationContext context: Context
    ): VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java, "video"
        ).build()
    }

    @Singleton
    @Provides
    fun provideVideoDao(
        videoDatabase: VideoDatabase
    ): VideoDao {
        return videoDatabase.videoDao()
    }

    @Singleton
    @Provides
    fun providerVideoDataSource(
        videoDao: VideoDao
    ): VideoDataSource {
        return LocalVideoDataSource(videoDao)
    }

    @Singleton
    @Provides
    fun provideVideoRepository(
        dataSource: VideoDataSource
    ): VideoRepository {
        return VideoRepository(dataSource)
    }
}