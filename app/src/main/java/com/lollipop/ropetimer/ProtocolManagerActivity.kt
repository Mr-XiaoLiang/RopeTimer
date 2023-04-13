package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.databinding.ActivityProtocolManagerBinding

class ProtocolManagerActivity : AppCompatActivity() {

    private val binding: ActivityProtocolManagerBinding by lazy {
        ActivityProtocolManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}