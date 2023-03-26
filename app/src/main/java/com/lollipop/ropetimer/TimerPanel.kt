package com.lollipop.ropetimer

import android.content.Context
import android.view.ViewManager
import com.lollipop.ropetimer.view.LightView

/**
 * 计时器的面板
 */
abstract class TimerPanel<P : Protocol>(val context: Context, val protocol: P) {

    abstract fun attach(viewManager: ViewManager)

    abstract fun detach()

    protected fun updateTheme(color: Int, vararg views: LightView) {
        views.forEach {
            it.color = color
        }
    }

}