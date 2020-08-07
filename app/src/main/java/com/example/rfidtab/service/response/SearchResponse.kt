package com.example.rfidtab.service.response

import com.example.rfidtab.service.model.search.SearchCard
import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("cardList") val cardList: List<SearchCard>,
    @SerializedName("card") val card: SearchCard,
    @SerializedName("multiple") val multiple: Boolean
)