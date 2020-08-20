package com.example.rfidtab.adapter.kitorder.kit

import com.example.rfidtab.service.response.kitorder.KitOrderKit

interface KitOrderOnlineListener {
    fun onOnlineKitClicked(model: KitOrderKit)
}