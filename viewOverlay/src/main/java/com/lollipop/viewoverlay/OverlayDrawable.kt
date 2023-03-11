package com.lollipop.viewoverlay

import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewOverlay
import java.lang.ref.WeakReference

/**
 * 模仿
 * com.google.android.material.internal.DescendantOffsetUtils
 * com.google.android.material.internal.ViewUtils
 * 制作的工具类，提供一些简单的View叠加绘制工具
 */
abstract class OverlayDrawable : Drawable() {

    protected val overlayBounds = Rect()
    protected val localBounds = Rect()
    private var attachedOverlay: WeakReference<ViewOverlay>? = null
    private var anchorView: WeakReference<View>? = null

    fun updateAnchorView(view: View) {
        anchorView = WeakReference(view)
    }

    fun setOverlayBounds(left: Int, top: Int, right: Int, bottom: Int) {
        localBounds.set(left, top, right, bottom)
        // Calculate the difference between the bounds of this view and the bounds of the root view to
        // correctly position this view in the overlay layer.
        if (updateOverlayBounds()) {
            bounds = overlayBounds
        } else {
            bounds = localBounds
            overlayBounds.set(localBounds)
        }
    }

    private fun updateOverlayBounds(): Boolean {
        val anchor = anchorView?.get() ?: return false
        return OverlayHelper.offsetDescendantRect(
            anchor,
            localBounds,
            overlayBounds
        ).isSuccess
    }

    /**
     * 附加到Overlay中
     * 它需要依赖于一个锚点View
     * 如果锚点丢失，将无法完成附加
     * 请使用 { updateAnchorView }
     */
    fun attachToOverlay() {
        val anchor = anchorView?.get()
        if (anchor == null) {
            if (OverlayHelper.isDebug) {
                throw java.lang.IllegalArgumentException("anchorView not found")
            }
            return
        }
        detachFromOverlay()
        val contentOverlay = OverlayHelper.getContentOverlay(anchor)
        contentOverlay?.add(this)
        attachedOverlay = WeakReference(contentOverlay)
    }

    fun detachFromOverlay() {
        attachedOverlay?.get()?.remove(this)
        attachedOverlay?.clear()
        attachedOverlay = null
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSPARENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

}