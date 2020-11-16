package com.example.rfidtab.service.db.entity.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TaskCardListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cardId: Int,
    val fullName: String?,
    val pipeSerialNumber: String?,
    val serialNoOfNipple: String?,
    val couplingSerialNumber: String?,
    val rfidTagNo: String?,
    val comment: String?,
    val accounting: Int,
    val commentProblemWithMark: String?,
    val taskId: Int,
    val taskTypeId: Int,
    val sortOrder: Int,
    val isConfirmed: Boolean,
    val isImageRequired: Boolean
) : Parcelable