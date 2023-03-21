package com.lollipop.ropetimer.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.lollipop.ropetimer.R
import kotlin.math.max
import kotlin.math.min

class CountdownView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, style: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attributeSet, style) {

    private val countdownDrawable = CountdownDrawable()
    private var displayCallback: DisplayCallback = NumberDisplay()

    private var maxValue = 100
    private var currentValue = 100
    var maskColor: Int
        get() {
            return countdownDrawable.maskColor
        }
        set(value) {
            countdownDrawable.maskColor = value
        }

    var progressColor: Int
        get() {
            return countdownDrawable.progressColor
        }
        set(value) {
            countdownDrawable.progressColor = value
        }

    init {
        background = countdownDrawable
        attributeSet?.let { attrs ->
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CountdownView)
            maxValue = typeArray.getInteger(R.styleable.CountdownView_countdownMax, 100)
            currentValue = typeArray.getInteger(R.styleable.CountdownView_countdownValue, 0)
            maskColor = typeArray.getColor(
                R.styleable.CountdownView_countdownMask, Color.TRANSPARENT
            )
            progressColor = typeArray.getColor(
                R.styleable.CountdownView_countdownArc, Color.BLACK
            )
            typeArray.recycle()
        }
        changeProgress()
    }

    fun setValue(max: Int, value: Int) {
        maxValue = max
        currentValue = value
        changeProgress()
    }

    fun setCurrent(value: Int) {
        currentValue = value
        changeProgress()
    }

    fun setDisplayCallback(callback: DisplayCallback) {
        this.displayCallback = callback
        changeProgress()
    }

    private fun changeProgress() {
        if (currentValue <= 0) {
            countdownDrawable.isShown = false
            invalidate()
            return
        }
        countdownDrawable.isShown = true
        countdownDrawable.process = currentValue * 1F / maxValue
        text = displayCallback.getValue(maxValue, currentValue)
        invalidate()
    }

    fun interface DisplayCallback {
        fun getValue(max: Int, value: Int): CharSequence
    }

    class NumberDisplay : DisplayCallback {
        override fun getValue(max: Int, value: Int): CharSequence {
            return value.toString()
        }
    }

    private class CountdownDrawable : Drawable() {

        var maskColor: Int = Color.TRANSPARENT

        var progressColor: Int = Color.BLACK

        private var alphaWeight: Float = 1F

        private val paint = Paint().apply {
            isDither = true
            isAntiAlias = true
        }

        var process: Float = 0F
            set(value) {
                field = min(360F, max(0F, value))
            }

        private val boundsF = RectF()

        var isShown = true

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            boundsF.set(bounds)
            invalidateSelf()
        }

        override fun draw(canvas: Canvas) {
            if (!isShown) {
                return
            }
            bindPaintColor(maskColor)
            canvas.drawRect(boundsF, paint)
            bindPaintColor(progressColor)
            val sweepAngle = 360 * process
            val startAngle = 360 - sweepAngle - 90
            canvas.drawArc(boundsF, startAngle, sweepAngle, true, paint)
        }

        private fun bindPaintColor(color: Int) {
            val colorAlpha = Color.alpha(color)
            val alpha = min(255, max(0, (colorAlpha * alphaWeight).toInt()))
            val newColor = color.and(0xFFFFFF).or(alpha.shl(24))
            paint.color = newColor
        }

        override fun setAlpha(alpha: Int) {
            this.alphaWeight = alpha / 255F
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("PixelFormat.TRANSPARENT", "android.graphics.PixelFormat")
        )
        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

    }

}