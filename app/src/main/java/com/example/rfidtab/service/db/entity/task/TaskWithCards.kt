package com.example.rfidtab.service.db.entity.task

import androidx.room.Embedded
import androidx.room.Relation

class TaskWithCards(
    @Embedded
    val task: TaskResultEntity,
    @Relation(parentColumn = "id", entityColumn = "cardId", entity = TaskCardListEntity::class)
    val cards: List<TaskCardListEntity>
)