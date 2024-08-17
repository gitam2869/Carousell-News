package com.app.carousell.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val spacing: Int,
    private val isEdgeEnabled: Boolean = true
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        with(outRect) {
            top = if (position == 0) {
                if (isEdgeEnabled) spacing else 0
            } else spacing

            bottom = if (position == state.itemCount - 1) {
                if (isEdgeEnabled) spacing else 0
            } else {
                0
            }

            left = if (isEdgeEnabled) spacing else 0
            right = if (isEdgeEnabled) spacing else 0
        }
    }
}