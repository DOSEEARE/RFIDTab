package com.example.rfidtab.adapter.kitorder.kitdetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderAddCardEntity
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import kotlinx.android.synthetic.main.item_over_cards.view.*

class KitDetailAddCardAdapter(
    items: ArrayList<KitOrderAddCardEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderAddCardEntity>(items) {

    override fun bind(item: KitOrderAddCardEntity, holder: ViewHolder) {
        holder.itemView.card_over_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_over_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_over_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_over_bond.text = "№ муфты: ${item.couplingSerialNumber}"
        holder.itemView.card_over_comment.text ="комментарии: ${item.comment}"
        holder.itemView.card_over_count.visibility = View.GONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_over_cards)
    }

}