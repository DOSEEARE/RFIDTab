package com.timelysoft.kainarcourierapp.service.response

import com.google.gson.annotations.SerializedName

class OrdersResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("clientId") val clientId: String,
    @SerializedName("clientEmail") val clientEmail: String,
    @SerializedName("paymentType") val paymentType: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("restaurantId") val restaurantId: String,
    @SerializedName("carbisOrderId") val carbisOrderId: Int,
    @SerializedName("discount") val discount: Int,
    @SerializedName("discountType") val discountType: Int,
    @SerializedName("address") val address: String,
    @SerializedName("addressId") val addressId: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("createdUtc") val createdUtc: String,
    @SerializedName("currentStatus") val currentStatus: Int,
    @SerializedName("currentStatusName") val currentStatusName: String,
    @SerializedName("deliverAt") val deliverAt: String,
    @SerializedName("deviceType") val deviceType: Int,
    @SerializedName("discountSum") val discountSum: Int,
    @SerializedName("paid") val paid: Int,
    @SerializedName("type") val type: Int
)