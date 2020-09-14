package com.example.rfidtab.service.response.user

import com.google.gson.annotations.SerializedName

class UserInfoResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("phone") val phone: Long,
    @SerializedName("email") val email: String,
    @SerializedName("fio") val fio: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("roleTitle") val roleTitle: String
)