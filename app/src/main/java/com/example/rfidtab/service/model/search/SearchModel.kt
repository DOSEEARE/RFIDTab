package com.example.rfidtab.service.model.search

import com.google.gson.annotations.SerializedName

class SearchModel(
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Int,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Int,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Int,
    @SerializedName("rfidTagNo") val rfidTagNo: String
)