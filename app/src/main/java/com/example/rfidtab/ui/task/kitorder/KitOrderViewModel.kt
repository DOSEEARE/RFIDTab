package com.example.rfidtab.ui.task.kitorder

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.kit.ProblemCardEntity
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.response.kitorder.KitOrderResponse

class KitOrderViewModel(application: Application) : BaseViewModel(application) {

    fun kitOrder(id: Int): LiveData<Resource<KitOrderResponse>> {
        return network.kitOrder(id)
    }

    fun insertTaskToDb(item: TaskWithCards) {
        db.insertTaskToDB(item)
    }

    fun deleteAllKitOrderCards(taskId: Int) {
        return db.deleteAllOverCards(taskId)
    }

    fun taskStatusChange(model: TaskStatusModel): LiveData<Resource<String>> {
        return network.taskStatusChange(model)
    }

    fun deleteKitOrderAddCard(id: Int) {
        return db.deleteKitOrderAddCard(id)
    }

    fun deleteAllKitOrderAddCards(kitId: Int) {
        return db.deleteAllKitOrderAddCards(kitId)
    }

    fun insertKitOrderSpec(entity: KitOrderSpecificationEntity) {
        db.insertKitOrderSpec(entity)
    }

    fun insertKitOrder(entity: KitOrderEntity) {
        db.insertKitOrder(entity)
    }

/*    fun deleteKitTaskById(id: Int) {
        db.deleteKitItem(id)
    }*/

    fun deleteKitTaskById(id : Int){
        db.deleteKitTaskById(id)
    }

    fun insertKitItem(entity: List<KitOrderKitEntity>) {
        db.insertKitItem(entity)
    }

    fun insertKitCards(list: List<KitOrderCardEntity>) {
        db.insertKitCads(list)
    }

    fun updateKitCard(cardId: Int, rfid: String) {
        return db.updateKitCard(cardId, rfid)
    }

    fun updateProblemCommentKitCard(cardId: Int, problemComment: String) {
        return db.updateProblemCommentKitCard(cardId, problemComment)
    }

    fun findKitItem(taskId: Int): LiveData<List<KitOrderKitEntity>> {
        return db.findKitItem(taskId)
    }

    suspend fun findAddCardsByKitIdNOLV(kitId: Int): List<KitOrderAddCardEntity> {
        return db.findAddCardsByKitIdNOLV(kitId)
    }

    fun findKitCards(kitId: Int): LiveData<List<KitOrderCardEntity>> {
        return db.findOrderCard(kitId)
    }

    fun findKitOrderProblemComment(kitId: Int): LiveData<List<KitOrderCardEntity>> {
        return db.findKitOrderProblemComment(kitId)
    }

    fun insertProblemCard(entity: ProblemCardEntity) {
        db.insertProblemCard(entity)
    }

    fun findProblemCard(cardId: Int, taskId: Int): LiveData<ProblemCardEntity> {
        return db.findProblemCard(cardId, taskId)
    }

    fun findKitOrderSpecByKitId(kitId: Int): LiveData<KitOrderSpecificationEntity> {
        return db.findKitOrderSpecByKitId(kitId)
    }

    fun findKitOrderByLogin(userLogin: String): LiveData<List<KitOrderEntity>> {
        return db.findKitOrderByLogin(userLogin)
    }

    fun sendKitOrderCards(model: KitOrderModel): LiveData<Resource<String>> {
        return network.sendKitOrderCards(model)
    }

    fun kitOrderCardConfirm(cardId: Int, isConfirmed: Boolean) {
        return db.kitOrderCardConfirm(cardId, isConfirmed)
    }

    fun insertKitOrderAddCard(entity: KitOrderAddCardEntity) {
        db.insertKitOrderAddCard(entity)
    }

    fun insertKitOrderAddCardList(list : List<KitOrderAddCardEntity>){
        db.insertKitOrderAddCardList(list)
    }
    fun findAddCardByKitId(kitId: Int): LiveData<List<KitOrderAddCardEntity>> {
        return db.findAddCardByKitId(kitId)
    }

    fun confirmCards (model : ConfirmCardModel) :LiveData<Resource<String>>{
        return network.confirmCards(model)
    }
}