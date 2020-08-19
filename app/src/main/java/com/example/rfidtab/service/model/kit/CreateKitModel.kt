package com.example.rfidtab.service.model.kit

import com.google.gson.annotations.SerializedName

class CreateKitModel    (
    @SerializedName("comment") val comment: String?,
    @SerializedName("cards") val cards: ArrayList<KitRfidCards>
)