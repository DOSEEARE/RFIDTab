package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class KitOrderAddCardEntity(
    @PrimaryKey
    val id: Int,
    val kitId: Int,
    val taskId: Int,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    val accounting: Int = 0,
    @SerializedName("comment") val comment: String?
) : Serializable