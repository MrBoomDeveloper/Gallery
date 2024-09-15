package com.mrboomdev.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.mrboomdev.gallery.R

class AlbumsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val scroll = ScrollView(inflater.context)
        val view = LinearLayout(inflater.context)

        val a = View(inflater.context)
        a.setBackgroundResource(R.mipmap.ic_launcher)
        a.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 9999)
        view.addView(a)

        scroll.addView(view)
        return scroll
    }
}