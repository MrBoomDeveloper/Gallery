package com.mrboomdev.gallery.ui

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.mrboomdev.gallery.utils.applyTheme

class SlideshowActivity : AppCompatActivity() {

    @Suppress("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        applyTheme()
        super.onCreate(savedInstanceState)

        val uri = intent.getParcelableExtra<Uri>(EXTRA_URI)
            ?: throw NullPointerException("Uri cannot be null!")

        val imageview = ImageView(this)
        imageview.scaleType = ImageView.ScaleType.FIT_CENTER
        setContentView(imageview)
        imageview.load(uri)
    }

    companion object {
        const val EXTRA_URI = "uri"
    }
}