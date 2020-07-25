package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import com.example.rfidtab.service.db.entity.task.*

class RoomRepository(private val dao: RoomDao) {

    fun insertTaskToDB(entity: TaskWithCards) {
        dao.insertTask(entity.task)
        dao.insertCard(entity.cards)
    }

    fun insertImage(entity: CardImagesEntity) {
        dao.insertImage(entity)
    }

    fun insertOverCard(entity: OverCardsEntity) {
        dao.insertOverCard(entity)
    }

    fun findImagesById(id: Int) {
        dao.findAllImages(id)
    }

    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>> {
       return dao.findOverCardById(id)
    }

    fun findAllTasks(): LiveData<List<TaskResultEntity>> {
        return dao.findAllTasks()
    }

    fun deleteTaskById(id: Int) {
        return dao.deleteTaskById(id)
    }

    fun deleteCardsById(id: Int) {
        return dao.deleteCardsById(id)
    }

    fun findCardsById(id: Int): LiveData<List<TaskCardListEntity>> {
        return dao.findCardsById(id)
    }

    fun updateCard(cardId: Int, rfidTag: String) {
        return dao.updateCard(cardId, rfidTag)
    }

    fun updateErrorComment(cardId: Int, comment: String) {
        return dao.updateErrorComment(cardId, comment)
    }
}