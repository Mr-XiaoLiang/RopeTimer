package com.lollipop.ropetimer.panel.normal

import android.animation.ValueAnimator
import android.graphics.Color
import android.util.TypedValue
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
    ViewDragHelper.OnDragTouchDownListener,
    ValueAnimator.AnimatorUpdateListener {

    companion object {
        fun create(miniGroup: ViewGroup, fillGroup: ViewGroup): NormalTimerHolder {
            val layoutInflater = LayoutInflater.from(miniGroup.context)
            return NormalTimerHolder(
                ItemMiniTimerBinding.inflate(layoutInflater, miniGroup, true),
                ItemFullTimerBinding.inflate(layoutInflater, fillGroup, true),
            )
        }

        private const val FLY_UP_DURATION = 300L
    }

    private var timer: NormalTimer? = null

    private var isSettingMode = false

    private var endTime: Long = 0

    private val animator by lazy {
        ValueAnimator().apply {
            addUpdateListener(this@NormalTimerHolder)
        }
    }

    private val floatingTimeValueDrawable = FloatingTimeValueDrawable()

    init {
        ViewDragHelper.bind(fullItem.countdownHolderView)
            .onLocationUpdate(this)
            .onDragTouchUp(this)
            .onDragTouchDown(this)
        floatingTimeValueDrawable.updateAnchorView(fullItem.root)
        floatingTimeValueDrawable.fontColor = Color.GRAY
        floatingTimeValueDrawable.popupColor = Color.WHITE
        floatingTimeValueDrawable.fontSize = getPixelSize(14F)
        floatingTimeValueDrawable.margin = getPixelSize(10F)
        floatingTimeValueDrawable.paddingHorizontal = getPixelSize(16F)
        floatingTimeValueDrawable.paddingVertical = getPixelSize(8F)
        floatingTimeValueDrawable.popupMinHeight = getPixelSize(32F)
        floatingTimeValueDrawable.popupMinWidth = getPixelSize(48F)

    }

    private fun getPixelSize(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            fullItem.root.resources.displayMetrics
        )
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

    fun updateCountdown() {
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
        floatingTimeValueDrawable.textValue = getCountdownHolderValue(
            getItemOffsetY(fullItem.root, fullItem.countdownHolderView)
        ).toString()
        floatingTimeValueDrawable.anchorYWeight = getItemOffsetYCenter(
            fullItem.root,
            fullItem.countdownHolderView
        )
    }

    override fun onDragTouchUp(performedClick: Boolean) {
        floatingTimeValueDrawable.detachFromOverlay()
        if (performedClick) {
            return
        }
        // 需要滚回顶部，并且开始计时
        val offsetY = getItemOffsetY(fullItem.root, fullItem.countdownHolderView)
        val countdownValue = getCountdownHolderValue(offsetY)
        endTimeChanged(countdownValue)
        animator.cancel()
        animator.duration = (FLY_UP_DURATION * offsetY).toLong()
        animator.setFloatValues(offsetY, 0F)
        animator.start()
    }

    private fun getCountdownHolderValue(float: Float): Int {
        return (float * (timer?.scale?.max ?: 120)).toInt()
    }

    /**
     * Y轴偏移量的权重
     * [0, 1]
     */
    private fun getItemOffsetY(group: ViewGroup, item: View): Float {
        val itemTop = item.top - group.paddingTop
        val groupHeight = group.height - group.paddingTop - group.paddingBottom
        val itemHeight = item.height
        return itemTop * 1F / (groupHeight - itemHeight)
    }

    /**
     * Item的中心点相对于Group本身的高度而言的位置
     */
    private fun getItemOffsetYCenter(group: ViewGroup, item: View): Float {
        return ((item.top + item.bottom) * 0.5F) / group.height
    }

    override fun onDragTouchDown() {
        animator.cancel()
        floatingTimeValueDrawable.attachToOverlay()
        val layout = fullItem.root
        floatingTimeValueDrawable.setOverlayBounds(0, 0, layout.width, layout.height)
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        if (animation === animator) {
            val progress = animation.animatedValue
            if (progress is Float) {
                updateHolderByAnimation(progress)
            }
        }
    }

    private fun updateHolderByAnimation(progress: Float) {
        val group = fullItem.root
        val holder = fullItem.countdownHolderView
        val groupHeight = group.height - group.paddingTop - group.paddingBottom - holder.height
        val top = (groupHeight * progress) + group.paddingTop
        val offsetY = (top - holder.top).toInt()
        if (offsetY != 0) {
            holder.offsetTopAndBottom(offsetY)
        }
    }

}