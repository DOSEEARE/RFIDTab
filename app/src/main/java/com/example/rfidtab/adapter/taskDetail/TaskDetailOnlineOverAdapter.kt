package com.example.rfidtab.adapter.taskDetail

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.response.task.OverCardsResponse
import kotlinx.android.synthetic.main.item_over_cards.view.*

class TaskDetailOnlineOverAdapter(
    items: ArrayList<OverCardsResponse> = ArrayList()
) :
    GenericRecyclerAdapter<OverCardsResponse>(items) {

    override fun bind(item: OverCardsResponse, holder: ViewHolder) {
        holder.itemView.card_over_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_over_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_over_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_over_bond.text = "№ муфты: ${item.couplingSerialNumber}"
        holder.itemView.card_over_count.text = "Излишек: ${holder.adapterPosition + 1}"
        holder.itemView.card_over_comment.text = "Комментарий: ${item.comment}"

        holder.itemView.card_over_edit_btn.isVisible = false
        holder.itemView.card_over_delete_btn.isVisible = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_over_cards)
    }

}