package com.lollipop.ropetimer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

class BroadcastBus private constructor(
    context: Context,
    lifecycle: Lifecycle,
    private val intentFilter: IntentFilter,
    private val callback: Callback
) : BroadcastReceiver(), LifecycleEventObserver {

    companion object {

        fun register(
            context: Context,
            lifecycle: Lifecycle,
            actions: Array<String>,
            callback: Callback
        ): BroadcastBus {
            val filter = IntentFilter()
            actions.forEach {
                filter.addAction(it)
            }
            return BroadcastBus(context, lifecycle, filter, callback)
        }

        fun send(context: Context, action: String, data: (Intent) -> Unit = {}) {
            val intent = Intent(action)
            data(intent)
            context.sendBroadcast(intent)
        }

    }

    /**
     * 活跃状态的生命周期
     * 默认是CREATED，可以修改为更小的RESUMED
     */
    private var activeLifecycleState = Lifecycle.State.CREATED

    /**
     * 当前的生命周期，用于自动注册
     */
    private var currentLifecycleState = lifecycle.currentState

    /**
     * 上下文的弱引用，用于自动注册广播
     */
    private val contextReference = WeakReference(context)

    /**
     * 生命周期的弱引用
     * 用于观察生命周期，并且必要的时候取消注册
     */
    private val lifecycleReference = WeakReference(lifecycle)

    /**
     * 用户设置的接收状态，当用户设置的状态为关闭时，将不会自动注册
     */
    private var userRegister = true

    /**
     * 当前是否已经注册
     * 用于避免反复注册
     */
    private var isRegister = false

    init {
        lifecycle.addObserver(this)
        autoRegister()
    }

    fun activeBy(state: Lifecycle.State): BroadcastBus {
        activeLifecycleState = state
        autoRegister()
        return this
    }

    /**
     * 销毁，它会移除所有引用，将无法再添加监听
     */
    fun destroy() {
        userRegister = false
        lifecycleReference.get()?.removeObserver(this)
        contextReference.get()?.unregisterReceiver(this)
        lifecycleReference.clear()
        contextReference.clear()
    }

    /**
     * 手动注册，用于手动解除注册之后的再次注册
     * 如果没有手动解除注册，那么不需要手动注册
     */
    fun register(): BroadcastBus {
        userRegister = true
        autoRegister()
        return this
    }

    /**
     * 手动解除注册，再再次手动注册之前，将不会自动根据生命周期注册
     */
    fun unregister(): BroadcastBus {
        userRegister = false
        autoRegister()
        return this
    }

    private fun autoRegister() {
        val context = contextReference.get() ?: return
        if (userRegister) {
            val needRegister = currentLifecycleState.isAtLeast(activeLifecycleState)
            if (needRegister) {
                if (isRegister) {
                    return
                }
                ContextCompat.registerReceiver(
                    context,
                    this,
                    intentFilter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
                isRegister = true
            } else {
                if (!isRegister) {
                    return
                }
                context.unregisterReceiver(this)
                isRegister = false
            }
        } else {
            context.unregisterReceiver(this)
            isRegister = false
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        callback.onReceive(context, intent)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        currentLifecycleState = event.targetState
        if (event == Lifecycle.Event.ON_DESTROY) {
            destroy()
        } else {
            autoRegister()
        }
    }

    fun interface Callback {
        fun onReceive(context: Context, intent: Intent)

    }

}

inline fun <reified T : Context> T.sendEvent(action: String, noinline data: (Intent) -> Unit = {}) {
    BroadcastBus.send(this, action, data)
}

fun ComponentActivity.broadcastBus(
    actions: Array<String>,
    callback: BroadcastBus.Callback
): BroadcastBus {
    return BroadcastBus.register(this, this.lifecycle, actions, callback)
}

fun ComponentActivity.broadcastBus(
    action: String,
    callback: BroadcastBus.Callback
): BroadcastBus {
    return broadcastBus(arrayOf(action), callback)
}