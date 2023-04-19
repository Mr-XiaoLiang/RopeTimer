package com.lollipop.ropetimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.databinding.ActivityProtocolEditBinding
import com.lollipop.ropetimer.utils.insets.WindowInsetsEdge
import com.lollipop.ropetimer.utils.insets.WindowInsetsHelper
import com.lollipop.ropetimer.utils.insets.fixInsetsByPadding

class NormalProtocolEditActivity : AppCompatActivity() {

    companion object {

        private const val PARAMS_FILE = "protocol_file"

        fun start(context: Context, filePath: String) {
            context.startActivity(
                Intent(context, NormalProtocolEditActivity::class.java).apply {
                    putExtra(PARAMS_FILE, filePath)
                }
            )
        }
    }

    private val binding: ActivityProtocolEditBinding by lazy {
        ActivityProtocolEditBinding.inflate(layoutInflater)
    }

    private val protocolName by lazy {
        intent.getStringExtra(PARAMS_FILE) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowInsetsHelper.initWindowFlag(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.actionBar.root.fixInsetsByPadding(WindowInsetsEdge.HEADER)
        binding.actionBar.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.actionBar.titleView.setText(R.string.plan_manager)
        if (protocolName.isEmpty()) {
            // TODO 不存在的话，需要处理一下
        }
    }
}