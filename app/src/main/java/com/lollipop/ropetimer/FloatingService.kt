package com.lollipop.ropetimer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class FloatingService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
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


}