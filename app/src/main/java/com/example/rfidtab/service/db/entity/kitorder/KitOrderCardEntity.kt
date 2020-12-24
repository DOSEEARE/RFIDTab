package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class  KitOrderCardEntity(
    @PrimaryKey
    val id: Int,
    val taskId : Int,
    val kitId: Int,
    val rfidTagNo: String?,
    val pipeSerialNumber: String?,
    val serialNoOfNipple: String?,
    val couplingSerialNumber: String? ,
    val fullName: String?,
    val comment: String?,
    val problemComment : String,
    val isConfirmed: Boolean
)