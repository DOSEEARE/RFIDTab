package com.example.rfidtab.adapter.taskDetail

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import kotlinx.android.synthetic.main.item_cards.view.card_bond
import kotlinx.android.synthetic.main.item_cards.view.card_comment
import kotlinx.android.synthetic.main.item_cards.view.card_name
import kotlinx.android.synthetic.main.item_cards.view.card_nipple
import kotlinx.android.synthetic.main.item_cards.view.card_pipe
import kotlinx.android.synthetic.main.item_cards.view.card_rfid
import kotlinx.android.synthetic.main.item_cards.view.card_scan_btn
import kotlinx.android.synthetic.main.item_mark_cards.view.*

class TaskMarkSavedAdapter(
    private val listener: TaskDetailListener,
    items: ArrayList<TaskCardListEntity> = ArrayList()
) :
    GenericRecyclerAdapter<TaskCardListEntity>(items) {

    override fun bind(item: TaskCardListEntity, holder: ViewHolder) {
        holder.itemView.card_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_name.text = "Наименование: ${item.fullName}"
        holder.itemView.card_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_bond.text = "№ муфты: ${item.couplingSerialNumber}"
        holder.itemView.card_comment.text = "Комментарии: ${item.comment}"


        holder.itemView.setOnClickListener {
            listener.cardClicked(item)
        }

        if (item.isConfirmed) {
            holder.itemView.card_status.setCardBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.card_status.setCardBackgroundColor(Color.RED)
        }

        holder.itemView.card_scan_btn.setOnClickListener {
            listener.scantBtnClicked(item)
        }

        //Добавление комментарии
        holder.itemView.card_add_comment_btn.setOnClickListener {
            listener.cameraBtnClicked(item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_mark_cards)
    }

}