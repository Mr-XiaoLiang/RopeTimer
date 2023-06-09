package com.lollipop.clip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import kotlin.math.min

class RoundClipLayout(
    context: Context, attrs: AttributeSet?, style: Int
) : FrameLayout(context, attrs, style) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val radius = FloatArray(8)
    private val clipPath = Path()

    private var strokeWidth = 0
    private val strokePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            isDither = true
            setStyle(Paint.Style.STROKE)
        }
    }

    var isStrokeVisible: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var maxRound = false
        set(value) {
            field = value
            onRadiusChanged()
        }

    init {
        attrs?.let { a ->
            val typeArray = context.obtainStyledAttributes(a, R.styleable.RoundClipLayout)
            maxRound = typeArray.getBoolean(R.styleable.RoundClipLayout_clipMaxRound, false)
            val defRadius = typeArray.getDimensionPixelSize(
                R.styleable.RoundClipLayout_android_radius, 0
            )
            val leftTop = typeArray.getDimensionPixelSize(
                R.styleable.RoundClipLayout_leftTop, defRadius
            ).toFloat()
            val rightTop = typeArray.getDimensionPixelSize(
                R.styleable.RoundClipLayout_rightTop, defRadius
            ).toFloat()
            val rightBottom = typeArray.getDimensionPixelSize(
                R.styleable.RoundClipLayout_rightBottom, defRadius
            ).toFloat()
            val leftBottom = typeArray.getDimensionPixelSize(
                R.styleable.RoundClipLayout_leftBottom, defRadius
            ).toFloat()
            setRadius(leftTop, rightTop, rightBottom, leftBottom)
            setStrokeColor(
                typeArray.getColor(
                    R.styleable.RoundClipLayout_clipStrokeColor, Color.BLACK
                )
            )
            setStrokeWidth(
                typeArray.getDimensionPixelSize(
                    R.styleable.RoundClipLayout_clipStrokeWidth, 0
                )
            )
            typeArray.recycle()
        }
    }

    fun setRadius(
        leftTopX: Float,
        leftTopY: Float,
        rightTopX: Float,
        rightTopY: Float,
        rightBottomX: Float,
        rightBottomY: Float,
        leftBottomX: Float,
        leftBottomY: Float
    ) {
        radius[0] = leftTopX
        radius[1] = leftTopY

        radius[2] = rightTopX
        radius[3] = rightTopY

        radius[4] = rightBottomX
        radius[5] = rightBottomY

        radius[6] = leftBottomX
        radius[7] = leftBottomY
        onRadiusChanged()
    }

    fun setRadius(
        leftTop: Float,
        rightTop: Float,
        rightBottom: Float,
        leftBottom: Float,
    ) {
        setRadius(
            leftTop, leftTop,
            rightTop, rightTop,
            rightBottom, rightBottom,
            leftBottom, leftBottom
        )
    }

    fun setRadiusDp(
        leftTop: Float,
        rightTop: Float,
        rightBottom: Float,
        leftBottom: Float,
    ) {
        setRadius(
            getDp(leftTop),
            getDp(rightTop),
            getDp(rightBottom),
            getDp(leftBottom),
        )
    }

    fun setRadius(r: Float) {
        setRadius(r, r, r, r)
    }

    fun setRadiusDp(r: Float) {
        setRadius(getDp(r))
    }

    private fun getDp(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        )
    }

    private fun onRadiusChanged() {
        clipPath.reset()
        if (width < 1 || height < 1) {
            return
        }
        val radii = FloatArray(radius.size)
        val boundsLeft: Float
        val boundsTop: Float
        val boundsRight: Float
        val boundsBottom: Float
        if (maxRound) {
            val r = min(width, height) * 0.5F
            for (i in radii.indices) {
                radii[i] = r
            }
        } else {
            for (i in radius.indices) {
                radii[i] = radius[i]
            }
        }
        if (strokeWidth > 0) {
            val halfStroke = strokeWidth * 0.5F
            for (i in radii.indices) {
                radii[i] = radii[i] - halfStroke
            }
            boundsLeft = halfStroke
            boundsTop = halfStroke
            boundsRight = width - halfStroke
            boundsBottom = height - halfStroke
        } else {
            boundsLeft = 0F
            boundsTop = 0F
            boundsRight = width.toFloat()
            boundsBottom = height.toFloat()
        }
        clipPath.addRoundRect(
            boundsLeft,
            boundsTop,
            boundsRight,
            boundsBottom,
            radii,
            Path.Direction.CW
        )
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
        strokePaint.strokeWidth = width.toFloat()
        onRadiusChanged()
        invalidate()
    }

    fun setStrokeColor(color: Int) {
        strokePaint.color = color
        invalidate()
    }

    fun setStrokeShader(shader: Shader?) {
        strokePaint.shader = shader
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        onRadiusChanged()
    }

    override fun draw(canvas: Canvas) {
        if (clipPath.isEmpty) {
            super.draw(canvas)
            return
        }
        val saveCount = canvas.save()
        canvas.clipPath(clipPath)
        super.draw(canvas)
        canvas.restoreToCount(saveCount)
        drawStroke(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (clipPath.isEmpty) {
            super.dispatchDraw(canvas)
            return
        }
        val saveCount = canvas.save()
        canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(saveCount)
        drawStroke(canvas)
    }

    private fun drawStroke(canvas: Canvas) {
        if (isStrokeVisible) {
            if (strokeWidth > 0) {
                canvas.drawPath(clipPath, strokePaint)
            }
        }
    }


}