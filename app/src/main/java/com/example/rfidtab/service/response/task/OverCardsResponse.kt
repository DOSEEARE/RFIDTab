package com.example.rfidtab.service.response.task


import com.google.gson.annotations.SerializedName


data class OverCardsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String,
    @SerializedName("rfidTagNo") val rfidTagNo: String,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String
)