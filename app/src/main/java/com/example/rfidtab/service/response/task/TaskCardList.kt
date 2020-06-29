package com.example.rfidtab.service.response.task

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TaskCardList(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("pipeSerialNumber") val pipeSerialNumber: Int,
    @SerializedName("serialNoOfNipple") val serialNoOfNipple: Int,
    @SerializedName("couplingSerialNumber") val couplingSerialNumber: Int,
    @SerializedName("rfidTagNo") val rfidTagNo: Int,
    @SerializedName("comment") val comment: String?
) : Parcelable