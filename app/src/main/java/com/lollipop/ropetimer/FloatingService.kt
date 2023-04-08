package com.lollipop.ropetimer

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lollipop.ropetimer.utils.Permission

class FloatingService : Service() {

    companion object {
        private const val FLOATING_CHANNEL_ID = "Floating"
        private const val FLOATING_NOTIFICATION_ID = 2333
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
        updateNotification()
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
                // TODO
                .build()
        )

    }

}