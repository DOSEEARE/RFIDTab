package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardListConfirmed (
    @SerializedName("id") val id: Int,
    @SerializedName("accounting") val accounting: Boolean,
    @SerializedName("comment") val comment: String,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String,
    @SerializedName("generalName") val generalName: String,
    @SerializedName("imagesLink") val imagesLink: List<String>,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String,
    @SerializedName("rfidTagNo") val rfidTagNo: String,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String
): Serializable