package com.example.rfidtab.service.model.overlist

import com.google.gson.annotations.SerializedName

class OverCards(
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("comment") val comment: String?
)