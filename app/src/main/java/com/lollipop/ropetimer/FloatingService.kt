package com.lollipop.ropetimer

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import com.lollipop.ropetimer.utils.BroadcastBus
import com.lollipop.ropetimer.utils.broadcastBus
import com.lollipop.ropetimer.utils.sendEvent

class FloatingService : LifecycleService() {

    companion object {
        private const val FLOATING_CHANNEL_ID = "Floating"
        private const val FLOATING_NOTIFICATION_ID = 2333

        private const val PENDING_INTENT_REQUEST_HOME = 965
        private const val PENDING_INTENT_REQUEST_CLOSE = 996
        private const val PENDING_INTENT_REQUEST_ADD = 233

        private const val ACTION_CLOSE = "com.lollipop.ropetimer.FloatingService.CLOSE"
        private const val ACTION_QUICK = "com.lollipop.ropetimer.FloatingService.QUICK"
        private const val ACTION_FLOATING = "com.lollipop.ropetimer.FloatingService.FLOATING"

        @JvmStatic
        fun onServiceChanged(
            context: ComponentActivity,
            callback: BroadcastBus.Callback
        ): BroadcastBus {
            return context.broadcastBus(ACTION_FLOATING, callback)
        }

        var isRunning: Boolean = false
            private set

    }

    private val closeBroadcastReceiver = BroadcastBus.register(
        this, this.lifecycle, arrayOf(ACTION_CLOSE)
    ) { _, _ ->
        stopSelf()
    }

    private val quickBroadcastReceiver = BroadcastBus.register(
        this, this.lifecycle, arrayOf(ACTION_QUICK)
    ) { _, _ ->
        quickAdd()
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        createChannels()
        initFloating()
        sendEvent(ACTION_FLOATING)
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        clearFloating()
        NotificationManagerCompat.from(this).cancel(FLOATING_NOTIFICATION_ID)
        sendEvent(ACTION_FLOATING)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        updateNotification()
        onStartCommand(intent)
        return START_STICKY_COMPATIBILITY
    }

    private fun quickAdd() {
        // TODO
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
            PENDING_INTENT_REQUEST_ADD,
            Intent(ACTION_QUICK),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}