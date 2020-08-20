package com.example.rfidtab.adapter.kitorder.kit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.response.kitorder.KitOrderCard
import kotlinx.android.synthetic.main.item_search.view.*

class KitOrderSavedAdapter(
    items: ArrayList<KitOrderCard> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderCard>(items) {

    override fun bind(item: KitOrderCard, holder: ViewHolder) {
        holder.itemView.search_item_title.text = "${item.fullName}"

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search)
    }

}