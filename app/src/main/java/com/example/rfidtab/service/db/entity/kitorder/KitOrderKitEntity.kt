package com.example.rfidtab.service.db.entity.kitorder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rfidtab.service.response.kitorder.KitOrderCard
import com.example.rfidtab.service.response.kitorder.KitOrderSpecification
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class KitOrderKitEntity(
    @PrimaryKey
    val id: Int,
    val taskId: Int,
    val title: String?
) : Serializable