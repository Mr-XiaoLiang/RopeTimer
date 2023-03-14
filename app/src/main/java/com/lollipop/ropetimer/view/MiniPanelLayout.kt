package com.lollipop.ropetimer.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.lollipop.ropetimer.R
import com.lollipop.ropetimer.view.TimerLinearLayout.Orientation.*
import kotlin.math.min

class TimerLinearLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0
) : ViewGroup(context, attributeSet, style) {

    private var itemWidth: Int = 1
    private var itemHeight: Int = 1
    private var itemInterval: Int = 0
    private var orientation: Orientation = HORIZONTAL

    init {
        attributeSet?.let { attrs ->
            val defItemSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                18F,
                context.resources.displayMetrics
            ).toInt()
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TimerLinearLayout)
            itemWidth = typeArray.getDimensionPixelSize(
                R.styleable.TimerLinearLayout_tll_itemWidth,
                defItemSize
            )
            itemHeight = typeArray.getDimensionPixelSize(
                R.styleable.TimerLinearLayout_tll_itemHeight,
                defItemSize
            )
            itemInterval = typeArray.getDimensionPixelSize(
                R.styleable.TimerLinearLayout_tll_itemInterval,
                0
            )
            val orientationValue = typeArray.getInteger(
                R.styleable.TimerLinearLayout_tll_orientation,
                HORIZONTAL.value
            )
            orientation =
                values().find { it.value == orientationValue } ?: HORIZONTAL
            typeArray.recycle()
        }
        if (isInEditMode) {
            addView(View(context).apply { setBackgroundColor(Color.RED) })
            addView(View(context).apply { setBackgroundColor(Color.GREEN) })
            addView(View(context).apply { setBackgroundColor(Color.BLUE) })
            addView(View(context).apply { setBackgroundColor(Color.GRAY) })
            addView(View(context).apply { setBackgroundColor(Color.CYAN) })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
        val count = childCount
        for (index in 0 until count) {
            getChildAt(index).measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
        var contentWidth = paddingLeft + paddingRight
        var contentHeight = paddingTop + paddingBottom
        when (orientation) {
            HORIZONTAL -> {
                contentWidth += count * (itemWidth + itemInterval) - itemInterval
                contentHeight += itemHeight
            }
            VERTICAL -> {
                contentWidth += itemWidth
                contentHeight += count * (itemHeight + itemInterval) - itemInterval
            }
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                contentWidth = widthSize
            }
            MeasureSpec.AT_MOST -> {
                contentWidth = min(contentWidth, widthSize)
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                contentHeight = heightSize
            }
            MeasureSpec.AT_MOST -> {
                contentHeight = min(contentHeight, heightSize)
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }
        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        when (orientation) {
            HORIZONTAL -> {
                layoutByHorizontal()
            }
            VERTICAL -> {
                layoutByVertical()
            }
        }
    }

    private fun layoutByHorizontal() {
        val childTop = paddingTop
        var childLeft = paddingLeft
        val childWidth = itemWidth
        val childHeight = itemHeight
        val interval = itemInterval
        val count = childCount
        for (index in 0 until count) {
            getChildAt(index).layout(
                childLeft,
                childTop,
                childLeft + childWidth,
                childTop + childHeight
            )
            childLeft += childWidth
            childLeft += interval
        }
    }

    private fun layoutByVertical() {
        var childTop = paddingTop
        val childLeft = paddingLeft
        val childWidth = itemWidth
        val childHeight = itemHeight
        val interval = itemInterval
        val count = childCount
        for (index in 0 until count) {
            getChildAt(index).layout(
                childLeft,
                childTop,
                childLeft + childWidth,
                childTop + childHeight
            )
            childTop += childHeight
            childTop += interval
        }
    }

    enum class Orientation(val value: Int) {
        HORIZONTAL(0),
        VERTICAL(1)
    }

}