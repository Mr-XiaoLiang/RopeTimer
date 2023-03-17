package com.lollipop.ropetimer

import android.content.Context
import android.view.ViewManager

/**
 * 计时器的面板
 */
abstract class TimerPanel<P : Protocol>(val context: Context, val protocol: P) {

    abstract fun attach(viewManager: ViewManager)

    abstract fun detach()

}