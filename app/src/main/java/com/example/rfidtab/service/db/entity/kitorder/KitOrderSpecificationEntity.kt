package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class KitOrderSpecificationEntity(
    @PrimaryKey
    @SerializedName("id") val id: Int?,
    val kitId: Int,
    @SerializedName("outerDiameterOfThePipe") val outerDiameterOfThePipe: Int,
    @SerializedName("pipeWallThickness") val pipeWallThickness: Int,
    @SerializedName("odlockNipple") val odlockNipple: Int,
    @SerializedName("idlockNipple") val idlockNipple: Int,
    @SerializedName("pipeLength") val pipeLength: String?,
    @SerializedName("shoulderAngle") val shoulderAngle: String?,
    @SerializedName("turnkeyLengthNipple") val turnkeyLengthNipple: Int,
    @SerializedName("turnkeyLengthCoupling") val turnkeyLengthCoupling: Int,
    @SerializedName("comment") val comment: String?,
    val cardCount : String
) : Serializable