package com.example.rfidtab.service.model

import com.google.gson.annotations.SerializedName

class AuthModel(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String
)