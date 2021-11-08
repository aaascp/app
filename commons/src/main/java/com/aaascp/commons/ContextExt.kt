package com.aaascp.commons

import android.app.Activity
import android.view.Surface

fun Activity.getOrientation(): Orientation = Orientation.create(
    this.windowManager.defaultDisplay.rotation
)

sealed class Orientation {
    object PORTRAIT : Orientation()
    object LANDSCAPE : Orientation()

    companion object {
        fun create(rotation: Int): Orientation = when (rotation) {
            Surface.ROTATION_90 -> LANDSCAPE
            Surface.ROTATION_270 -> LANDSCAPE
            else -> PORTRAIT
        }
    }
}
