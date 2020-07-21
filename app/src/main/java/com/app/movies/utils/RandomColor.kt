package com.app.movies.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import java.util.*

class RandomColor {
    companion object {
        fun getRandomDrawableColor(): ColorDrawable? {
            val idx: Int = Random().nextInt(vibrantLightColorList.size-1)
            return vibrantLightColorList[idx]
        }

        private val vibrantLightColorList = arrayOf(
            ColorDrawable(Color.parseColor("#9ACCCD")), ColorDrawable(Color.parseColor("#8FD8A0")),
            ColorDrawable(Color.parseColor("#CBD890")), ColorDrawable(Color.parseColor("#DACC8F")),
            ColorDrawable(Color.parseColor("#D9A790")), ColorDrawable(Color.parseColor("#D18FD9")),
            ColorDrawable(Color.parseColor("#FF6772")), ColorDrawable(Color.parseColor("#DDFB5C"))
        )
    }
}