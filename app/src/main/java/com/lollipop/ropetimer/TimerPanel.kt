package com.lollipop.ropetimer

import android.content.Context

/**
 * 计时器的面板
 */
abstract class TimerPanel<P : Protocol>(val context: Context, val protocol: P) {

    abstract fun attach()

    abstract fun detach()

}