package com.example.rfidtab.service.model.kit

import com.google.gson.annotations.SerializedName

class ImageListBase64Model (@SerializedName("taskId") val taskId : Int,
                            @SerializedName("taskTypeId") val taskTypeId : Int,
                            @SerializedName("cards") val cards : List<ImageBase64>)