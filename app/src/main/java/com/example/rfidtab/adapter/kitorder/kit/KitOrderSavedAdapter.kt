package com.example.rfidtab.adapter.kitorder.kit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
import kotlinx.android.synthetic.main.item_search.view.*

class KitOrderSavedAdapter(
    val listener: KitOrderSavedListener,

    items: ArrayList<KitOrderKitEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderKitEntity>(items) {

    override fun bind(item: KitOrderKitEntity, holder: ViewHolder) {
        holder.itemView.search_item_title.text = "${holder.adapterPosition + 1}) ${item.title}"

        holder.itemView.setOnClickListener {
            listener.onSavedKitClicked(item, holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search)
    }

}