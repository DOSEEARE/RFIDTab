package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderCardEntity(
    @PrimaryKey
    val id: Int,
    val kitInt: Int,
    val rfidTagNo: String?,
    val pipeSerialNumber: Int,
    val serialNoOfNipple: Int,
    val couplingSerialNumber: Int,
    val fullName: String?,
    val comment: String?
)