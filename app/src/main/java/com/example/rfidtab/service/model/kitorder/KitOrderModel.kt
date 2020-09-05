package com.example.rfidtab.service.model.kitorder

import com.google.gson.annotations.SerializedName

class KitOrderModel(
    @SerializedName("id") val id: Int,
    @SerializedName("list") val cardsList: List<KitOrderCards>
)