package com.example.rfidtab.adapter.taskDetail

import com.example.rfidtab.service.db.entity.task.TaskCardListEntity

interface TaskDetailListener {
    fun scantBtnClicked(model: TaskCardListEntity)

    fun cameraBtnClicked(model: TaskCardListEntity)

    fun cardClicked (model : TaskCardListEntity)
}