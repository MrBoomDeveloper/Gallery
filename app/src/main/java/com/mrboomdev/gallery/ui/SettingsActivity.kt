package com.mrboomdev.gallery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var linear = LinearLayoutCompat(this)
        linear.orientation = LinearLayoutCompat.HORIZONTAL
        setContentView(linear)
    }
}