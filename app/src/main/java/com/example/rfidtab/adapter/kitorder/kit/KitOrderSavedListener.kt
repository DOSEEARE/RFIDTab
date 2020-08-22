package com.example.rfidtab.adapter.kitorder.kit

import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity

interface KitOrderSavedListener {
    fun onSavedKitClicked(model: KitOrderKitEntity)
}