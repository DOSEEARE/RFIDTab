package com.example.rfidtab.service.response.kitorder

import com.google.gson.annotations.SerializedName


data class KitOrderEquipment (
	@SerializedName("id") val id : Int,
	@SerializedName("value") val value : String?
)