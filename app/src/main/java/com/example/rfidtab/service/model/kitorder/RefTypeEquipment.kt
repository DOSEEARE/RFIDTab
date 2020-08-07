package com.example.rfidtab.service.model.kitorder

import com.google.gson.annotations.SerializedName


data class RefTypeEquipment (
	@SerializedName("id") val id : Int,
	@SerializedName("value") val value : String?
)