package com.example.rfidtab.adapter.kit

import com.example.rfidtab.service.db.entity.kit.KitRfidEntity

interface CreateKitDetailListener {
    fun rfidItemDelete(rfid: KitRfidEntity, position: Int)
}