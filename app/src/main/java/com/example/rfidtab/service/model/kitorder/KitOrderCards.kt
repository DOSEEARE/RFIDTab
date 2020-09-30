package com.example.rfidtab.service.model.kitorder

import com.google.gson.annotations.SerializedName

class KitOrderCards(
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: String?,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: String?,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: String?,
    @SerializedName("rfidTagNo") val rfidTagNo: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("commentProblemWithMark") val commentProblemWithMark: String?

)