package com.example.rfidtab.service.model.confirm

import com.google.gson.annotations.SerializedName

class ConfirmCardModel(
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("cards") val cards: List<ConfirmCards>
)