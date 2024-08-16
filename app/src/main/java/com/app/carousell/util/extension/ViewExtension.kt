package com.app.carousell.util.extension

import android.view.View
import com.app.carousell.util.Constant

object ViewExtension {
    fun View.setOnSingleClickListener(
        intervalMillis: Long = Constant.SINGLE_CLICK_EVENT_DEBOUNCE_TIME,
        doClick: (View) -> Unit
    ) = setOnClickListener(SingleClickListener(intervalMillis, doClick))

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }
}