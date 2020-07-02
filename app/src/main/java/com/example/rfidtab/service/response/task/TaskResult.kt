package com.example.rfidtab.service.response.task

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TaskResult(
    val id : Int,
    @SerializedName("statusId") val statusId : Int,
    @SerializedName("cardList") val cardList : List<TaskCardList>,
    @SerializedName("taskTypeId") val taskTypeId : Int,
    @SerializedName("statusTitle") val statusTitle : String?,
    @SerializedName("taskTypeTitle") val taskTypeTitle : String?,
    @SerializedName("createdByFio") val createdByFio : String?,
    @SerializedName("executorFio") val executorFio : String?,
    @SerializedName("comment") val comment : String?
): Parcelable