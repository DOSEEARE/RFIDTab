package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
class KitOrderAddCardEntity(
    @PrimaryKey
    val id: Int,
    val kitId: Int,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Long?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Long?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Long?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    val accounting: Int = 0,
    @SerializedName("comment") val comment: String?
)