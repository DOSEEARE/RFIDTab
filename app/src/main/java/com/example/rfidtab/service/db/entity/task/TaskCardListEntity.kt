package com.example.rfidtab.service.db.entity.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TaskCardListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("id") val cardId: Int,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("accounting") val accounting: Int,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?,
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("taskTypeId") val taskTypeId: Int,
    val isConfirmed : Boolean,
    val isImageRequired : Boolean
) : Parcelable