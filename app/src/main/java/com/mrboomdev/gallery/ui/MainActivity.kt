package com.mrboomdev.gallery.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mrboomdev.gallery.R
import com.mrboomdev.gallery.databinding.ActivityMainBinding
import com.mrboomdev.gallery.utils.applyInsets
import com.mrboomdev.gallery.utils.applyTheme
import com.mrboomdev.gallery.utils.dpPx
import com.mrboomdev.gallery.utils.setBottomPadding
import com.mrboomdev.gallery.utils.setLeftPadding
import com.mrboomdev.gallery.utils.setRightPadding
import com.mrboomdev.gallery.utils.setTopPadding

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
            binding.toolbar.setRightPadding(insets.right + dpPx(8f))
            true
        }

        binding.tabs.applyInsets(displayCutout() or statusBars()) { insets ->
            binding.tabs.setLeftPadding(insets.left)
            binding.tabs.setBottomPadding(insets.bottom)
            true
        }

        binding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when(position) {
                    0 -> GalleryFragment()
                    1 -> AlbumsFragment()
                    else -> throw IllegalArgumentException("Unknown page: $position")
                }
            }
        }

        binding.tabs.setOnItemSelectedListener { item: MenuItem ->
            binding.viewpager.setCurrentItem(when(item.itemId) {
                R.id.photos -> 0
                R.id.albums -> 1
                else -> throw IllegalArgumentException("Unknown item: ${item.itemId}")
            }, false)

            true
        }

        binding.search?.setOnClickListener {
            Toast.makeText(this, "Currently unavailable", Toast.LENGTH_SHORT).show()
        }

        binding.searchBar?.setOnClickListener {
            Toast.makeText(this, "Currently unavailable", Toast.LENGTH_SHORT).show()
        }

        binding.account.setOnClickListener {
            Toast.makeText(this, "Currently unavailable", Toast.LENGTH_SHORT).show()
        }

        binding.add.setOnClickListener {
            Toast.makeText(this, "Currently unavailable", Toast.LENGTH_SHORT).show()
        }
    }
}