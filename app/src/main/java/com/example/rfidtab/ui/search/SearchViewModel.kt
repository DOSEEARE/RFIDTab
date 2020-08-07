package com.example.rfidtab.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.model.search.SearchModel
import com.example.rfidtab.service.response.SearchResponse

class SearchViewModel(application: Application) : BaseViewModel(application) {

    fun searchCard(model: SearchModel): LiveData<Resource<SearchResponse>> {
        return network.searchCard(model)
    }

}