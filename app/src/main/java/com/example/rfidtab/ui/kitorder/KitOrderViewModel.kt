package com.example.rfidtab.ui.kitorder

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
import com.example.rfidtab.service.response.kitorder.KitOrderResponse

class KitOrderViewModel(application: Application) : BaseViewModel(application) {

    fun kitOrder(id: Int): LiveData<Resource<KitOrderResponse>> {
        return network.kitOrder(id)
    }

    fun insertKitOrder(entity: KitOrderEntity) {
        db.insertKitOrder(entity)
    }

    fun deleteKitTaskById (id: Int){
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

    fun findKitOrder(): LiveData<List<KitOrderEntity>> {
        return db.findKitOrder()
    }

}