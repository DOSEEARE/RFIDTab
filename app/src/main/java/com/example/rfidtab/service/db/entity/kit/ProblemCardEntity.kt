package com.example.rfidtab.service.db.entity.kit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProblemCardEntity (
    @PrimaryKey
   val id : Int,
   val cardId : Int,
    val taskId : Int,
   val problemComment : String
)