package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class  KitOrderCardEntity(
    @PrimaryKey
    val id: Int,
    val kitId: Int,
    val rfidTagNo: String?,
    val pipeSerialNumber: Int?,
    val serialNoOfNipple: Int?,
    val couplingSerialNumber: Int? ,
    val fullName: String?,
    val comment: String?,
    val isConfirmed: Boolean
)