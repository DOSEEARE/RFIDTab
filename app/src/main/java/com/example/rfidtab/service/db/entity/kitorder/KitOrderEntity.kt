package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
class KitOrderEntity(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    val userLogin: String?,
    @SerializedName("mainReason") val mainReason: String?,
    @SerializedName("tenantName") val tenantName: String?,
    @SerializedName("createAt") val createAt: String?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("statusTitle") val statusTitle: String?,
    @SerializedName("statusId") val statusId: String?,
    @SerializedName("createdByFio") val createdByFio: String?,
    @SerializedName("executorFio") val executorFio: String?,
    @SerializedName("kitCount") val kitCount: String?,
    @SerializedName("withKit") val withKit: String?,
    @SerializedName("cardCount") val cardCount: String?,
    @SerializedName("kitCardCount") val kitCardCount: String?
) : Serializable