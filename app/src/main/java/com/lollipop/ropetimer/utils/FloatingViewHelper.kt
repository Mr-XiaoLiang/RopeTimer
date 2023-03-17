package com.lollipop.ropetimer.utils

import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.view.WindowManager
import androidx.viewbinding.ViewBinding

/**
 * @author lollipop
 * @date 2020/5/1 14:23
 * 悬浮的View的辅助类
 */
class FloatingViewHelper private constructor(
    private val windowManager: WindowManager
) : ViewManager {

    companion object {
        fun create(windowManager: WindowManager): FloatingViewHelper {
            return FloatingViewHelper(windowManager)
        }

        fun createParams(): WindowManager.LayoutParams {
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            return WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//不拦截焦点，否则所有界面将不可用
                        or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSPARENT
            )
        }

        fun bindDrag(
            holder: View,
            updateListener: ViewDragHelper.OnLocationUpdateListener
        ): ViewDragHelper {
            return ViewDragHelper.bind(holder).onLocationUpdate(updateListener)
        }

        fun bindDragByWindowManager(holder: View, panel: View = holder): ViewDragHelper {
            return bindDrag(holder, WindowManagerDragImpl(panel))
        }

        private fun offsetByWindowManager(panel: View, offsetX: Int, offsetY: Int) {
            val layoutParams = panel.layoutParams
            if (layoutParams is WindowManager.LayoutParams) {
                layoutParams.x += offsetX
                layoutParams.y += offsetY
                val parent = panel.parent
                if (parent is ViewManager) {
                    parent.updateViewLayout(panel, layoutParams)
                }
            }
        }

        fun detach(view: View) {
            val parent = view.parent ?: return
            if (parent is ViewManager) {
                parent.removeView(view)
            }
        }

        fun detach(panel: ViewBinding) {
            detach(panel.root)
        }

    }

    override fun addView(view: View, params: ViewGroup.LayoutParams) {
        detach(view)
        windowManager.addView(view, params)
    }

    override fun updateViewLayout(view: View, params: ViewGroup.LayoutParams) {
        windowManager.updateViewLayout(view, params)
    }

    override fun removeView(view: View) {
        if (view.parent == null) {
            return
        }
        windowManager.removeView(view)
    }

    private class WindowManagerDragImpl(
        private val panel: View
    ) : ViewDragHelper.OnLocationUpdateListener {
        override fun onLocationUpdate(view: View, offsetX: Int, offsetY: Int) {
            offsetByWindowManager(panel, offsetX, offsetY)
        }

    }

}