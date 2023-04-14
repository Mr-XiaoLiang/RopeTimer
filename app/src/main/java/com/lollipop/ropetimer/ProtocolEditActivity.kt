package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.databinding.ActivityProtocolEditBinding
import com.lollipop.ropetimer.utils.insets.WindowInsetsEdge
import com.lollipop.ropetimer.utils.insets.WindowInsetsHelper
import com.lollipop.ropetimer.utils.insets.fixInsetsByPadding

class ProtocolEditActivity : AppCompatActivity() {

    private val binding: ActivityProtocolEditBinding by lazy {
        ActivityProtocolEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowInsetsHelper.initWindowFlag(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.actionBar.root.fixInsetsByPadding(WindowInsetsEdge.HEADER)
        binding.actionBar.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.actionBar.titleView.setText(R.string.plan_manager)
    }
}