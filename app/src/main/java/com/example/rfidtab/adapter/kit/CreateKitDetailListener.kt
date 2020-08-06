package com.example.rfidtab.adapter.kit

import com.example.rfidtab.service.db.entity.kit.KitRfidEntity

interface CreateKitDetailListener {
    fun rfidItemClicked(rfid: KitRfidEntity, position: Int)
}