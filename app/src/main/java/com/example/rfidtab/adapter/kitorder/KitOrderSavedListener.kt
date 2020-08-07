package com.example.rfidtab.adapter.kitorder

import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity

interface KitOrderSavedListener {
    fun onKitItemClicked (model : KitOrderEntity)
}