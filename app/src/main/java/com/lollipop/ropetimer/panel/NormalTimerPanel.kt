package com.lollipop.ropetimer.panel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewManager
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.lollipop.ropetimer.TimerPanel
import com.lollipop.ropetimer.databinding.ItemFullTimerBinding
import com.lollipop.ropetimer.databinding.ItemMiniTimerBinding
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
    private val holderList = ArrayList<Holder>()

    private var panelState = PanelState.MINI

    private val miniTimerPanel: PanelMiniTimerBinding
    private val fullTimerPanel: PanelFullTimerBinding

    init {
        val layoutInflater = LayoutInflater.from(context)
        miniTimerPanel = PanelMiniTimerBinding.inflate(layoutInflater)
        fullTimerPanel = PanelFullTimerBinding.inflate(layoutInflater)
        FloatingViewHelper.bindDragByWindowManager(miniTimerPanel.root)
        FloatingViewHelper.bindDrag(fullTimerPanel.dragHolder) { _, offX, offY ->
            fullTimerPanel.timerPanel.offsetLeftAndRight(offX)
            fullTimerPanel.timerPanel.offsetTopAndBottom(offY)
        }
        miniTimerPanel.root.setOnClickListener {
            switchTo(PanelState.FULL)
        }
        fullTimerPanel.root.setOnClickListener {
            switchTo(PanelState.MINI)
        }
        fullTimerPanel.settingButton.setOnClickListener {
            switchTo(PanelState.SETTING)
        }
    }

    override fun attach(viewManager: ViewManager) {
        // 添加到悬浮窗
        attachView(viewManager, miniTimerPanel, WRAP_CONTENT, WRAP_CONTENT)
        attachView(viewManager, fullTimerPanel, MATCH_PARENT, MATCH_PARENT)
        updatePanelVisible()
        startTimer()
    }

    private fun attachView(
        viewManager: ViewManager,
        panel: ViewBinding,
        width: Int,
        height: Int
    ) {
        viewManager.addView(panel.root, ViewGroup.LayoutParams(width, height))
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

    private fun updateTime() {
        TODO("Not yet implemented")
    }

    private fun updatePanelVisible() {
        miniTimerPanel.root.isVisible = panelState.isMini
        val fullPanel = !panelState.isMini
        if (fullPanel) {
            val settingMode = panelState == PanelState.SETTING
            fullTimerPanel.settingButton.isInvisible = settingMode
            holderList.forEach {
                it.settingMode(settingMode)
            }
        }
        fullTimerPanel.root.isVisible = fullPanel
    }

    private fun switchTo(state: PanelState) {
        panelState = state
        updatePanelVisible()
    }

    private class Timer(
        val cover: Cover,
        val scale: Scale
    )

    private class Holder(
        private val miniItem: ItemMiniTimerBinding,
        private val fullItem: ItemFullTimerBinding
    ) {

        companion object {
            fun create(miniGroup: ViewGroup, fillGroup: ViewGroup): Holder {
                val layoutInflater = LayoutInflater.from(miniGroup.context)
                return Holder(
                    ItemMiniTimerBinding.inflate(layoutInflater, miniGroup, true),
                    ItemFullTimerBinding.inflate(layoutInflater, fillGroup, true),
                )
            }
        }

        private var timer: Timer? = null

        private var isSettingMode = false

        private var endTime: Long = 0

        fun settingMode(isSetting: Boolean) {
            this.isSettingMode = isSetting
            updateLayout()
        }

        fun updateTimer(timer: Timer) {
            this.timer = timer
            onTimerChanged()
        }

        private fun endTimeChanged(duration: Int) {
            val ms = duration * 1000L
            endTime = now() + ms
        }

        private fun updateCountdown() {
            val t = timer
            if (t == null) {
                miniItem.countdownView.isVisible = false
                fullItem.countdownView.isVisible = false
                return
            }
            val max = t.scale.max
            val value = ((endTime - now()) / 1000).toInt()
            miniItem.countdownView.setValue(max, value)
            fullItem.countdownView.setValue(max, value)
        }

        private fun onTimerChanged() {
            timer?.cover?.load(miniItem.coverView)
            timer?.cover?.load(fullItem.coverView)
            updateCountdown()
        }

        private fun updateLayout() {
            // TODO
            updateCountdown()
        }

        private fun now(): Long {
            return System.currentTimeMillis()
        }

    }

    private enum class PanelState {
        MINI,
        FULL,
        SETTING;

        val isMini: Boolean
            get() {
                return this == MINI
            }

    }

}