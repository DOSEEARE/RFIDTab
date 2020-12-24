package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class KitOrderKitEntity(
    @PrimaryKey
    val id: Int,
    val taskId: Int,
    val title: String?
) : Serializable