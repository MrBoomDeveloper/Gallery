package com.mrboomdev.gallery

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
        BigImageViewer.initialize(FrescoImageLoader.with(this))
    }

    /**
     * Used by Coil
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
                add(SvgDecoder.Factory())

                add(if(Build.VERSION.SDK_INT >= 28) {
                    ImageDecoderDecoder.Factory()
                } else {
                    GifDecoder.Factory()
                })
            }
            .crossfade(100)
            .build()
    }
}