package com.example.rfidtab.adapter.card

import com.example.rfidtab.service.db.entity.task.CardImagesEntity

interface CardDetailListener {
    fun imageClicked(model: CardImagesEntity)
}