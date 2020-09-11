package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class  KitOrderCardEntity(
    @PrimaryKey
    val id: Int,
    val kitId: Int,
    val rfidTagNo: String?,
    val pipeSerialNumber: Long?,
    val serialNoOfNipple: Long?,
    val couplingSerialNumber: Long? ,
    val fullName: String?,
    val comment: String?,
    val problemComment : String,
    val isConfirmed: Boolean
)