package com.mrboomdev.gallery.ui

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mrboomdev.gallery.data.Photo
import com.mrboomdev.gallery.databinding.GalleryItemBinding
import com.mrboomdev.gallery.util.GalleryLayoutManager
import com.mrboomdev.gallery.util.extensions.dpPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class GalleryFragment : Fragment() {
    lateinit var parentView: WeakReference<View>
    private val mAdapter = Adapter()
    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        path = arguments?.getString(ARGUMENT_PATH)

        lifecycleScope.launch {
            loadData()
        }

        return RecyclerView(inflater.context).apply {
            adapter = mAdapter

            layoutManager = GalleryLayoutManager(
                maxRowHeight = context.dpPx(300f),
                aspectRatioExtractor = { position ->
                    mAdapter.items?.get(position).let {
                        if(it == null) 0f else width.toFloat() / height.toFloat()
                    }
                }
            )
        }
    }

    private fun loadFromCollectionUri(uri: Uri, into: MutableList<Photo>) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )

        requireContext().contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)

            while(cursor.moveToNext()) {
                into += Photo(
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn)),
                    cursor.getString(nameColumn),
                    cursor.getInt(widthColumn),
                    cursor.getInt(heightColumn)
                )

                // TODO: Remove it after all optimizations
                if(into.size >= 10) break
            }
        }
    }

    private suspend fun loadData() {
        withContext(Dispatchers.IO) {
            val images = ArrayList<Photo>()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                loadFromCollectionUri(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), images)
                loadFromCollectionUri(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_INTERNAL), images)
            } else {
                loadFromCollectionUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, images)
                loadFromCollectionUri(MediaStore.Images.Media.INTERNAL_CONTENT_URI, images)
            }

            withContext(Dispatchers.Main) {
                mAdapter.items = images
            }
        }
    }

    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        init {
            setHasStableIds(true)
        }

        var items: List<Photo>? = null
            @SuppressLint("NotifyDataSetChanged")
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        }

        override fun getItemCount(): Int {
            return items?.size ?: 0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items!![position])
        }
    }

    class ViewHolder(private val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var uri: Uri? = null

        init {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, SlideshowActivity::class.java)
                intent.putExtra(SlideshowActivity.EXTRA_URI, uri)
                binding.root.context.startActivity(intent)
            }
        }

        fun bind(item: Photo) {
            this.uri = item.uri

            binding.image.load(item.uri) {
                crossfade(100)
                size(item.width, item.height)
            }
        }
    }

    companion object {
        const val ARGUMENT_PATH: String = "PATH"
    }
}