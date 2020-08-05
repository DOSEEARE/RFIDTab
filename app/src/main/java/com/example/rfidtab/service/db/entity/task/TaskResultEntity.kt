package com.example.rfidtab.service.db.entity.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TaskResultEntity(
    @PrimaryKey
    val id: Int,
    val userLogin: String?,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    @SerializedName("statusTitle") val statusTitle: String?,
    @SerializedName("taskTypeTitle") val taskTypeTitle: String?,
    @SerializedName("createdByFio") val createdByFio: String?,
    @SerializedName("executorFio") val executorFio: String?,
    @SerializedName("comment") val comment: String?
) : Parcelable