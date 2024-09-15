package com.mrboomdev.gallery

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder

class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
    }

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