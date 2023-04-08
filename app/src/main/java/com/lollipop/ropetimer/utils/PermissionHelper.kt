package com.lollipop.ropetimer.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
        result: ActivityResultCallback<Result>
    ): ActivityResultLauncher<Array<Permission>> {
        return activity.registerForActivityResult(ResultContract(), result)
    }

    private class ResultContract : ActivityResultContract<Array<Permission>, Result>() {

        private val contractImpl = ActivityResultContracts.RequestMultiplePermissions()

        override fun createIntent(context: Context, input: Array<Permission>): Intent {
            // 获取当前版本需要请求的权限，如果高于了当前版本，那么说明不需要请求
            val permissions = Permission.currentSdkPermissions()
            // 将枚举转换为字符串
            val inputValue = permissions.map { it.key }.toTypedArray()
            // 套壳让系统去请求
            return contractImpl.createIntent(
                context,
                inputValue
            )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Result {
            // 套壳返回结果，它可以转换为我们需要的结果了
            return Result(contractImpl.parseResult(resultCode, intent))
        }

    }

    class Result(
        private val resultImpl: Map<String, @JvmSuppressWildcards Boolean>
    ) {

        fun isGranted(permission: Permission): Boolean {
            return permission.isGranted(resultImpl)
        }

        fun forEach(callback: (permission: Permission, granted: Boolean) -> Unit) {
            resultImpl.forEach { action ->
                Permission.find(action.key)?.let {
                    callback(it, action.value)
                }
            }
        }

    }

}

enum class Permission(
    val key: String,
    val label: Int,
    val description: Int,
    val sdk: Int
) {

    @SuppressLint("InlinedApi")
    NOTIFICATIONS(
        Manifest.permission.POST_NOTIFICATIONS,
        R.string.label_post_notifications,
        R.string.description_post_notifications,
        Build.VERSION_CODES.TIRAMISU
    ),
    FLOATING_WINDOW(
        Manifest.permission.SYSTEM_ALERT_WINDOW,
        R.string.label_floating_window,
        R.string.description_floating_window,
        Build.VERSION_CODES.M
    );

    fun isGranted(requestResult: Map<String, Boolean>): Boolean {
        return requestResult[this.key] == true
    }

    fun isGranted(context: Context): Boolean {
        if (this.sdk < Build.VERSION.SDK_INT) {
            return true
        }
        return ActivityCompat.checkSelfPermission(
            context,
            this.key
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {

        fun find(key: String): Permission? {
            return values().find { it.key == key }
        }

        fun currentSdkPermissions(): List<Permission> {
            val currentSdk = Build.VERSION.SDK_INT
            return values().filter { it.sdk >= currentSdk }
        }

        fun needRequestPermissions(context: Context): List<Permission> {
            return values().filter { !it.isGranted(context) }
        }
    }

}