package com.example.rfidtab.service.model.search

import com.google.gson.annotations.SerializedName

class SearchModel(
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String,
    @SerializedName("rfidTagNo") val rfidTagNo: String
)