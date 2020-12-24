package com.example.rfidtab.adapter.kitorder.kitdetail

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderAddCardEntity
import kotlinx.android.synthetic.main.item_over_cards.view.*

class KitDetailAddCardAdapter(
    items: ArrayList<KitOrderAddCardEntity> = ArrayList(),
    private val isOnline: Boolean,
    private val listener: KitDetailAddCardClickListener
) :
    GenericRecyclerAdapter<KitOrderAddCardEntity>(items) {

    override fun bind(item: KitOrderAddCardEntity, holder: ViewHolder) {
        holder.itemView.card_over_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_over_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_over_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_over_bond.text = "№ муфты: ${item.couplingSerialNumber}"
        holder.itemView.card_over_comment.text = "комментарии: ${item.comment}"

        if (isOnline) {
            holder.itemView.card_over_edit_btn.isVisible = false
            holder.itemView.card_over_delete_btn.isVisible = false
        } else {
            holder.itemView.card_over_edit_btn.setOnClickListener {
                listener.editBtnClicked(item)
            }
            holder.itemView.card_over_delete_btn.setOnClickListener {
                listener.deleteBtnClicked(item.id)
            }
        }

        if (item.accounting == 1) {
            holder.itemView.card_over_count.setBackgroundColor(Color.GREEN)
            holder.itemView.card_over_count.setText("Подтвержден")
        } else {
            holder.itemView.card_over_count.setBackgroundColor(Color.RED)
            holder.itemView.card_over_count.setText("Не подтвержден")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_over_cards)
    }
}

interface KitDetailAddCardClickListener {
    fun editBtnClicked(item: KitOrderAddCardEntity)
    fun deleteBtnClicked(cardId: Int)
}