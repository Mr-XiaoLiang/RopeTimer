package com.lollipop.ropetimer.panel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewManager
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.lollipop.ropetimer.TimerPanel
import com.lollipop.ropetimer.databinding.PanelFullTimerBinding
import com.lollipop.ropetimer.databinding.PanelMiniTimerBinding
import com.lollipop.ropetimer.protocol.Cover
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.protocol.Scale
import com.lollipop.ropetimer.utils.FloatingViewHelper

open class NormalTimerPanel(context: Context, protocol: NormalProtocol) :
    TimerPanel<NormalProtocol>(context, protocol) {

    companion object {
        private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
        private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private val timerList = ArrayList<Timer>()

    private var panelState = PanelState.MINI

    private val miniTimerPanel: PanelMiniTimerBinding
    private val fullTimerPanel: PanelFullTimerBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        miniTimerPanel = PanelMiniTimerBinding.inflate(layoutInflater)
        fullTimerPanel = PanelFullTimerBinding.inflate(layoutInflater)
    }

    override fun attach(viewManager: ViewManager) {
        // 添加到悬浮窗
        attachView(viewManager, miniTimerPanel, WRAP_CONTENT, WRAP_CONTENT)
        attachView(viewManager, fullTimerPanel, MATCH_PARENT, MATCH_PARENT)
        updatePanelVisible()
        startTimer()
    }

    override fun detach() {
        FloatingViewHelper.detach(miniTimerPanel)
        FloatingViewHelper.detach(fullTimerPanel)
    }

    private fun startTimer() {
        TODO("Not yet implemented")
    }

    private fun stopTimer() {
        TODO("Not yet implemented")
    }

    private fun attachView(
        viewManager: ViewManager,
        panel: ViewBinding,
        width: Int,
        height: Int
    ) {
        viewManager.addView(panel.root, ViewGroup.LayoutParams(width, height))
    }

    private fun updateTime() {
        TODO("Not yet implemented")
    }

    private fun updatePanelVisible() {
        miniTimerPanel.root.isVisible = panelState == PanelState.MINI
        fullTimerPanel.root.isVisible = panelState == PanelState.FULL
    }

    private fun switchTo(state: PanelState) {
        panelState = state
        updatePanelVisible()
    }

    private class Timer(
        val cover: Cover,
        val scale: Scale
    )

    private enum class PanelState {
        MINI,
        FULL
    }

}