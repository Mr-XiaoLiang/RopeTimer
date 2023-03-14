package com.lollipop.ropetimer.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.lollipop.ropetimer.R
import kotlin.math.min

class LightView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0
) : AppCompatImageView(context, attributeSet, style) {

    private val lightDrawable = LightDrawable()

    var color: Int
        get() {
            return lightDrawable.color
        }
        set(value) {
            lightDrawable.color = value
        }

    init {
        setImageDrawable(lightDrawable)

        attributeSet?.let { attrs ->
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LightView)
            color = typeArray.getColor(R.styleable.LightView_color, Color.BLACK)
            typeArray.recycle()
        }

    }

    private class LightDrawable : Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
        }

        private var radius = 0F
        private val boundsF = RectF()

        var color: Int
            get() {
                return paint.color
            }
            set(value) {
                paint.color = value
            }

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            radius = min(bounds.width(), bounds.height()) * 0.5F
            boundsF.set(bounds)
        }

        override fun draw(canvas: Canvas) {
            canvas.drawRoundRect(boundsF, radius, radius, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

    }

}