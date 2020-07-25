package com.example.rfidtab.service.db.entity.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class OverCardsEntity(
    @PrimaryKey
    val id: Int,
    val taskId: Int,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("comment") val comment: String?
)