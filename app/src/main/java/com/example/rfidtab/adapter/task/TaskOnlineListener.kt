package com.example.rfidtab.adapter.task

import com.example.rfidtab.service.response.task.TaskResponse

interface TaskOnlineListener {
    fun onItemClicked(model: TaskResponse)
    fun onItemSaved(model: TaskResponse)
}