package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.utils.PermissionHelper

class MainActivity : AppCompatActivity() {

    private val permissionLauncher = PermissionHelper.register(
        this,
        ::onRequestPermissionsResult
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun onRequestPermissionsResult(
        result: PermissionHelper.Result
    ) {
        result.forEach { permission, granted ->
            // TODO
        }
    }

}