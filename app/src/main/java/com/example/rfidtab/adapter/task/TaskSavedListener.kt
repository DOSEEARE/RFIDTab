package com.example.rfidtab.adapter.task

import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.response.task.TaskResult

interface TaskSavedListener {
    fun onItemClicked(model: TaskResultEntity)
    fun onItemDeleted(id: Int)
}