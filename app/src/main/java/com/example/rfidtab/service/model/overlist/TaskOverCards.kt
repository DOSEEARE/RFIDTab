package com.example.rfidtab.service.model.overlist

import com.google.gson.annotations.SerializedName

class TaskOverCards(
    @SerializedName("id") val id: Int,
    @SerializedName("overList") val overList: List<OverCards>
)