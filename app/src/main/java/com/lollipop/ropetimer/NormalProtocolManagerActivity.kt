package com.lollipop.ropetimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ropetimer.databinding.ActivityProtocolManagerBinding
import com.lollipop.ropetimer.databinding.ItemProtocolManagerBinding
import com.lollipop.ropetimer.protocol.normal.NormalProtocolManager
import com.lollipop.ropetimer.utils.bind
import com.lollipop.ropetimer.utils.insets.WindowInsetsEdge
import com.lollipop.ropetimer.utils.insets.WindowInsetsHelper
import com.lollipop.ropetimer.utils.insets.fixInsetsByPadding
import com.lollipop.ropetimer.utils.onClick

class NormalProtocolManagerActivity : AppCompatActivity() {

    private val binding: ActivityProtocolManagerBinding by lazy {
        ActivityProtocolManagerBinding.inflate(layoutInflater)
    }

    private val normalProtocolManager = NormalProtocolManager()

    private val dataList = ArrayList<String>()

    private val adapter = ItemAdapter(dataList, ::onItemClick)

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
        binding.recyclerView.adapter = adapter
    }

    private fun onItemClick(name: String) {
        NormalProtocolEditActivity.start(this, name)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        normalProtocolManager.loadList {
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private class ItemAdapter(
        private val data: List<String>,
        private val onItemClickCallback: (String) -> Unit
    ) : RecyclerView.Adapter<ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(parent.bind(false), ::onItemClick)
        }

        private fun onItemClick(position: Int) {
            if (position < 0 || position >= data.size) {
                return
            }
            onItemClickCallback(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bind(data[position])
        }

    }

    private class ItemHolder(
        private val binding: ItemProtocolManagerBinding,
        private val onClickCallback: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.protocolNameView.onClick {
                onItemClick()
            }
        }

        private fun onItemClick() {
            onClickCallback(adapterPosition)
        }

        fun bind(name: String) {
            binding.protocolNameView.text = name
        }

    }

}