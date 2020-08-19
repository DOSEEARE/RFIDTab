package com.example.rfidtab.ui.createkit

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.model.kit.CreateKitModel

class CreateKitViewModel(application: Application) : BaseViewModel(application) {

    fun createKit(body: CreateKitModel): LiveData<Resource<String>> {
        return network.createKit(body)
    }

    fun insertKitComment(entity: KitCommentEntity) {
        db.insertKitComment(entity)
    }

    fun insertKitRfid(entity: KitRfidEntity) {
        db.insertKitRfid(entity)
    }

    fun insertKitItem(entity: KitItemEntity) {
        db.insertKitItem(entity)
    }

    fun findKitComment(kitId: Int): LiveData<List<KitCommentEntity>> {
        return db.findKitComment(kitId)
    }

    fun findKitRfid(kitId: Int): LiveData<List<KitRfidEntity>> {
        return db.findKitRfid(kitId)
    }

    fun findKitItem(userLogin: String): LiveData<List<KitItemEntity>> {
        return db.findKitItem(userLogin)
    }

    fun deleteKitRfid(rfid: Int) {
        db.deleteKitRfid(rfid)
    }

    fun deleteKitItem(kitId: Int) {
        db.deleteKitItem(kitId)
    }

}