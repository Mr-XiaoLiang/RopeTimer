package com.lollipop.ropetimer

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat

class FloatingService : Service() {

    companion object {
        private const val FLOATING_CHANNEL_ID = "Floating"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createChannels()
        initFloating()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // addNewPanel()
        return START_STICKY_COMPATIBILITY
    }

    private fun initFloating() {
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

}