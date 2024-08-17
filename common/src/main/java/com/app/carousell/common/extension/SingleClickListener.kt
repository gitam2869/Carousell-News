package com.app.carousell.common.extension

import android.os.SystemClock
import android.view.View
import com.app.carousell.common.Constant

class SingleClickListener(
    private val intervalMillis: Long = Constant.SINGLE_CLICK_EVENT_DEBOUNCE_TIME,
    private val doClick: ((View) -> Unit)
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(v: View?) {
        if ((SystemClock.elapsedRealtime() - lastTimeClicked) >= intervalMillis) {
            v?.let {
                lastTimeClicked = SystemClock.elapsedRealtime()
                doClick(it)
            }
        }
    }
}