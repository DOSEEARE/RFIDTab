package com.example.rfidtab.adapter.search

import android.graphics.Point
import android.view.Display
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import kotlinx.android.synthetic.main.item_image.view.*


class SearchDetailAdapter(
    val context: AppCompatActivity,
    items: ArrayList<String> = ArrayList()
) :
    GenericRecyclerAdapter<String>(items) {
    private val display: Display = context.windowManager.defaultDisplay
    private val size = Point()

    override fun bind(item: String, holder: ViewHolder) {
        display.getSize(size)
        holder.itemView.card_item_image.layoutParams.height = size.x / 3
        holder.itemView.card_item_image.layoutParams.width = size.x / 3

        Glide.with(holder.itemView).load(item).into(
            holder.itemView.card_item_image
        )

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_image)
    }
}