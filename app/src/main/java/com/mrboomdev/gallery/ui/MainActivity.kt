package com.mrboomdev.gallery.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mrboomdev.gallery.R
import com.mrboomdev.gallery.databinding.ActivityMainBinding
import com.mrboomdev.gallery.util.extensions.applyInsets
import com.mrboomdev.gallery.util.extensions.applyTheme
import com.mrboomdev.gallery.util.extensions.dpPx
import com.mrboomdev.gallery.util.extensions.fixAndShow
import com.mrboomdev.gallery.util.extensions.setBottomPadding
import com.mrboomdev.gallery.util.extensions.setLeftPadding
import com.mrboomdev.gallery.util.extensions.setRightPadding
import com.mrboomdev.gallery.util.extensions.setTopPadding
import com.mrboomdev.gallery.util.extensions.toast
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        applyTheme()

        super.onCreate(savedInstanceState)
        createLayout()
    }

    private fun createLayout() {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewpager.isUserInputEnabled = false
        setContentView(binding.root)

        binding.toolbar.applyInsets(displayCutout() or statusBars()) { insets ->
            binding.toolbar.setTopPadding(insets.top + dpPx(8f))
            binding.toolbar.setLeftPadding(insets.left + dpPx(8f))
            binding.toolbar.setRightPadding(insets.right + dpPx(16f))
            true
        }

        for(view in arrayOf(binding.navigation, binding.tabs)) {
            view?.applyInsets(displayCutout() or statusBars()) { insets ->
                view.setLeftPadding(insets.left)
                view.setBottomPadding(insets.bottom)
                true
            }
        }

        binding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when(position) {
                    0 -> GalleryFragment().apply { parentView = WeakReference(binding.viewpager) }
                    1 -> AlbumsFragment()
                    else -> throw IllegalArgumentException("Unknown page: $position")
                }
            }
        }

        (fun(item: MenuItem): Boolean {
            binding.viewpager.setCurrentItem(when(item.itemId) {
                R.id.photos -> {
                    binding.searchBar?.setText(R.string.search_photos)
                    0
                }

                R.id.albums -> {
                    binding.searchBar?.setText(R.string.search_albums)
                    1
                }

                else -> throw IllegalArgumentException("Unknown item: ${item.itemId}")
            }, false)

            return true
        }).let {
            binding.tabs?.setOnItemSelectedListener { item -> it(item) }
            binding.navigation?.setNavigationItemSelectedListener { item -> it(item) }
        }

        View.OnClickListener {
            toast("Currently unavailable")
        }.let {
            binding.searchButton?.setOnClickListener(it)
            binding.searchBar?.setOnClickListener(it)
        }

        binding.account.setOnClickListener {
            QuickSettingsDialog().fixAndShow(supportFragmentManager, "QuickSettings")
        }

        binding.add.setOnClickListener {
            toast("Currently unavailable")
        }
    }
}