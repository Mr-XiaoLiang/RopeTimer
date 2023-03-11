package com.lollipop.viewoverlay

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.view.ViewOverlay
import android.view.ViewParent

/**
 * 模仿 com.google.android.material.internal.DescendantOffsetUtils
 * 制作的工具类
 * 提供一些简单的View叠加绘制工具
 */
object OverlayHelper {

    var isDebug: Boolean = false

    fun offsetDescendantRect(view: View, rect: Rect, outRect: Rect): Result {
        try {
            val contentView = getContentView(view)
                ?: return Result.Failure(IllegalArgumentException("contentView not found"))
            outRect.set(rect)
            DescendantOffsetUtils.offsetDescendantRect(contentView, view, rect)
            return Result.Successful
        } catch (e: Throwable) {
            return Result.Failure(e)
        }
    }

    fun getContentOverlay(view: View): ViewOverlay? {
        return getContentView(view)?.overlay
    }

    fun getContentView(view: View?): ViewGroup? {
        if (view == null) {
            return null
        }
        val rootView = view.rootView
        val contentView = rootView.findViewById<ViewGroup>(android.R.id.content)
        if (contentView != null) {
            return contentView
        }

        // Account for edge cases: Parent's parent can be null without ever having found
        // android.R.id.content (e.g. if view is in an overlay during a transition).
        // Additionally, sometimes parent's parent is neither a ViewGroup nor a View (e.g. if view
        // is in a PopupWindow).
        return if (rootView !== view && rootView is ViewGroup) {
            rootView
        } else {
            null
        }
    }

    private object DescendantOffsetUtils {
        private val matrix = ThreadLocal<Matrix>()
        private val rectF = ThreadLocal<RectF>()

        /**
         * This is a port of the common [ViewGroup.offsetDescendantRectToMyCoords] from
         * the framework, but adapted to take transformations into account. The result will be the
         * bounding rect of the real transformed rect.
         *
         * @param descendant view defining the original coordinate system of rect
         * @param rect (in/out) the rect to offset from descendant to this view's coordinate system
         */
        fun offsetDescendantRect(
            parent: ViewGroup, descendant: View, rect: Rect
        ) {
            var m = matrix.get()
            if (m == null) {
                m = Matrix()
                matrix.set(m)
            } else {
                m.reset()
            }
            offsetDescendantMatrix(parent, descendant, m)
            var rectF = rectF.get()
            if (rectF == null) {
                rectF = RectF()
                DescendantOffsetUtils.rectF.set(rectF)
            }
            rectF.set(rect)
            m.mapRect(rectF)
            rect.set(
                (rectF.left + 0.5f).toInt(),
                (rectF.top + 0.5f).toInt(),
                (rectF.right + 0.5f).toInt(),
                (rectF.bottom + 0.5f).toInt()
            )

        }

        fun getDescendantRect(parent: ViewGroup, descendant: View, out: Rect) {
            out.set(0, 0, descendant.width, descendant.height)
            offsetDescendantRect(parent, descendant, out)
        }

        private fun offsetDescendantMatrix(
            target: ViewParent, view: View, m: Matrix
        ) {
            val parent = view.parent
            if (parent is View && parent !== target) {
                val vp = parent as View
                offsetDescendantMatrix(target, vp, m)
                m.preTranslate(-vp.scrollX.toFloat(), -vp.scrollY.toFloat())
            }
            m.preTranslate(view.left.toFloat(), view.top.toFloat())
            if (!view.matrix.isIdentity) {
                m.preConcat(view.matrix)
            }
        }
    }

    sealed class Result(val isSuccess: Boolean) {
        object Successful : Result(true)

        class Failure(val exception: Throwable) : Result(false)

    }

}