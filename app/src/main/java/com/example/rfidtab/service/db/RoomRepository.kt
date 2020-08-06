package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.db.entity.task.*

class RoomRepository(private val dao: RoomDao) {

    fun insertTaskToDB(entity: TaskWithCards) {
        dao.insertTask(entity.task)
        dao.insertCard(entity.cards)
    }

    fun insertImage(entity: CardImagesEntity) {
        dao.insertImage(entity)
    }

    fun insertKitItem(entity: KitItemEntity) {
        dao.insertKitItem(entity)
    }

    fun insertOverCard(entity: OverCardsEntity) {
        dao.insertOverCard(entity)
    }

    fun insertKitComment(entity: KitCommentEntity) {
        dao.insertKitComment(entity)
    }

    fun insertKitRfid(entity: KitRfidEntity) {
        dao.insertKitRfid(entity)
    }

    fun findImagesById(id: Int): LiveData<List<CardImagesEntity>> {
        return dao.findImagesById(id)
    }

    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>> {
        return dao.findOverCardById(id)
    }

    fun findTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>> {
        return dao.findTasksByLogin(userLogin)
    }

    fun findKitComment(kitId: Int): LiveData<List<KitCommentEntity>> {
        return dao.findKitComment(kitId)
    }

    fun findKitItem(): LiveData<List<KitItemEntity>> {
        return dao.findKitItem()
    }

    fun findKitRfid(kitId: Int): LiveData<List<KitRfidEntity>> {
        return dao.findKitRfid(kitId)
    }


    fun deleteTaskById(id: Int) {
        return dao.deleteTaskById(id)
    }

    fun deleteCardsById(id: Int) {
        return dao.deleteCardsById(id)
    }

    fun deleteKitRfid(rfidID: Int) {
        return dao.deleteKitRfid(rfidID)
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