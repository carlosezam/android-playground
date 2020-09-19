package com.ezam.playground.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezam.playground.R
import com.google.android.material.chip.Chip

data class ChipCategory ( val icon: Int, val label: String )

class ChipCategoryViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val chip: Chip

    init {
        chip = itemView.findViewById(R.id.chip)
    }
    fun bind( data: ChipCategory ){
        chip.setText( data.label )
        chip.setChipIconResource( data.icon )
    }
}

class ChipCategoryDiffCallback : DiffUtil.ItemCallback<ChipCategory>(){
    override fun areContentsTheSame(oldItem: ChipCategory, newItem: ChipCategory): Boolean = oldItem == newItem
    override fun areItemsTheSame(oldItem: ChipCategory, newItem: ChipCategory): Boolean = oldItem == newItem
}

class ChipCategoriesAdapter : ListAdapter<ChipCategory ,ChipCategoryViewHoder>(ChipCategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipCategoryViewHoder {
        return ChipCategoryViewHoder( LayoutInflater.from(parent.context).inflate(R.layout.item_chip, parent, false) )
    }

    override fun onBindViewHolder(holder: ChipCategoryViewHoder, position: Int) {
        holder.bind( getItem(position) )
    }
}