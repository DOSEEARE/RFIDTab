package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
class KitOrderAddCardEntity(
    @PrimaryKey
    val id: Int,
    val kitId: Int,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Int?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Int?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Int?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("comment") val comment: String?
)