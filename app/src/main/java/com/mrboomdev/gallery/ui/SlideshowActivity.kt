package com.mrboomdev.gallery.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.piasy.biv.loader.ImageLoader
import com.github.piasy.biv.view.BigImageView
import com.github.piasy.biv.view.FrescoImageViewFactory
import com.mrboomdev.gallery.util.extensions.applyTheme
import com.mrboomdev.gallery.util.extensions.toast
import java.io.File

class SlideshowActivity : AppCompatActivity() {

    @Suppress("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        applyTheme()
        super.onCreate(savedInstanceState)

        val uri = intent.getParcelableExtra<Uri>(EXTRA_URI)
            ?: throw NullPointerException("Uri cannot be null!")

        BigImageView(this).apply {
            setImageViewFactory(FrescoImageViewFactory())
            setInitScaleType(BigImageView.INIT_SCALE_TYPE_FIT_CENTER)

            setImageLoaderCallback(object : ImageLoader.Callback {
                override fun onCacheHit(imageType: Int, image: File?) {}
                override fun onCacheMiss(imageType: Int, image: File?) {}
                override fun onStart() {}
                override fun onProgress(progress: Int) {}
                override fun onFinish() {}

                override fun onSuccess(image: File?) {
                    ssiv.isQuickScaleEnabled = true
                    ssiv.maxScale = 100f
                }

                override fun onFail(e: Exception?) {
                    toast("Failed to load an image!")
                    Log.e(TAG, "Failed to load an image!", e)
                }
            })

            showImage(uri)
            setContentView(this)
        }
    }

    companion object {
        const val EXTRA_URI = "uri"
        private const val TAG = "SlideshowActivity"
    }
}