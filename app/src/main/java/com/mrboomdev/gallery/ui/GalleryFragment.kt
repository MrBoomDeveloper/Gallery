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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mrboomdev.gallery.data.Photo
import com.mrboomdev.gallery.databinding.GalleryItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryFragment : Fragment() {
    private val adapter = Adapter()
    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        path = arguments?.getString(ARGUMENT_PATH)

        val maxColumns = inflater.context.resources.configuration.screenWidthDp / 75
        val manager = GridLayoutManager(inflater.context, maxColumns)

        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapter.items?.get(position) ?: return 1
                var ratio = item.width.toFloat() / item.height.toFloat()
                ratio *= 2

                while(ratio > maxColumns) {
                    ratio *= .9f
                }

                while(ratio < 1) {
                    ratio *= 1.1f
                }

                return Math.round(ratio)
            }
        }

        val recycler = RecyclerView(inflater.context)
        recycler.layoutManager = manager
        recycler.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            loadData()
        }

        return recycler
    }

    private suspend fun loadData() {
        val images = mutableListOf<Photo>()

        val collection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )

       requireContext().contentResolver.query(
            collection,
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
               images += Photo(
                   ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn)),
                   cursor.getString(nameColumn),
                   cursor.getInt(widthColumn),
                   cursor.getInt(heightColumn)
               )
           }
       }

        withContext(Dispatchers.Main) {
            adapter.items = images
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
            binding.image.load(item.uri)
        }
    }

    companion object {
        const val ARGUMENT_PATH: String = "PATH"
    }
}