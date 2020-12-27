package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Ref(
    @SerializedName("id") val id: Int,
    @SerializedName("value") val value: String?
) : Serializable