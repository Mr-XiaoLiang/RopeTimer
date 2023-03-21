package com.lollipop.ropetimer.panel.normal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.lollipop.ropetimer.databinding.ItemFullTimerBinding
import com.lollipop.ropetimer.databinding.ItemMiniTimerBinding
import com.lollipop.ropetimer.utils.ViewDragHelper

class NormalTimerHolder(
    private val miniItem: ItemMiniTimerBinding,
    private val fullItem: ItemFullTimerBinding
) : ViewDragHelper.OnLocationUpdateListener,
    ViewDragHelper.OnDragTouchUpListener,
    ViewDragHelper.OnDragTouchDownListener {

    companion object {
        fun create(miniGroup: ViewGroup, fillGroup: ViewGroup): NormalTimerHolder {
            val layoutInflater = LayoutInflater.from(miniGroup.context)
            return NormalTimerHolder(
                ItemMiniTimerBinding.inflate(layoutInflater, miniGroup, true),
                ItemFullTimerBinding.inflate(layoutInflater, fillGroup, true),
            )
        }
    }

    private var timer: NormalTimer? = null

    private var isSettingMode = false

    private var endTime: Long = 0

    init {
        ViewDragHelper.bind(fullItem.countdownHolderView)
            .onLocationUpdate(this)
            .onDragTouchUp(this)
    }

    fun settingMode(isSetting: Boolean) {
        this.isSettingMode = isSetting
        updateLayout()
    }

    fun updateTimer(timer: NormalTimer) {
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
        miniItem.countdownView.isVisible = true
        fullItem.countdownView.isVisible = true
        miniItem.countdownView.setValue(max, value)
        fullItem.countdownView.setValue(max, value)
    }

    private fun onTimerChanged() {
        timer?.cover?.load(miniItem.coverView)
        timer?.cover?.load(fullItem.coverView)
        updateCountdown()
    }

    private fun updateLayout() {
        if (isSettingMode) {
            fullItem.countdownView.isVisible = false
            fullItem.deleteButton.isVisible = true
        } else {
            fullItem.countdownView.isVisible = true
            fullItem.deleteButton.isVisible = false
            updateCountdown()
        }
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }

    override fun onLocationUpdate(view: View, offsetX: Int, offsetY: Int) {
        fullItem.countdownHolderView.offsetTopAndBottom(offsetY)
        // 需要展示悬浮的数字
    }

    override fun onDragTouchUp(performedClick: Boolean) {
        if (performedClick) {
            return
        }
        // 需要滚回顶部，并且开始计时

        TODO("Not yet implemented")
    }

    override fun onDragTouchDown() {
        TODO("Not yet implemented")
    }

}