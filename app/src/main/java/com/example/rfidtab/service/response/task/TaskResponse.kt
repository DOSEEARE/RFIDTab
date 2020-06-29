package com.example.rfidtab.service.response.task

import com.google.gson.annotations.SerializedName

class TaskResponse(
    @SerializedName("result") val result: List<TaskResult>
)