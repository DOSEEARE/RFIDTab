package com.example.rfidtab.adapter.kit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import kotlinx.android.synthetic.main.item_kit.view.*

class CreateKitAdapter(
    private val listener: CreateKitListener,
    items: ArrayList<KitItemEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitItemEntity>(items) {

    override fun bind(item: KitItemEntity, holder: ViewHolder) {
        holder.itemView.setOnClickListener {
            listener.kitItemClicked(item)
        }

        holder.itemView.kit_delete.setOnClickListener {
            listener.kitItemDelete(item.kitId)
        }
        holder.itemView.kit_title.text = "Создание комплекта №${holder.adapterPosition + 1}"
        holder.itemView.kit_comment.text = item.comment

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_kit)
    }

}