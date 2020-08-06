package com.example.rfidtab.service.db.entity.kit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class KitRfidEntity(
    val kitId: Int,
    @PrimaryKey
    val rfidId: Int,
    val rfid: String?
)
