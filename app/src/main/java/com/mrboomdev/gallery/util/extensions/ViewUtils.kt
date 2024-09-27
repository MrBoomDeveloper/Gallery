package com.mrboomdev.gallery.util.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.DynamicColors

fun Activity.applyTheme() {
    DynamicColors.applyToActivityIfAvailable(this)
}

fun View.setLeftPadding(padding: Int) {
    setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setRightPadding(padding: Int) {
    setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setBottomPadding(padding: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun View.setTopPadding(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.setLeftMargin(margin: Int) {
    useLayoutParams<MarginLayoutParams> {
        params -> params.leftMargin = margin
    }
}

fun View.setTopMargin(margin: Int) {
    useLayoutParams<MarginLayoutParams> {
            params -> params.topMargin = margin
    }
}

fun View.setBottomMargin(margin: Int) {
    useLayoutParams<MarginLayoutParams> {
            params -> params.bottomMargin = margin
    }
}

fun View.setRightMargin(margin: Int) {
    useLayoutParams<MarginLayoutParams> {
            params -> params.rightMargin = margin
    }
}

inline fun <reified T : LayoutParams> View.useLayoutParams(callback: (params: T) -> Unit) {
    val params = layoutParams
    callback.invoke(params as T)
    layoutParams = params
}

/**
 * @param callback Return true to consume insets
 */
fun View.applyInsets(insets: Int, callback: (insets: Insets) -> Boolean) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insetsCompat ->
        if(callback.invoke(insetsCompat.getInsets(insets))) {
            WindowInsetsCompat.CONSUMED
        } else {
            insetsCompat
        }
    }
}

fun Drawable.transitionFrom(oldDrawable: Drawable?): TransitionDrawable {
    val from = oldDrawable ?: ColorDrawable(Color.TRANSPARENT)
    val transitionDrawable = TransitionDrawable(arrayOf(from, this))
    transitionDrawable.isCrossFadeEnabled = true
    return transitionDrawable
}

fun Context.dpPx(dp: Float): Int {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics))
}