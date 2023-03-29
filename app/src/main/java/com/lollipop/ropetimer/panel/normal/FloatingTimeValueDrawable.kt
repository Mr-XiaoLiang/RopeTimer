package com.lollipop.ropetimer.panel.normal

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.lollipop.viewoverlay.OverlayDrawable

class FloatingTimeValueDrawable : OverlayDrawable() {

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        textSize
    }

    /**
     * 字体大小
     */
    var fontSize: Float = 16F

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


    override fun draw(canvas: Canvas) {

        TODO("Not yet implemented")
    }

    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }
}