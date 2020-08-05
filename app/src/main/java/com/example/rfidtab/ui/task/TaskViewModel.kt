package com.example.rfidtab.ui.task

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.task.*
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import okhttp3.MultipartBody

class TaskViewModel(application: Application) : BaseViewModel(application) {

    fun taskNetwork(withCards: Boolean): LiveData<Resource<List<TaskResponse>>> {
        return network.task(withCards)
    }

    fun changeCard(model: CardModel): LiveData<Resource<TaskCardResponse>> {
        return network.cardChange(model)
    }

    fun sendImage(image: MultipartBody.Part, cardId: Int): LiveData<Resource<String>> {
        return network.sendImage(image, cardId)
    }

    fun sendOverCards(model: TaskOverCards): LiveData<Resource<String>> {
        return network.overCards(model)
    }

    fun taskStatusChange(model: TaskStatusModel): LiveData<Resource<String>> {
        return network.taskStatusChange(model)
    }

    fun insertTaskToDb(item: TaskWithCards) {
        db.insertTaskToDB(item)
    }

    fun insertImage(entity: CardImagesEntity) {
        return db.insertImage(entity)
    }

    fun insertOverCard(entity: OverCardsEntity) {
        return db.insertOverCard(entity)
    }

    fun finTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>> {
        return db.finTasksByLogin(userLogin)
    }

    fun findImagesById(id: Int): LiveData<List<CardImagesEntity>> {
        return db.findImagesById(id)
    }

    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>> {
        return db.findOverCardById(id)
    }

    fun deleteTaskById(id: Int) {
        return db.deleteTaskById(id)
    }

    fun deleteCardsById(id: Int) {
        return db.deleteCardsById(id)
    }

    fun findCardsById(id: Int): LiveData<List<TaskCardListEntity>> {
        return db.findCardsById(id)
    }

    fun updateCard(cardId: Int, rfidTag: String) {
        return db.updateCard(cardId, rfidTag)
    }

    fun updateErrorComment(cardId: Int, comment: String) {
        return db.updateErrorComment(cardId, comment)
    }

}