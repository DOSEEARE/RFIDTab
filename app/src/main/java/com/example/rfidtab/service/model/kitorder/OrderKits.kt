package com.example.rfidtab.service.model.kitorder

import com.google.gson.annotations.SerializedName

data class OrderKits(
    @SerializedName("id") val id: Int,
    @SerializedName("cards") val cards: List<OrderCardList>,
    @SerializedName("title") val title: String?,
    @SerializedName("specification") val specification: Specification
)