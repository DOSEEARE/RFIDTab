package com.example.rfidtab.ui.kitorder

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.OrderCardEntity
import com.example.rfidtab.service.response.kitorder.KitOrderResponse

class KitOrderViewModel(application: Application) : BaseViewModel(application) {

    fun kitOrder(id: Int): LiveData<Resource<KitOrderResponse>> {
        return network.kitOrder(id)
    }

    fun insertKitOrder(entity: KitOrderEntity) {
        db.insertKitOrder(entity)
    }

    fun insertKitCards(list: List<OrderCardEntity>) {
        db.insertKitCads(list)
    }

    fun findKitCards(kitId: Int): LiveData<List<OrderCardEntity>> {
        return db.findOrderCard(kitId)
    }

    fun findKitOrder(): LiveData<List<KitOrderEntity>> {
        return db.findKitOrder()
    }

}