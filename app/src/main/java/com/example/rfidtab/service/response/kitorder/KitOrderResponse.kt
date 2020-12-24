package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName

data class KitOrderResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("mainReason") val mainReason: String?,
    @SerializedName("tenantName") val tenantName: String?,
    @SerializedName("createAt") val createAt: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("kits") val kits: List<KitOrderKit>,
    @SerializedName("statusTitle") val statusTitle: String?,
    @SerializedName("statusId") val statusId: String?,
    @SerializedName("createdByFio") val createdByFio: String?,
    @SerializedName("executorFio") val executorFio: String?,
    @SerializedName("kitCount") val kitCount: String?,
    @SerializedName("withKit") val withKit: String?,
    @SerializedName("kitType") val kitType: String?,
    @SerializedName("cardCount") val cardCount: String?,
    @SerializedName("kitCardCount") val kitCardCount: String?,
    @SerializedName("cardList") val cardList: List<KitOrderCard>

)