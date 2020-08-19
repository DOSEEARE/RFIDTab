package com.example.rfidtab.service.model.search

class ImageCardModel(val imageUrl: ArrayList<String?>) {

    fun list(): ArrayList<String?> {
        return imageUrl
    }
}