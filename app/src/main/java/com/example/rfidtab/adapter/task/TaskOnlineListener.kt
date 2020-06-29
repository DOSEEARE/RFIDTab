package com.example.rfidtab.adapter.task

import com.example.rfidtab.service.response.task.TaskResult

interface TaskOnlineListener {
    fun onItemClicked(model: TaskResult)
    fun onItemSaved(model: TaskResult)
}