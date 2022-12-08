package com.wakeup.presentation.ui.globe

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(space: Float) : RecyclerView.ItemDecoration() {

    private var halfSpace = space / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.paddingLeft.toFloat() != halfSpace) {
            parent.setPadding(
                halfSpace.toInt(),
                halfSpace.toInt(),
                halfSpace.toInt(),
                halfSpace.toInt())
            parent.clipToPadding = false
        }
        outRect.top = halfSpace.toInt()
        outRect.bottom = halfSpace.toInt()
        outRect.left = halfSpace.toInt()
        outRect.right = halfSpace.toInt()
    }
}