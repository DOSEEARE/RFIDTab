package com.example.rfidtab.adapter.kitorder.kit

import com.example.rfidtab.service.response.kitorder.KitOrderCard

interface KitOrderSavedListener {
    fun onOnlineKitClicked(model: KitOrderCard)
}