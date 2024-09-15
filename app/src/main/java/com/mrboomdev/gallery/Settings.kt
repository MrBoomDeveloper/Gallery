package com.mrboomdev.gallery

import android.content.Context
import android.content.SharedPreferences
import java.lang.ref.WeakReference

object Settings {
    private lateinit var prefs: WeakReference<SharedPreferences>

    var PROFILES: String
        get() = prefs.get()!!.getString("PROFILES", "[]").toString()
        set(value) = prefs.get()!!.edit().putString("PROFILES", value).apply()

    var THUMBNAIL_AUTOPLAY: Boolean
        get() = prefs.get()!!.getBoolean("THUMBNAIL_AUTOPLAY", true)
        set(value) = prefs.get()!!.edit().putBoolean("THUMBNAIL_AUTOPLAY", value).apply()

    var LOOP_VIDEOS: Boolean
        get() = prefs.get()!!.getBoolean("LOOP_VIDEOS", true)
        set(value) = prefs.get()!!.edit().putBoolean("LOOP_VIDEOS", value).apply()

    var CROP_THUMBNAILS: Boolean
        get() = prefs.get()!!.getBoolean("CROP_THUMBNAILS", true)
        set(value) = prefs.get()!!.edit().putBoolean("CROP_THUMBNAILS", value).apply()

    fun init(context: Context) {
        this.prefs = WeakReference(context.getSharedPreferences("Gallery", 0))
    }
}