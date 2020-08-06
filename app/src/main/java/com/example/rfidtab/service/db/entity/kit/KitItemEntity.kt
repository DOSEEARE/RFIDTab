package com.example.rfidtab.service.db.entity.kit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class KitItemEntity(
    @PrimaryKey
    val kitId: Int,
    val comment: String?
)
