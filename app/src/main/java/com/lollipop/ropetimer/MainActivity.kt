package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.databinding.ActivityMainBinding
import com.lollipop.ropetimer.utils.PermissionHelper

class MainActivity : AppCompatActivity() {

    private val permissionLauncher = PermissionHelper.register(
        this,
        ::onRequestPermissionsResult
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        // TODO
    }

    private fun onRequestPermissionsResult(
        result: PermissionHelper.Result
    ) {
        result.forEach { permission, granted ->
            // TODO
        }
    }

}