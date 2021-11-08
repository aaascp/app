package com.aaascp.commons

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private vararg val margin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            when {
                isTheSameMarginForAllCoordinates() -> {
                    left = margin[0]
                    if (parent.getChildAdapterPosition(view) == 0) {
                        top = margin[0]
                    }
                    right = margin[0]
                    bottom = margin[0]
                }
                else -> {
                    left = margin[0]
                    if (parent.getChildAdapterPosition(view) == 0) {
                        top = margin[1]
                    }
                    right = margin[2]
                    bottom = margin[3]
                }
            }
        }
    }

    private fun isTheSameMarginForAllCoordinates() = margin.isNotEmpty() && margin.size == 1
}
