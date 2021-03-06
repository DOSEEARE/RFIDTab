package com.example.rfidtab.adapter.kitorder.kit

import com.example.rfidtab.service.response.kitorder.KitOrderKit
import java.text.FieldPosition

interface KitOrderOnlineListener {
    fun onOnlineKitClicked(model: KitOrderKit, position: Int)
}