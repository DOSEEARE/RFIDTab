package com.example.rfidtab.service.model.kitorder

import com.google.gson.annotations.SerializedName

class KitOrderCards(
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Long?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Long?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Long?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?
)