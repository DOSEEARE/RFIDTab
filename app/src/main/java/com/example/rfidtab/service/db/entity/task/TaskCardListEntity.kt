package com.example.rfidtab.service.db.entity.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TaskCardListEntity(
    @PrimaryKey
    @SerializedName("id") val cardId: Int,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Int,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Int,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Int,
    @SerializedName("rfidTagNo") val rfidTagNo: Long,
    @SerializedName("comment") val comment: String?,
    @SerializedName("accounting") val accounting: Int,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?,
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int
) : Parcelable