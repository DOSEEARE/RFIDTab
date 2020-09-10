package com.example.rfidtab.service.db.entity.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CardImagesEntity(
    @PrimaryKey
    val id: Int,
    val cardId: Int,
    val taskId: Int,
    val imagePath: String
)
