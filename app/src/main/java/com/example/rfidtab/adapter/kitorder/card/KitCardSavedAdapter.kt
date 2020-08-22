package com.example.rfidtab.adapter.kitorder.card

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import kotlinx.android.synthetic.main.item_cards.view.*

class KitCardSavedAdapter(
    val listener: KitCardSavedListener,
    items: ArrayList<KitOrderCardEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderCardEntity>(items) {

    override fun bind(item: KitOrderCardEntity, holder: ViewHolder) {
        holder.itemView.card_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_name.visibility = View.GONE
        holder.itemView.card_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_bond.text = "№ муфты: ${item.couplingSerialNumber}"

        holder.itemView.card_camera_btn.visibility = View.GONE
        holder.itemView.card_scan_btn.visibility = View.VISIBLE

        holder.itemView.card_scan_btn.setOnClickListener {
            listener.scanBtnClicked(item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_cards)
    }

}