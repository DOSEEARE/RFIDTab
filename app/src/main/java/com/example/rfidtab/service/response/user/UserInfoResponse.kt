package com.example.rfidtab.service.response.user

import com.google.gson.annotations.SerializedName

class UserInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: Int,
    @SerializedName("email") val email: String,
    @SerializedName("fio") val fio: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("roleTitle") val roleTitle: String
)