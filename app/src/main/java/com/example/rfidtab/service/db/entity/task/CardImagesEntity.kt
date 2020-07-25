package com.example.rfidtab.service.db.entity.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class CardImagesEntity(
    @PrimaryKey
    val id: Int,
    @SerializedName("id") val cardId: Int,
    @SerializedName("imagePath") val imagePath: String
)
