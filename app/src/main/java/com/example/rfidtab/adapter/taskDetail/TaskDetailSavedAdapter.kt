package com.example.rfidtab.adapter.taskDetail

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import kotlinx.android.synthetic.main.item_cards.view.*
import kotlinx.android.synthetic.main.item_cards.view.card_bond
import kotlinx.android.synthetic.main.item_cards.view.card_camera_btn
import kotlinx.android.synthetic.main.item_cards.view.card_name
import kotlinx.android.synthetic.main.item_cards.view.card_nipple
import kotlinx.android.synthetic.main.item_cards.view.card_pipe
import kotlinx.android.synthetic.main.item_cards.view.card_rfid
import kotlinx.android.synthetic.main.item_cards.view.card_scan_btn
import kotlinx.android.synthetic.main.item_cards_confirm.view.*

class TaskDetailSavedAdapter(
    private val listener: TaskDetailListener,
    private val taskTypeId : Int,
    items: ArrayList<TaskCardListEntity> = ArrayList()
) :
    GenericRecyclerAdapter<TaskCardListEntity>(items) {

    override fun bind(item: TaskCardListEntity, holder: ViewHolder) {
        holder.itemView.card_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_name.text = "Наименование: ${item.fullName}"
        holder.itemView.card_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_bond.text = "№ муфты: ${item.couplingSerialNumber}"

        holder.itemView.card_confirm_problem_mark.text = "Проблемы с меткой: ${item.commentProblemWithMark}"

        holder.itemView.card_confirm_comment.text = "Комментарии: ${item.comment}"

        holder.itemView.card_scan_btn.setOnClickListener {
            listener.scantBtnClicked(item)
        }

        holder.itemView.setOnClickListener {
            listener.cardClicked(item)
        }

        when (taskTypeId) {
            TaskTypeEnum.inspection -> {
                holder.itemView.card_camera_btn.setOnClickListener {
                    listener.cameraBtnClicked(item)
                }
            }
            TaskTypeEnum.kitForFix -> {
                holder.itemView.card_camera_btn.setOnClickListener {
                    listener.cameraBtnClicked(item)
                }
                holder.itemView.card_camera_btn.isVisible = true
                if (item.isImageRequired)
                    holder.itemView.card_warning_photo.isVisible = true
            }
            else -> {
                holder.itemView.card_camera_btn.isVisible = false
            }
        }

        if (item.isConfirmed) {
            holder.itemView.card_confirmed_tv.text = "Подтверждён"
            holder.itemView.card_confirmed_tv.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.card_confirmed_tv.text = "Не подтверждён"
            holder.itemView.card_confirmed_tv.setBackgroundColor(Color.RED)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_cards_confirm)
    }

}