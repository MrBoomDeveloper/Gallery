package com.mrboomdev.gallery.util.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.toast(text: String, duration: Int = 0) {
    Toast.makeText(this, text, duration).show()
}

fun Context.toast(@StringRes text: Int, duration: Int = 0) {
    Toast.makeText(this, text, duration).show()
}

fun Fragment.toast(text: String, duration: Int = 0) {
    requireContext().toast(text, duration)
}

fun Fragment.toast(@StringRes text: Int, duration: Int = 0) {
    requireContext().toast(text, duration)
}