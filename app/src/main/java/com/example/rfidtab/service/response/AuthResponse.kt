package com.example.rfidtab.service.response

import com.google.gson.annotations.SerializedName

class AuthResponse (
    @SerializedName("jwt") val jwt : String
)