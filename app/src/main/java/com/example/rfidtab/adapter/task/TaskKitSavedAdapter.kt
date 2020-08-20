package com.example.rfidtab.adapter.task

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import kotlinx.android.synthetic.main.item_task.view.*


class TaskKitSavedAdapter(
    private val listener: TaskKitSavedListener,
    items: ArrayList<KitOrderEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderEntity>(items) {

    override fun bind(item: KitOrderEntity, holder: ViewHolder) {
        holder.itemView.task_auth.text = "Автор: ${item.createdByFio}"
        holder.itemView.task_executor.text = "Исполнитель ${item.executorFio}"
        holder.itemView.task_type.text = "Тип: Комплектация в аренду"

        when (item.statusId?.toInt()) {
            TaskStatusEnum.sentToExecutor -> holder.itemView.task_status_tv.setBackgroundColor(Color.GREEN)
            TaskStatusEnum.takenForExecution -> holder.itemView.task_status_tv.setBackgroundColor(
                Color.RED
            )
            TaskStatusEnum.savedToLocal -> holder.itemView.task_status_tv.setBackgroundColor(Color.BLUE)
            TaskStatusEnum.done -> holder.itemView.task_status_tv.setBackgroundColor(Color.YELLOW)
            TaskStatusEnum.createdEdited -> holder.itemView.task_status_tv.setBackgroundColor(Color.CYAN)
            TaskStatusEnum.sentForRevision -> holder.itemView.task_status_tv.setBackgroundColor(
                Color.GRAY
            )
        }

        holder.itemView.task_status_tv.text = item.statusTitle
        holder.itemView.task_save_btn.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onKitItemClicked(item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_task)
    }


}