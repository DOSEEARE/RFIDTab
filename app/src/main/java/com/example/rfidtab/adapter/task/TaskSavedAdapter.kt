package com.example.rfidtab.adapter.task

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import kotlinx.android.synthetic.main.item_task.view.*


class TaskSavedAdapter(
    private val listener: TaskSavedListener,
    items: ArrayList<TaskResultEntity> = ArrayList()
) :
    GenericRecyclerAdapter<TaskResultEntity>(items) {

    override fun bind(item: TaskResultEntity, holder: ViewHolder) {
        holder.itemView.task_auth.text = "Автор: ${item.createdByFio}"
        holder.itemView.task_executor.text = "Исполнитель ${item.executorFio}"
        holder.itemView.task_type.text = "Тип: ${item.taskTypeTitle}"
        holder.itemView.task_status_tv.text = item.statusTitle
        holder.itemView.task_save_btn.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }

        holder.itemView.task_delet_btn.setOnClickListener {
            listener.onItemDeleted(item.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_task)
    }


}