package com.example.rfidtab.adapter.taskDetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.response.task.TaskCardResponse
import kotlinx.android.synthetic.main.item_cards.view.*


class TaskDetailOnlineAdapter(
    items: ArrayList<TaskCardResponse> = ArrayList()
) :
    GenericRecyclerAdapter<TaskCardResponse>(items) {

    override fun bind(item: TaskCardResponse, holder: ViewHolder) {
        holder.itemView.card_name.text = "Наименование: ${item.fullName}"
        holder.itemView.card_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_scan_btn.visibility = View.GONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_cards)
    }

}