package com.example.rfidtab.service.response.kitorder

import com.example.rfidtab.service.model.kitorder.OrderCardList
import com.example.rfidtab.service.model.kitorder.OrderKits
import com.google.gson.annotations.SerializedName

class KitOrderResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("mainReason") val mainReason: String?,
    @SerializedName("tenantName") val tenantName: String?,
    @SerializedName("createAt") val createAt: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("kits") val kits: List<OrderKits>,
    @SerializedName("statusTitle") val statusTitle: String?,
    @SerializedName("statusId") val statusId: String?,
    @SerializedName("createdByFio") val createdByFio: String?,
    @SerializedName("executorFio") val executorFio: String?,
    @SerializedName("cardList") val cardList: List<OrderCardList>
)