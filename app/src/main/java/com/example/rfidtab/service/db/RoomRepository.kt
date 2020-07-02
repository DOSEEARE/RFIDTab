package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.response.task.TaskResult

class RoomRepository(private val dao: RoomDao) {

    fun insertTaskToDB(entity: TaskWithCards) {
        dao.insertTask(entity.task)
        dao.insertCard(entity.cards)
    }

    fun updateTasks(data: TaskResult) {

    }

    fun findAllTasks(): LiveData<List<TaskResultEntity>> {
        return dao.findAllTasks()
    }

    fun deleteTaskById(id: Int) {
        return dao.deleteTaskById(id)
    }

    fun findCardsById(id: Int): LiveData<List<TaskCardListEntity>> {
        return dao.findCardsById(id)
    }

    fun updateCard (cardId : Int, rfidTag : Long) {
        return dao.updateCard(cardId, rfidTag)
    }
}