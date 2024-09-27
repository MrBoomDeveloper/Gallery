package com.mrboomdev.gallery.util

import androidx.recyclerview.widget.RecyclerView

class GalleryLayoutManager(
    val aspectRatioExtractor: (position: Int) -> Float,
    maxRowHeight: Int = -1
) : RecyclerView.LayoutManager() {

    init {
        initValues(maxRowHeight)
    }

    private fun initValues(maxRowHeight: Int) {
        this.maxRowHeight = maxRowHeight
    }

    var maxRowHeight: Int = -1
        set(value) {
            field = value
            requestLayout()
        }

    private var recyclerView: RecyclerView? = null
    private var offset = 0

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        recyclerView = null
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        var lastY = 0

        for(position in 0 until itemCount) {
            val ratio = aspectRatioExtractor(position)

            if(ratio < 0) {
                throw IllegalArgumentException("Aspect ratio cannot be less than 0!")
            }

            val view = recycler.getViewForPosition(position)
            addView(view)

            val params = view.layoutParams as RecyclerView.LayoutParams
            params.width = width
            params.height = width - 150
            view.layoutParams = params

            measureChild(view, width, width)

            var top = lastY
            var left = 0
            var right = left + width
            var bottom = top + width

            layoutDecorated(view, left, top, right, bottom)
            lastY = bottom
        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        offsetChildrenVertical(-dy)
        return dy
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }
}