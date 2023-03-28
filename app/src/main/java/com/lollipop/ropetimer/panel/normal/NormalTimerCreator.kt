package com.lollipop.ropetimer.panel.normal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.ropetimer.R
import com.lollipop.ropetimer.databinding.ItemTimerCoverSelectorBinding
import com.lollipop.ropetimer.databinding.ItemTimerScaleSelectorBinding
import com.lollipop.ropetimer.protocol.Cover
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.protocol.Scale

class NormalTimerCreator(
    private val protocol: NormalProtocol,
    private val coverGroup: RecyclerView,
    private val timerGroup: RecyclerView
) {

    private var selectedCoverPosition = 0
    private var selectedScalePosition = 0

    init {
        coverGroup.layoutManager = LinearLayoutManager(
            coverGroup.context, RecyclerView.HORIZONTAL, false
        )
        timerGroup.layoutManager = LinearLayoutManager(
            coverGroup.context, RecyclerView.HORIZONTAL, false
        )

        val themeColor = protocol.themeColor
        coverGroup.adapter = CoverAdapter(
            protocol.coverList, themeColor, ::isCoverSelected, ::onCoverClick
        )
        timerGroup.adapter = ScaleAdapter(
            protocol.scaleList, themeColor, ::isScaleSelected, ::onScaleClick
        )
    }

    private fun isCoverSelected(position: Int): Boolean {
        return selectedCoverPosition == position
    }

    private fun isScaleSelected(position: Int): Boolean {
        return selectedScalePosition == position
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onCoverClick(position: Int) {
        selectedCoverPosition = position
        coverGroup.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onScaleClick(position: Int) {
        selectedScalePosition = position
        timerGroup.adapter?.notifyDataSetChanged()
    }

    fun currentCover(): Cover {
        return protocol.coverList[selectedCoverPosition]
    }

    fun currentScale(): Scale {
        return protocol.scaleList[selectedScalePosition]
    }

    private class CoverAdapter(
        private val data: List<Cover>,
        private val themeColor: Int,
        private val selectedItem: (Int) -> Boolean,
        private val itemClickListener: (Int) -> Unit
    ) : RecyclerView.Adapter<CoverHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverHolder {
            return CoverHolder(
                ItemTimerCoverSelectorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                ::onItemClick
            ).apply {
                setTheme(themeColor)
            }
        }

        private fun onItemClick(position: Int) {
            itemClickListener(position)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: CoverHolder, position: Int) {
            holder.bind(data[position], selectedItem(position))
        }

    }

    private class ScaleAdapter(
        private val data: List<Scale>,
        private val themeColor: Int,
        private val selectedItem: (Int) -> Boolean,
        private val itemClickListener: (Int) -> Unit
    ) : RecyclerView.Adapter<ScaleHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScaleHolder {
            return ScaleHolder(
                ItemTimerScaleSelectorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                ::onItemClick
            ).apply {
                setTheme(themeColor)
            }
        }

        private fun onItemClick(position: Int) {
            itemClickListener(position)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ScaleHolder, position: Int) {
            holder.bind(data[position], selectedItem(position))
        }

    }

    private class CoverHolder(
        private val binding: ItemTimerCoverSelectorBinding,
        private val onClickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick()
            }
        }

        private fun onClick() {
            onClickListener(adapterPosition)
        }

        fun setTheme(themeColor: Int) {
            binding.root.setStrokeColor(themeColor)
        }

        fun bind(cover: Cover, isSelected: Boolean) {
            cover.load(binding.coverView)
            binding.root.isStrokeVisible = isSelected
        }

    }

    private class ScaleHolder(
        private val binding: ItemTimerScaleSelectorBinding,
        private val onClickListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick()
            }
        }

        private fun onClick() {
            onClickListener(adapterPosition)
        }

        fun setTheme(themeColor: Int) {
            binding.root.setStrokeColor(themeColor)
        }

        fun bind(scale: Scale, isSelected: Boolean) {
            binding.scaleView.text = itemView.context.getString(R.string.scale, scale.max)
            binding.root.isStrokeVisible = isSelected
        }

    }

}