package com.example.rfidtab.service.response.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TaskCardResponse(
    @SerializedName("id") val cardId: Int,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String,
    @SerializedName("rfidTagNo") val rfidTagNo: String = " ",
    @SerializedName("comment") val comment: String = " ",
    @SerializedName("accounting") val accounting: Int,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?,
    @SerializedName("images") val images: List<String?>,
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("cardImgRequired") val cardImgRequired: Boolean,
    @SerializedName("sortOrder") val sortOrder: Int

) : Serializable