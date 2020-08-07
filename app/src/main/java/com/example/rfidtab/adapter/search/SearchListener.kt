package com.example.rfidtab.adapter.search

import com.example.rfidtab.service.model.search.SearchCard

interface SearchListener {
    fun onItemClicked(model: SearchCard)
}