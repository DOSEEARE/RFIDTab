package com.example.rfidtab.ui.task

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.task.*
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.CardModelList
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kit.ImageListBase64Model
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.response.task.OverCardsResponse
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse

class TaskViewModel(application: Application) : BaseViewModel(application) {

    fun taskNetwork(withCards: Boolean): LiveData<Resource<List<TaskResponse>>> {
        return network.task(withCards)
    }

    fun changeCard(model: CardModel): LiveData<Resource<TaskCardResponse>> {
        return network.cardChange(model)
    }

    fun changeCardList(model: CardModelList): LiveData<Resource<String>> {
        return network.cardChangeList(model)
    }

    fun sendImage(list : List<CardImagesEntity>, cardId: Int, taskTypeId : Int, taskId : Int): LiveData<Resource<String>> {
        return network.sendImage(list, cardId, taskTypeId, taskId)
    }

    fun sendImageBase64 (body : ImageListBase64Model): LiveData<Resource<String>> {
        return network.sendImageListBase64(body)
    }

    fun sendOverCards(model: TaskOverCards): LiveData<Resource<String>> {
        return network.sendOverCards(model)
    }

    fun taskStatusChange(model: TaskStatusModel): LiveData<Resource<String>> {
        return network.taskStatusChange(model)
    }

    fun getOverCards(taskId: Int): LiveData<Resource<List<OverCardsResponse>>> {
        return network.getOverCards(taskId)
    }

/*
    fun getOverCardsNoLive (taskId: Int) : List<OverCardsResponse>?{
        return network.getOverCardsNoLive(taskId)
    }
*/

    fun insertTaskToDb(item: TaskWithCards) {
        db.insertTaskToDB(item)
    }

    fun insertImage(entity: CardImagesEntity) {
        return db.insertImage(entity)
    }

    fun insertOverCard(entity: OverCardsEntity) {
        return db.insertOverCard(entity)
    }

    fun insertOverCardsList(list: List<OverCardsEntity>) {
        return db.insertOverCardsList(list)
    }

    fun findTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>> {
        return db.findTasksByLogin(userLogin)
    }

    fun findImagesById(cardId: Int, taskId: Int): LiveData<List<CardImagesEntity>> {
        return db.findImagesById(cardId, taskId)
    }

    fun findAllOverCardById(id: Int): LiveData<List<OverCardsEntity>> {
        return db.findAllOverCardById(id)
    }

    suspend fun findAllOverCardByIdNO(id: Int): List<OverCardsEntity> {
        return db.findAllOverCardByIdNO(id)
    }

    fun findOverCardById (id : Int) : LiveData<OverCardsEntity>{
        return db.findOverCardById(id)
    }
    suspend fun findAllOverCardByIdNoLive(id: Int): List<OverCardsEntity> {
        return db.findAllOverCardByIdNoLive(id)
    }

    fun deleteTaskById(id: Int) {
        return db.deleteTaskById(id)
    }

    fun deleteCardsById(id: Int) {
        return db.deleteCardsById(id)
    }

    fun deleteCardImageById(cardId: Int) {
        return db.deleteCardImageById(cardId)
    }

    fun deleteAllOverCards(taskId: Int) {
        return db.deleteAllOverCards(taskId)
    }

    fun deleteAllKitOrderCards(taskId: Int) {
        return db.deleteAllOverCards(taskId)
    }

    fun deleteOverCard (id : Int){
        return db.deleteOverCard(id)
    }
    fun findCardsById(id: Int): LiveData<List<TaskCardListEntity>> {
        return db.findCardsById(id)
    }

    suspend fun findCardsByIdNoLive(id: Int): List<TaskCardListEntity> {
        return db.findCardsByIdNoLive(id)
    }

    fun updateCard(cardId: Int, rfidTag: String) {
        return db.updateCard(cardId, rfidTag)
    }

    fun updateCardConfirm(cardId: Int, isConfirmed: Boolean) {
        return db.updateCardConfirm(cardId, isConfirmed)
    }

    fun updateCardComment(cardId: Int, comment: String) {
        return db.updateCardComment(cardId, comment)
    }

    fun updateErrorComment(cardId: Int, comment: String) {
        return db.updateErrorComment(cardId, comment)
    }

    fun updateConfirmTaskCard(cardId: Int, isConfirmed: Boolean) {
        return db.updateConfirmTaskCard(cardId, isConfirmed)
    }

    fun getConfirmedCardCount(taskId: Int): LiveData <Int> {
        return db.getConfirmedCardsCount(taskId)
    }

    fun getUnConfirmedCardCount(taskId: Int): LiveData <Int> {
        return db.getUnConfirmedCardsCount(taskId)
    }

    fun confirmCards (model : ConfirmCardModel) :LiveData<Resource<String>>{
        return network.confirmCards(model)
    }
}