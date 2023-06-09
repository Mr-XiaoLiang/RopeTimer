package com.lollipop.ropetimer.utils

import android.graphics.Point
import android.graphics.PointF
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

/**
 * @author lollipop
 * @date 2020/5/1 20:45
 * View的拖拽辅助类
 */
class ViewDragHelper(private val view: View) : View.OnTouchListener {

    companion object {
        // 最大单击时长
        private const val SINGLE_TAP_TIME = 800L

        fun bind(view: View): ViewDragHelper {
            return ViewDragHelper(view)
        }
    }

    private var dragListener: OnViewDragListener? = null
    private var updateLocalListener: OnLocationUpdateListener? = null
    private var onDragTouchUpListener: OnDragTouchUpListener? = null
    private var onDragTouchDownListener: OnDragTouchDownListener? = null

    private val scaledTouchSlop = ViewConfiguration.get(view.context).scaledTouchSlop
    private var downTime = 0L
    private val moveOffset = PointF()
    private val lastPoint = PointF()
    private val downPoint = PointF()
    private val tempPoint = Point()
    private var allowClick = false
    private var startDrag = false
    private var dragCancel = true

    init {
        view.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v != view) {
            return false
        }
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 保存本次落点
                downPoint.set(event.activeX(), event.activeY())
                // 保存上次位置
                lastPoint.set(downPoint)
                // 更新按下时间
                downTime = SystemClock.uptimeMillis()
                // 默认为点击模式
                allowClick = true
                // 尚未开始拖拽
                startDrag = false
                // 事件是否取消？
                dragCancel = false
                onDragTouchDownListener?.onDragTouchDown()
            }
            MotionEvent.ACTION_MOVE -> {
                if (dragCancel) {
                    return false
                }
                onMove(event)
            }
            MotionEvent.ACTION_UP -> {
                if (dragCancel) {
                    return false
                }
                val now = SystemClock.uptimeMillis()
                var performedClick = false
                if (allowClick && now - downTime <= SINGLE_TAP_TIME) {
                    performedClick = true
                    view.performClick()
                }
                onDragTouchUpListener?.onDragTouchUp(performedClick)
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_CANCEL -> {
                dragCancel = true
                return false
            }
        }
        return true
    }

    private fun onMove(event: MotionEvent) {
        val x = event.activeX()
        val y = event.activeY()
        // 如果当前位置距离按下位置超过了最小标准，
        // 那么开始拖拽，否则将认为处于点击模式
        // 但是如果按下时间过长，仍然无法触发点击
        if (abs(x - downPoint.x) > scaledTouchSlop ||
            abs(y - downPoint.y) > scaledTouchSlop
        ) {
            startDrag = true
            allowClick = false
        }
        if (!startDrag) {
            return
        }
        dragView(x, y)
    }

    private fun dragView(x: Float, y: Float) {
        val moveX = x - lastPoint.x + moveOffset.x
        val moveY = y - lastPoint.y + moveOffset.y
        tempPoint.x = moveX.toInt()
        tempPoint.y = moveY.toInt()
        dragListener?.onViewDrag(view, tempPoint)
        val realX = tempPoint.x
        val realY = tempPoint.y
        moveOffset.x = moveX - realX
        moveOffset.y = moveY - realY
        lastPoint.x = x
        lastPoint.y = y
        updateLocalListener?.onLocationUpdate(view, realX, realY)
    }

    private fun MotionEvent.activeX(): Float {
        return this.rawX
    }

    private fun MotionEvent.activeY(): Float {
        return this.rawY
    }

    fun onLocationUpdate(listener: OnLocationUpdateListener?): ViewDragHelper {
        updateLocalListener = listener
        return this
    }

    fun onDragTouchUp(listener: OnDragTouchUpListener?): ViewDragHelper {
        onDragTouchUpListener = listener
        return this
    }

    fun onDragTouchDown(listener: OnDragTouchDownListener?): ViewDragHelper {
        onDragTouchDownListener = listener
        return this
    }

    fun onViewDrag(listener: OnViewDragListener?): ViewDragHelper {
        dragListener = listener
        return this
    }

    fun interface OnLocationUpdateListener {
        fun onLocationUpdate(view: View, offsetX: Int, offsetY: Int)
    }

    fun interface OnViewDragListener {
        fun onViewDrag(view: View, loc: Point)
    }

    fun interface OnDragTouchUpListener {
        fun onDragTouchUp(performedClick: Boolean)
    }

    fun interface OnDragTouchDownListener {
        fun onDragTouchDown()
    }

}