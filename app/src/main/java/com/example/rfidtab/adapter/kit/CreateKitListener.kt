package com.example.rfidtab.adapter.kit

import com.example.rfidtab.service.db.entity.kit.KitItemEntity

interface CreateKitListener {
    fun kitItemClicked(model: KitItemEntity)

    fun kitItemDelete(kitId : Int)
}