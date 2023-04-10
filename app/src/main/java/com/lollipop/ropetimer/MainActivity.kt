package com.lollipop.ropetimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ropetimer.databinding.ActivityMainBinding
import com.lollipop.ropetimer.databinding.ItemPermissionStatusBinding
import com.lollipop.ropetimer.utils.Permission
import com.lollipop.ropetimer.utils.PermissionHelper

class MainActivity : AppCompatActivity() {

    private val permissionLauncher = PermissionHelper.register(
        this,
        ::onRequestPermissionsResult
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter = Adapter(::onPermissionClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )

    }

    private fun onPermissionClick(permission: Permission) {
        permissionLauncher.launch(arrayOf(permission))
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateStatus() {
        adapter.notifyDataSetChanged()
    }

    private fun onRequestPermissionsResult(
        result: PermissionHelper.Result
    ) {
        result.forEach { permission, _ ->
            adapter.onPermissionChanged(permission)
        }
    }

    private class Adapter(
        private val onItemClickListener: (Permission) -> Unit
    ) : RecyclerView.Adapter<PermissionStatusHolder>() {

        private val data = Permission.values()

        private var layoutInflater: LayoutInflater? = null

        fun onPermissionChanged(permission: Permission) {
            notifyItemChanged(data.indexOf(permission))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionStatusHolder {
            val inflater = layoutInflater ?: LayoutInflater.from(parent.context)
            return PermissionStatusHolder(
                ItemPermissionStatusBinding.inflate(inflater),
                ::onItemClick
            )
        }

        private fun onItemClick(position: Int) {
            if (position < 0 || position >= data.size) {
                return
            }
            onItemClickListener(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: PermissionStatusHolder, position: Int) {
            holder.bind(data[position])
        }

    }

    private class PermissionStatusHolder(
        private val binding: ItemPermissionStatusBinding,
        private val onItemClickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick()
            }
        }

        private fun onItemClick() {
            onItemClickListener(adapterPosition)
        }

        fun bind(info: Permission) {
            binding.nameView.setText(info.label)
            binding.descriptionView.setText(info.description)
            val granted = info.isGranted(itemView.context)
            binding.statusIconView.setImageResource(
                if (granted) {
                    R.drawable.ic_baseline_done_24
                } else {
                    R.drawable.ic_baseline_block_24
                }
            )
        }

    }

}