package com.lollipop.ropetimer.panel.normal

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.annotation.ColorInt
import com.lollipop.viewoverlay.OverlayDrawable
import kotlin.math.min

class FloatingTimeValueDrawable : OverlayDrawable() {

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    /**
     * 字体大小
     */
    var fontSize: Float
        get() {
            return paint.textSize
        }
        set(value) {
            paint.textSize = value
            changeFontOffset()
        }

    /**
     * 间距，文字气泡与内容主体之间的间距
     */
    var margin: Float = 0F

    /**
     * 内补白，气泡中文字与气泡边缘的水平方向间距
     */
    var paddingHorizontal: Float = 0F

    /**
     * 内补白，气泡中文字与气泡边缘的垂直方向间距
     */
    var paddingVertical: Float = 0F

    /**
     * 文字的颜色
     */
    @ColorInt
    var fontColor: Int = Color.WHITE

    /**
     * 气泡的颜色
     */
    @ColorInt
    var popupColor: Int = Color.GRAY

    /**
     * 气泡最小宽度
     */
    var popupMinWidth = 0F

    /**
     * 气泡最小高度
     */
    var popupMinHeight = 0F

    /**
     * 文本内容
     */
    var textValue = ""
        set(value) {
            field = value
            measureText()
        }

    /**
     * 锚点的Y轴位置
     */
    var anchorYWeight = 0F

    private var fontOffsetY = 0F

    private val leftPopupBounds = RectF()

    private val rightPopupBounds = RectF()

    private var radius = 0F

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        changeFontOffset()
        measureText()
    }

    private fun changeFontOffset() {
        val fontMetrics = paint.fontMetrics
        fontOffsetY = ((fontMetrics.descent - fontMetrics.ascent) / 2) - fontMetrics.descent
    }

    private fun measureText() {
        val textWidth = paint.measureText(textValue)
        var popWidth = textWidth + paddingHorizontal + paddingHorizontal
        var popHeight = fontSize + paddingVertical + paddingVertical
        val centerY = overlayBounds.height() * anchorYWeight + overlayBounds.top
        val popTop = centerY - (popHeight * 0.5F)
        if (popWidth < popupMinWidth) {
            popWidth = popupMinWidth
        }
        if (popHeight < popupMinHeight) {
            popHeight = popupMinHeight
        }
        radius = min(popHeight, popWidth) * 0.5F
        updateLeftPopupBounds(popWidth, popHeight, popTop)
        updateRightPopupBounds(popWidth, popHeight, popTop)
        invalidateSelf()
    }

    private fun updateLeftPopupBounds(width: Float, height: Float, top: Float) {
        val left = overlayBounds.left - margin - width
        leftPopupBounds.set(left, top, left + width, top + height)
    }

    private fun updateRightPopupBounds(width: Float, height: Float, top: Float) {
        val left = overlayBounds.right + margin
        rightPopupBounds.set(left, top, left + width, top + height)
    }

    override fun draw(canvas: Canvas) {
        paint.color = popupColor
        canvas.drawRoundRect(leftPopupBounds, radius, radius, paint)
        canvas.drawRoundRect(rightPopupBounds, radius, radius, paint)
        paint.color = fontColor
        canvas.drawText(
            textValue,
            leftPopupBounds.centerX(),
            leftPopupBounds.centerY() + fontOffsetY,
            paint
        )
        canvas.drawText(
            textValue,
            rightPopupBounds.centerX(),
            rightPopupBounds.centerY() + fontOffsetY,
            paint
        )
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}