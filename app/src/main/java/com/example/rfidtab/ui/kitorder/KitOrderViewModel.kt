package com.example.rfidtab.ui.kitorder

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.response.kitorder.KitOrderResponse

class KitOrderViewModel(application: Application) : BaseViewModel(application) {

    fun kitOrder(id: Int): LiveData<Resource<KitOrderResponse>> {
        return network.kitOrder(id)
    }

    fun insertKitOrderSpec(entity: KitOrderSpecificationEntity) {
        db.insertKitOrderSpec(entity)
    }

    fun insertKitOrder(entity: KitOrderEntity) {
        db.insertKitOrder(entity)
    }

    fun deleteKitTaskById(id: Int) {
        db.deleteKitItem(id)
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

    fun findKitItem(taskId: Int): LiveData<List<KitOrderKitEntity>> {
        return db.findKitItem(taskId)
    }

    fun findKitCards(kitId: Int): LiveData<List<KitOrderCardEntity>> {
        return db.findOrderCard(kitId)
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

    fun findAddCardByKitId(kitId: Int): LiveData<List<KitOrderAddCardEntity>> {
        return db.findAddCardByKitId(kitId)
    }

    fun confirmCards (model : ConfirmCardModel) :LiveData<Resource<String>>{
        return network.confirmCards(model)
    }
}