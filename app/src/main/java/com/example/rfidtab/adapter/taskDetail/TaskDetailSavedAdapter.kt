package com.example.rfidtab.adapter.taskDetail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity


class TaskDetailSavedAdapter(
    items: ArrayList<TaskCardListEntity> = ArrayList()
) :
    GenericRecyclerAdapter<TaskCardListEntity>(items) {

    override fun bind(item: TaskCardListEntity, holder: ViewHolder) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_cards)
    }


}