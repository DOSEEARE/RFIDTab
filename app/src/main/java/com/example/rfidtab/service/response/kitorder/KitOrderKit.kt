package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KitOrderKit(
    @SerializedName("id") val id: Int,
    @SerializedName("cards") val cards: List<KitOrderCard>,
    @SerializedName("title") val title: String?,
    @SerializedName("specification") val specification: KitOrderSpecification
) : Serializable