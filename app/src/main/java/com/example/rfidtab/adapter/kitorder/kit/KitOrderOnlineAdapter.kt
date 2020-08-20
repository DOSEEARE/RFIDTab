package com.example.rfidtab.adapter.kitorder.kit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.kit.KitOrderOnlineListener
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.response.kitorder.KitOrderKit
import kotlinx.android.synthetic.main.item_search.view.*

class KitOrderOnlineAdapter(
    private val listener: KitOrderOnlineListener,
    items: ArrayList<KitOrderKit> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderKit>(items) {

    override fun bind(item: KitOrderKit, holder: ViewHolder) {
        holder.itemView.search_item_title.text = "${holder.adapterPosition + 1}) ${item.title}"

        holder.itemView.setOnClickListener {
            listener.onOnlineKitClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search)
    }

}