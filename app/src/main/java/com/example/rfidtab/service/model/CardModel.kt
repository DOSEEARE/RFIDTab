package com.example.rfidtab.service.model

import com.google.gson.annotations.SerializedName

class CardModel(
    @SerializedName("id")val id: Int,
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("accounting") val accounting: Int?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?
)