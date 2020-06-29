package com.example.rfidtab.service.db.entity.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TaskResultEntity(
    @PrimaryKey
    val id: Int,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("statusTitle") val statusTitle: String,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("taskTypeTitle") val taskTypeTitle: String,
    @SerializedName("createdByFio") val createdByFio: String,
    @SerializedName("executorFio") val executorFio: String
) : Parcelable