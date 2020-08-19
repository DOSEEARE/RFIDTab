package com.example.rfidtab.adapter.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.model.search.SearchCard
import kotlinx.android.synthetic.main.item_search.view.*


class SearchAdapter(
    private val listener: SearchListener,
    items: ArrayList<SearchCard> = ArrayList()
) :
    GenericRecyclerAdapter<SearchCard>(items) {

    override fun bind(item: SearchCard, holder: ViewHolder) {
        holder.itemView.search_item_title.text = "${holder.adapterPosition + 1}) ${item.fullName}"

        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search)
    }

}