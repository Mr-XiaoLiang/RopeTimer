package com.lollipop.ropetimer

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class FloatingService : Service() {

    companion object {
        private const val FLOATING_CHANNEL_ID = "Floating"
        private const val FLOATING_NOTIFICATION_ID = 2333

        private const val PENDING_INTENT_REQUEST_HOME = 965
        private const val PENDING_INTENT_REQUEST_CLOSE = 996
        private const val PENDING_INTENT_REQUEST_ADD = 233

        private const val ACTION_CLOSE = "com.lollipop.ropetimer.FloatingService.CLOSE"
        private const val ACTION_QUICK = "com.lollipop.ropetimer.FloatingService.QUICK"
        private const val ACTION_FLOATING_CHANGED =
            "com.lollipop.ropetimer.FloatingService.FLOATING"

        @JvmStatic
        fun onServiceChanged(
            context: Context,
            lifecycle: Lifecycle,
            callback: (context: Context?, intent: Intent?) -> Unit
        ) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    callback(context, intent)
                }
            }
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        context.unregisterReceiver(receiver)
                    }
                }
            })
            ContextCompat.registerReceiver(
                context,
                receiver,
                IntentFilter(ACTION_FLOATING_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        var isRunning: Boolean = false
            private set

    }

    private val closeBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // TODO("Not yet implemented")
        }
    }

    private val quickBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // TODO("Not yet implemented")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true

        createChannels()
        initFloating()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        clearFloating()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateNotification()
        onStartCommand(intent)
        return START_STICKY_COMPATIBILITY
    }

    private fun initFloating() {
        // TODO
    }

    private fun clearFloating() {
        // TODO
    }

    private fun onStartCommand(intent: Intent?) {
        // TODO
    }

    private fun addNewPanel(protocol: Protocol) {
        // TODO
    }

    private fun createChannels() {
        NotificationManagerCompat.from(this)
            .createNotificationChannel(
                NotificationChannelCompat.Builder(
                    FLOATING_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
                ).setName(getString(R.string.floating_channel_name))
                    .setVibrationEnabled(false)
                    .build()
            )
    }

    private fun updateNotification() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(
            FLOATING_NOTIFICATION_ID,
            NotificationCompat.Builder(this, FLOATING_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_done_24)
                .setContentTitle(getString(R.string.app_name))
                .setContentInfo(getString(R.string.service_running))
                .addAction(
                    R.drawable.ic_baseline_home_24,
                    getString(R.string.home),
                    getHomeIntent()
                )
                .addAction(
                    R.drawable.ic_baseline_close_24,
                    getString(R.string.stop),
                    getCloseIntent()
                )
                .addAction(
                    R.drawable.ic_baseline_add_24,
                    getString(R.string.plan_add),
                    getAddIntent()
                )
                .build()
        )

    }

    private fun getHomeIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            PENDING_INTENT_REQUEST_HOME,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getCloseIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            PENDING_INTENT_REQUEST_CLOSE,
            Intent(ACTION_CLOSE),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getAddIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            PENDING_INTENT_REQUEST_CLOSE,
            Intent(ACTION_QUICK),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}