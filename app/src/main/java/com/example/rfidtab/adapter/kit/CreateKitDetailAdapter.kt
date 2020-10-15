package com.example.rfidtab.adapter.kit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import kotlinx.android.synthetic.main.item_kit_detail.view.*

class CreateKitDetailAdapter(
    private val listener: CreateKitDetailListener,
    items: ArrayList<KitRfidEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitRfidEntity>(items) {

    override fun bind(item: KitRfidEntity, holder: ViewHolder) {
        holder.itemView.kit_detail_delete.setOnClickListener {
            listener.rfidItemDelete(item, holder.adapterPosition)
        }

        holder.itemView.kit_detail_rfid.text = item.rfid

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_kit_detail)
    }

}