package com.lollipop.ropetimer.panel

import android.content.Context
import android.view.LayoutInflater
import com.lollipop.ropetimer.TimerPanel
import com.lollipop.ropetimer.databinding.PanelFullTimerBinding
import com.lollipop.ropetimer.databinding.PanelMiniTimerBinding
import com.lollipop.ropetimer.protocol.Cover
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.protocol.Scale

open class NormalTimerPanel(context: Context, protocol: NormalProtocol) :
    TimerPanel<NormalProtocol>(context, protocol) {

    private val timerList = ArrayList<Timer>()

    private val miniTimerPanel: PanelMiniTimerBinding
    private val fullTimerPanel: PanelFullTimerBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        miniTimerPanel = PanelMiniTimerBinding.inflate(layoutInflater)
        fullTimerPanel = PanelFullTimerBinding.inflate(layoutInflater)
    }

    override fun attach() {
        // 添加到悬浮窗
        updatePanelVisible()
        updateTime()
        TODO("Not yet implemented")
    }

    override fun detach() {
        TODO("Not yet implemented")
    }

    private fun updateTime() {
        TODO("Not yet implemented")
    }

    private fun updatePanelVisible() {
        TODO("Not yet implemented")
    }

    private class Timer(
        val cover: Cover,
        val scale: Scale
    )

}