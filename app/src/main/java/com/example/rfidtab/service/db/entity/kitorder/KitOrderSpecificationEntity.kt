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
    @SerializedName("refTypeEquipment") val refTypeEquipment: String?,
    @SerializedName("refTypeDisembarkation") val refTypeDisembarkation: String?,
    @SerializedName("outerDiameterOfThePipe") val outerDiameterOfThePipe: String?,
    @SerializedName("pipeWallThickness") val pipeWallThickness: String?,
    @SerializedName("refTypeThread") val refTypeThread: String?,
    @SerializedName("odlockNipple") val odlockNipple: String?,
    @SerializedName("idlockNipple") val idlockNipple: String?,
    @SerializedName("pipeLength") val pipeLength: String?,
    @SerializedName("shoulderAngle") val shoulderAngle: String?,
    @SerializedName("turnkeyLengthNipple") val turnkeyLengthNipple: String?,
    @SerializedName("turnkeyLengthCoupling") val turnkeyLengthCoupling: String?,
    @SerializedName("refThreadCoating") val refThreadCoating: String?,
    @SerializedName("refInnerCoating") val refInnerCoating: String?,
    @SerializedName("refHardbandingCoupling") val refHardbandingCoupling: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("refTypeEquipmentTitle") val refTypeEquipmentTitle: String?,
    @SerializedName("refTypeDisembarkationTitle") val refTypeDisembarkationTitle: String?,
    @SerializedName("refTypeThreadTitle") val refTypeThreadTitle: String?,
    @SerializedName("refThreadCoatingTitle") val refThreadCoatingTitle: String?,
    @SerializedName("refInnerCoatingTitle") val refInnerCoatingTitle: String?,
    @SerializedName("refHardbandingCouplingTitle") val refHardbandingCouplingTitle: String?,
    val cardCount: String??
) : Serializable