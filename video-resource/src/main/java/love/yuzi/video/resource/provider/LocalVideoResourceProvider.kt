package love.yuzi.video.resource.provider

import android.content.Context

class LocalVideoResourceProvider(
    context: Context,
) : VideoResourceProvider() {
    override val videosDir =
        context.getExternalFilesDir("videos")
}