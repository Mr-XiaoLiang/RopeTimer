package com.lollipop.ropetimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ropetimer.databinding.ActivityProtocolManagerBinding
import com.lollipop.ropetimer.utils.insets.WindowInsetsEdge
import com.lollipop.ropetimer.utils.insets.WindowInsetsHelper
import com.lollipop.ropetimer.utils.insets.fixInsetsByPadding

class ProtocolManagerActivity : AppCompatActivity() {

    private val binding: ActivityProtocolManagerBinding by lazy {
        ActivityProtocolManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowInsetsHelper.initWindowFlag(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.actionBar.root.fixInsetsByPadding(WindowInsetsEdge.HEADER)
        binding.actionBar.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.actionBar.titleView.setText(R.string.plan_manager)
        binding.refreshLayout.setOnRefreshListener {
            loadData()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        // TODO Adapter
        loadData()
    }

    private fun loadData() {
        // TODO
    }

}