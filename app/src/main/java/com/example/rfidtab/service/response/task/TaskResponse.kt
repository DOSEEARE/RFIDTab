package com.example.rfidtab.service.response.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class TaskResponse(
    val id: Int,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("cardList") val cardList: List<TaskCardResponse>,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("statusTitle") val statusTitle: String?,
    @SerializedName("taskTypeTitle") val taskTypeTitle: String?,
    @SerializedName("createdByFio") val createdByFio: String?,
    @SerializedName("executorFio") val executorFio: String?,
    @SerializedName("comment") val comment: String?
) : Serializable