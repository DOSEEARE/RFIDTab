package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName



data class KitOrderSpecification(
    @SerializedName("id") val id: Int,
    @SerializedName("refTypeEquipment") val refTypeEquipment: KitOrderEquipment,
    @SerializedName("outerDiameterOfThePipe") val outerDiameterOfThePipe: Int,
    @SerializedName("pipeWallThickness") val pipeWallThickness: Int,
    @SerializedName("odlockNipple") val odlockNipple: Int,
    @SerializedName("idlockNipple") val idlockNipple: Int,
    @SerializedName("pipeLength") val pipeLength: String,
    @SerializedName("shoulderAngle") val shoulderAngle: String,
    @SerializedName("turnkeyLengthNipple") val turnturnkeyLengthNippleLengthNipple: Int,
    @SerializedName("turnkeyLengthCoupling") val turnturnkeyLengthCouplingLengthCoupling: Int,
    @SerializedName("comment") val comment: String?
)