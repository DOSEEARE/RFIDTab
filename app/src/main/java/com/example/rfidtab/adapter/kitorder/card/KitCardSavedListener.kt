package com.example.rfidtab.adapter.kitorder.card

import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity

interface KitCardSavedListener {
    fun scanBtnClicked(model: KitOrderCardEntity)
}