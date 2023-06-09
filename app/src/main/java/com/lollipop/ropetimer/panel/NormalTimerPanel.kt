package com.lollipop.ropetimer.panel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewManager
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.lollipop.ropetimer.TimerPanel
import com.lollipop.ropetimer.databinding.PanelFullTimerBinding
import com.lollipop.ropetimer.databinding.PanelMiniTimerBinding
import com.lollipop.ropetimer.panel.normal.NormalPanelState
import com.lollipop.ropetimer.panel.normal.NormalTimer
import com.lollipop.ropetimer.panel.normal.NormalTimerCreator
import com.lollipop.ropetimer.panel.normal.NormalTimerHolder
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.utils.FloatingViewHelper
import com.lollipop.ropetimer.utils.ViewDragHelper

open class NormalTimerPanel(context: Context, protocol: NormalProtocol) :
    TimerPanel<NormalProtocol>(context, protocol) {

    companion object {
        private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
        private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private val timerList = ArrayList<NormalTimer>()
    private val holderList = ArrayList<NormalTimerHolder>()

    private var panelState = NormalPanelState.MINI

    private val miniTimerPanel: PanelMiniTimerBinding
    private val fullTimerPanel: PanelFullTimerBinding

    private val timerCreator: NormalTimerCreator

    private val timerHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val updateTask = Runnable {
        updateTime()
    }

    init {
        val layoutInflater = LayoutInflater.from(context)
        miniTimerPanel = PanelMiniTimerBinding.inflate(layoutInflater)
        fullTimerPanel = PanelFullTimerBinding.inflate(layoutInflater)
        timerCreator = NormalTimerCreator(
            protocol,
            fullTimerPanel.coverSelectView,
            fullTimerPanel.timeSelectView
        )
        bindListener()
        bindTheme()
    }

    private fun bindListener() {
        FloatingViewHelper.bindDrag(miniTimerPanel.root)
        ViewDragHelper.bind(fullTimerPanel.dragHolder).onLocationUpdate { _, offX, offY ->
            fullTimerPanel.timerPanel.offsetLeftAndRight(offX)
            fullTimerPanel.timerPanel.offsetTopAndBottom(offY)
        }
        miniTimerPanel.root.setOnClickListener {
            switchTo(NormalPanelState.FULL)
        }
        fullTimerPanel.root.setOnClickListener {
            switchTo(NormalPanelState.MINI)
        }
        fullTimerPanel.settingButton.setOnClickListener {
            switchTo(NormalPanelState.SETTING)
        }
        fullTimerPanel.doneButton.setOnClickListener {
            switchTo(NormalPanelState.FULL)
        }
        fullTimerPanel.addButton.setOnClickListener {
            switchTo(NormalPanelState.EDIT)
        }
        fullTimerPanel.createButton.setOnClickListener {
            addHolder()
            switchTo(NormalPanelState.SETTING)
        }
    }

    private fun bindTheme() {
        updateTheme(
            protocol.themeColor,
            miniTimerPanel.endLightView,
            miniTimerPanel.startLightView,
            fullTimerPanel.settingButtonBackground,
            fullTimerPanel.touchLightView,
            fullTimerPanel.addButtonBackground,
            fullTimerPanel.doneButtonBackground,
            fullTimerPanel.createButtonBackground,
        )
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
        stopTimer()
        FloatingViewHelper.detach(miniTimerPanel)
        FloatingViewHelper.detach(fullTimerPanel)
    }

    private fun startTimer() {
        timerHandler.removeCallbacks(updateTask)
        timerHandler.post(updateTask)
    }

    private fun stopTimer() {
        timerHandler.removeCallbacks(updateTask)
    }

    private fun updateTime() {
        holderList.forEach {
            it.updateCountdown()
        }
        timerHandler.postDelayed(updateTask, 250L)
    }

    private fun updatePanelVisible() {
        for (index in holderList.indices) {
            holderList[index].updateTimer(timerList[index])
        }
        fullTimerPanel.settingButton.isInvisible = panelState.isSetting
        fullTimerPanel.addButton.isInvisible = panelState.isEdit
        fullTimerPanel.doneButton.isInvisible = panelState.isEdit
        miniTimerPanel.root.isVisible = panelState.isMini
        val fullPanel = !panelState.isMini
        if (fullPanel) {
            if (panelState.isEdit) {
                fullTimerPanel.createTimerPanel.isVisible = true
                fullTimerPanel.timerGroup.isVisible = false
            } else {
                fullTimerPanel.createTimerPanel.isVisible = false
                fullTimerPanel.timerGroup.isVisible = true
                val settingMode = panelState.isSetting
                holderList.forEach {
                    it.settingMode(settingMode)
                }
            }
        }
        fullTimerPanel.root.isVisible = fullPanel
    }

    private fun addHolder() {
        val cover = timerCreator.currentCover()
        val scale = timerCreator.currentScale()
        addHolder(NormalTimer(cover, scale))
    }

    private fun addHolder(timer: NormalTimer) {
        timerList.add(timer)
        holderList.add(
            NormalTimerHolder.create(
                miniTimerPanel.timerGroup,
                fullTimerPanel.timerGroup
            )
        )
    }

    private fun switchTo(state: NormalPanelState) {
        panelState = state
        updatePanelVisible()
    }

}