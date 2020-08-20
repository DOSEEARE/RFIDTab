package com.example.rfidtab.adapter.task

import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity

interface TaskKitSavedListener {
    fun onKitItemClicked (model : KitOrderEntity)
}