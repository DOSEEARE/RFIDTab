package com.example.rfidtab.service.model.kit

import com.google.gson.annotations.SerializedName

class ImageBase64 (@SerializedName("cardId") val cardId : Int,
                   @SerializedName("images") val images : List<String>)