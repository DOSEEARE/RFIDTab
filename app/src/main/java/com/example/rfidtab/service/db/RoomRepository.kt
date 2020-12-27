package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.db.entity.kit.ProblemCardEntity
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.db.entity.task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun insertOverCardsList(list: List<OverCardsEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertOverCardsList(list)
        }
    }




    fun insertKitComment(entity: KitCommentEntity) {
        dao.insertKitComment(entity)
    }

    fun insertKitRfid(entity: KitRfidEntity) {
        dao.insertKitRfid(entity)
    }

    fun insertKitOrder(entity: KitOrderEntity) {
        dao.insertKitOrder(entity)
    }

    fun insertKitItem(entity: List<KitOrderKitEntity>) {
        dao.insertKitItem(entity)
    }

    fun insertKitCads(list: List<KitOrderCardEntity>) {
        dao.insertKitOrderCards(list)
    }

    fun findAddCardByKitId(kitId: Int): LiveData<List<KitOrderAddCardEntity>> {
        return dao.findAddCardsByKitId(kitId)
    }

    suspend fun findAddCardsByKitIdNOLV(kitId: Int): List<KitOrderAddCardEntity> {
        return dao.findAddCardsByKitIdNOLV(kitId)
    }

    fun insertKitOrderAddCard(entity: KitOrderAddCardEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertAddCard(entity)
        }
    }

    fun insertKitOrderAddCardList(list: List<KitOrderAddCardEntity>) {
        dao.insertKitOrderAddCardList(list)
    }

    fun insertProblemCard(entity: ProblemCardEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertProblemCard(entity)
        }
    }

    fun findProblemCard(cardId: Int, taskId: Int): LiveData<ProblemCardEntity> {
        return dao.findProblemCard(cardId, taskId)
    }

    fun insertKitOrderSpec(entity: KitOrderSpecificationEntity) {
        dao.insertKitOrderSpec(entity)
    }

    fun findKitItem(taskId: Int): LiveData<List<KitOrderKitEntity>> {
        return dao.findKitItem(taskId)
    }

    suspend fun findKitItemNoLV(taskId: Int): List<KitOrderKitEntity> {
        return dao.findKitItemNoLV(taskId)
    }

    fun findKitOrderSpecByKitId(kitId: Int): LiveData<KitOrderSpecificationEntity> {
        return dao.findKitOrderSpecByKitId(kitId)
    }

    fun findOrderCard(kitInt: Int): LiveData<List<KitOrderCardEntity>> {
        return dao.findKitOrderCard(kitInt)
    }

    fun findKitOrderProblemComment(kitId: Int): LiveData<List<KitOrderCardEntity>> {
        return dao.findKitOrderProblemCard(kitId)
    }

    fun findKitOrderByLogin(userLogin: String): LiveData<List<KitOrderEntity>> {
        return dao.findKitOrderByLogin(userLogin)
    }

    fun findImagesById(cardId: Int, taskId: Int): LiveData<List<CardImagesEntity>> {
        return dao.findImagesById(cardId, taskId)
    }

    fun findAllOverCardById(id: Int): LiveData<List<OverCardsEntity>> {
        return dao.findAllOverCardById(id)
    }

   suspend fun findAllOverCardByIdNO(id: Int): List<OverCardsEntity> {
        return dao.findAllOverCardByIdNO(id)
    }

    fun findOverCardById(id: Int): LiveData<OverCardsEntity> {
        return dao.findOverCardById(id)
    }

    suspend fun findAllOverCardByIdNoLive(taskId: Int): List<OverCardsEntity> {
        return dao.findAllOverCardByIdNoLIve(taskId)
    }

    fun findTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>> {
        return dao.findTasksByLogin(userLogin)
    }

    fun findKitComment(kitId: Int): LiveData<List<KitCommentEntity>> {
        return dao.findKitComment(kitId)
    }

    fun findKitItem(userLogin: String): LiveData<List<KitItemEntity>> {
        return dao.findKitItem(userLogin)
    }

    suspend fun findKitItemNoLive(userLogin: String): List<KitItemEntity> {
        return dao.findKitItemNoLive(userLogin)
    }

    fun findKitRfid(kitId: Int): LiveData<List<KitRfidEntity>> {
        return dao.findKitRfid(kitId)
    }


    fun deleteTaskById(id: Int) {
        return dao.deleteTaskById(id)
    }

    fun deleteCardImageById(cardId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteCardImageById(cardId)
        }
    }

    fun deleteAllOverCards(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteAllOverCards(taskId)
        }
    }

    fun deleteAllKitOrderCards(taskId: Int) {
        return dao.deleteAllKitOrderCards(taskId)
    }

    fun deleteAllKitOrderAddCards(kitId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteAllKitOrderAddCards(kitId)
        }
    }

    fun deleteOverCard(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteOverCard(taskId)
        }
    }

    fun deleteKitTaskById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteKitTaskById(id)
        }
    }

    fun deleteCardsById(id: Int) {
        return dao.deleteCardsById(id)
    }

    fun deleteKitRfid(rfidID: Int) {
        return dao.deleteKitRfid(rfidID)
    }

    fun deleteKitAllRfid(kitId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteKitAllRfid(kitId)
        }
    }

    fun deleteKitItem(kitId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteKitItem(kitId)
        }
    }

    fun deleteKitOrderAddCard(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.deleteKitOrderAddCard(id)
        }
    }

    fun findCardsById(id: Int): LiveData<List<TaskCardListEntity>> {
        return dao.findCardsById(id)
    }

    suspend fun findCardsByIdNoLive(id: Int): List<TaskCardListEntity> {
        return dao.findCardsByIdNoLive(id)
    }

    fun updateCard(cardId: Int, rfidTag: String) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateCard(cardId, rfidTag)

        }
    }
    fun updateCardConfirm(cardId: Int, isConfirmed: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateCardConfirm(cardId, isConfirmed)
        }
    }

  fun updateCardComment(cardId: Int, comment: String) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateCardComment(cardId, comment)
        }
    }

    fun kitOrderCardConfirm(cardId: Int, isConfirmed: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.kitOrderCardConfirm(cardId, isConfirmed)
        }
    }

    fun updateKitCard(cardId: Int, rfid: String) {
        return dao.updateKitCard(cardId, rfid)
    }

    fun updateProblemCommentKitCard(cardId: Int, problemComment: String) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateProblemCommentKitCard(cardId, problemComment)
        }
    }

    fun updateErrorComment(cardId: Int, comment: String) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateErrorComment(cardId, comment)
        }
    }

    fun updateConfirmTaskCard(cardId: Int, isConfirmed: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            return@launch dao.updateConfirmTaskCard(cardId, isConfirmed)
        }
    }

    fun getConfirmedCardsCount (taskId: Int) : LiveData <Int>{
        return dao.getConfirmedCardsCount(taskId)
    }

    fun getUnConfirmedCardsCount (taskId: Int) : LiveData <Int>{
        return dao.getUnConfirmedCardsCount(taskId)
    }
}