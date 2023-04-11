package com.lollipop.ropetimer.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lollipop.ropetimer.R


object PermissionHelper {

    fun register(
        activity: AppCompatActivity,
        result: ActivityResultCallback<Unit>
    ): ActivityResultLauncher<Permission> {
        return activity.registerForActivityResult(ResultContract(), result)
    }

    private class ResultContract : ActivityResultContract<Permission, Unit>() {

        override fun createIntent(context: Context, input: Permission): Intent {
            return input.request(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?) {
        }

    }

}

sealed class Permission(
    val label: Int,
    val description: Int,
    val sdk: Int,
) {

    abstract fun request(context: Context): Intent

    protected abstract fun checkGranted(context: Context): Boolean

    open class Normal(
        val key: String, label: Int, description: Int, sdk: Int
    ) : Permission(label, description, sdk) {

        private val contractImpl = ActivityResultContracts.RequestPermission()

        override fun checkGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context,
                this.key
            ) == PackageManager.PERMISSION_GRANTED
        }

        override fun request(context: Context): Intent {
            return contractImpl.createIntent(context, key)
        }

    }

    @SuppressLint("InlinedApi")
    object Notifications : Normal(
        Manifest.permission.POST_NOTIFICATIONS,
        R.string.label_post_notifications,
        R.string.description_post_notifications,
        Build.VERSION_CODES.TIRAMISU
    )

    object FloatingWindow : Permission(
        R.string.label_floating_window,
        R.string.description_floating_window,
        Build.VERSION_CODES.M
    ) {
        override fun request(context: Context): Intent {
            return Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
        }

        override fun checkGranted(context: Context): Boolean {
            return Settings.canDrawOverlays(context)
        }

    }

    val needRequest: Boolean
        get() {
            return sdk <= Build.VERSION.SDK_INT
        }

    fun isGranted(context: Context): Boolean {
        if (!needRequest) {
            return true
        }
        return checkGranted(context)
    }

}