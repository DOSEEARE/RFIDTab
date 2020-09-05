package com.example.rfidtab.adapter.task

import com.example.rfidtab.service.db.entity.task.TaskResultEntity

interface TaskSavedListener {
    fun onItemClicked(model: TaskResultEntity)
}