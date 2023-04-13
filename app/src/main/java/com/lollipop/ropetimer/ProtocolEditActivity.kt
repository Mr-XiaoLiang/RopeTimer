package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.ropetimer.databinding.ActivityProtocolEditBinding

class ProtocolEditActivity : AppCompatActivity() {

    private val binding: ActivityProtocolEditBinding by lazy {
        ActivityProtocolEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}