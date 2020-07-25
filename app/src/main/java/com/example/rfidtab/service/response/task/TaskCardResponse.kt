package com.example.rfidtab.service.response.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TaskCardResponse(
    @SerializedName("id") val cardId: Int,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Int,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Int,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Int,
    @SerializedName("rfidTagNo") val rfidTagNo: String = " ",
    @SerializedName("comment") val comment: String = " ",
    @SerializedName("accounting") val accounting: Int,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?,
    @SerializedName("images") val images: List<String?>,
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int
) : Serializable