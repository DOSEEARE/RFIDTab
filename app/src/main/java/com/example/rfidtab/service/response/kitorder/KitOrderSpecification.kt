package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KitOrderSpecification(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("refTypeEquipment") val refTypeEquipment: KitOrderEquipment,
    @SerializedName("outerDiameterOfThePipe") val outerDiameterOfThePipe: Int,
    @SerializedName("pipeWallThickness") val pipeWallThickness: String?,
    @SerializedName("odlockNipple") val odlockNipple: String?,
    @SerializedName("idlockNipple") val idlockNipple: String?,
    @SerializedName("pipeLength") val pipeLength: String?,
    @SerializedName("shoulderAngle") val shoulderAngle: String?,
    @SerializedName("turnkeyLengthNipple") val turnkeyLengthNipple: String?,
    @SerializedName("turnkeyLengthCoupling") val turnkeyLengthCoupling: String?,
    @SerializedName("comment") val comment: String?
) : Serializable