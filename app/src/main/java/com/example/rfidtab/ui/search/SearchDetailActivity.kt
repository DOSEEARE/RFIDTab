package com.example.rfidtab.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.R
import com.example.rfidtab.adapter.search.SearchDetailAdapter
import com.example.rfidtab.service.model.search.SearchCard
import kotlinx.android.synthetic.main.activity_search_detail.*

class SearchDetailActivity : AppCompatActivity() {
    private lateinit var data: SearchCard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        data = intent.getSerializableExtra("data") as SearchCard

        search_detail_name.text = "Наименование ${data.fullName}"
        search_detail_pipe.text = "№ Трубы ${data.fullName}"
        search_detail_nipple.text = "Ниппель ${data.serialNoOfNipple}"
        search_detail_bond.text = "Муфта ${data.couplingSerialNumber}"
        search_detail_rfid.text = "Rfid ${data.rfidTagNo}"
        search_detail_comment.text = "Комментарии ${data.comment}"

        search_detail_rv.adapter = SearchDetailAdapter(this, data.imagesLink as ArrayList<String>)
    }
}