package com.lollipop.ropetimer.utils.insets

class BoundsSnapshot(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    companion object {
        val EMPTY = BoundsSnapshot(0, 0, 0, 0)
    }
}