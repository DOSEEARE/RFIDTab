package com.example.rfidtab.service.model

import com.google.gson.annotations.SerializedName

class TaskStatusModel(
    @SerializedName("id") val id: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("statusId") val statusId: Int
)