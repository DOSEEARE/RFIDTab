package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KitOrderSpecification(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("refTypeEquipment") val refTypeEquipment: Ref?,
    @SerializedName("refTypeDisembarkation") val refTypeDisembarkation: Ref?,
    @SerializedName("outerDiameterOfThePipe") val outerDiameterOfThePipe: String,
    @SerializedName("pipeWallThickness") val pipeWallThickness: String,
    @SerializedName("refTypeThread") val refTypeThread: Ref?,
    @SerializedName("odlockNipple") val odlockNipple: String,
    @SerializedName("idlockNipple") val idlockNipple: String,
    @SerializedName("pipeLength") val pipeLength: String,
    @SerializedName("shoulderAngle") val shoulderAngle: String,
    @SerializedName("turnkeyLengthNipple") val turnkeyLengthNipple: String,
    @SerializedName("turnkeyLengthCoupling") val turnkeyLengthCoupling: String,
    @SerializedName("refThreadCoating") val refThreadCoating: Ref?,
    @SerializedName("refInnerCoating") val refInnerCoating: Ref?,
    @SerializedName("refHardbandingCoupling") val refHardbandingCoupling: Ref?,
    @SerializedName("comment") val comment: String,
    @SerializedName("refTypeEquipmentTitle") val refTypeEquipmentTitle: String,
    @SerializedName("refTypeDisembarkationTitle") val refTypeDisembarkationTitle: String,
    @SerializedName("refTypeThreadTitle") val refTypeThreadTitle: String,
    @SerializedName("refThreadCoatingTitle") val refThreadCoatingTitle: String,
    @SerializedName("refInnerCoatingTitle") val refInnerCoatingTitle: String,
    @SerializedName("refHardbandingCouplingTitle") val refHardbandingCouplingTitle: String
) : Serializable