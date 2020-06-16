package com.timelysoft.kainarcourierapp.service.response

import com.google.gson.annotations.SerializedName

class StatusResponse (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)