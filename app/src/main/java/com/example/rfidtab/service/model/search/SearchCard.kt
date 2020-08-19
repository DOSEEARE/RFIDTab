package com.example.rfidtab.service.model.search

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchCard (
	@SerializedName("id") val id : Int,
	@SerializedName("rfidTagNo") val rfidTagNo : String,
	@SerializedName("pipeSerialNumber") val pipeSerialNumber : Int,
	@SerializedName("serialNoOfNipple") val serialNoOfNipple : Int,
	@SerializedName("couplingSerialNumber") val couplingSerialNumber : Int,
	@SerializedName("fullName") val fullName : String,
	@SerializedName("imagesLink") val imagesLink : List<String>,
	@SerializedName("comment") val comment : 	String
) : Serializable